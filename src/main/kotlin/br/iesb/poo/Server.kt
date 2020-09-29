package br.iesb.poo
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import br.iesb.poo.resources.*
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime
import java.io.File


fun appendStringToList(arr: Array<String>, element: String): Array<String>{
    val mutable = arr.toMutableList()
    mutable.add(element)
    return mutable.toTypedArray()
}
fun appendCidadeToList(arr: Array<br.iesb.poo.resources.Cidade>, element: br.iesb.poo.resources.Cidade): Array<br.iesb.poo.resources.Cidade>{
    val mutable = arr.toMutableList()
    mutable.add(element)
    return mutable.toTypedArray()
}


fun createCidadeHash(map: HashMap<String, String>): br.iesb.poo.resources.Cidade {
    val cidade = Cidade(
        map.get("code")!!.toFloat().toInt(),
        map.get("date")!!,
        map.get("name")!!,
        map.get("state")!!,
        map.get("cases")!!.toInt(),
        map.get("deaths")!!.toInt()
    )
    return cidade
}

fun read_city_from_file(path: String): Array<br.iesb.poo.resources.Cidade>{
    var cidades = arrayOf<br.iesb.poo.resources.Cidade>()
    var firstLine = true
    var myFile = File(path)
    var data = HashMap<String, String>()
    var dataColumns = arrayOf<String>()
    myFile.forEachLine {
        if (firstLine) {
            firstLine = false
            it.split(",").toTypedArray().forEach {
                dataColumns = appendStringToList(dataColumns, it)
            }
        }else{
            it.split(",").toTypedArray().forEachIndexed{ idx, element ->
                data.put(dataColumns[idx], element)
            }
            var cidade = createCidadeHash(data)
            cidades = appendCidadeToList(cidades, cidade)
        }
    }
    return cidades
}


// Instancia objeto de comunicação com o DB
object Cidade : Table() {
    val code = integer("code").uniqueIndex()
    val date = datetime("date")
    val name = varchar("name", 50)
    val state = varchar("state", 50)
    val cases = integer("cases")
    val deaths = integer("deaths")

    override val primaryKey = PrimaryKey(code, name="PK_CIDADE_ID")

    fun toCidade(row: ResultRow) = Cidade(
            code = row[Cidade.code],
            date = row[Cidade.date].toString("yyyy-MM-dd"),
            name = row[Cidade.name],
            state = row[Cidade.state],
            cases = row[Cidade.cases],
            deaths = row[Cidade.deaths]
        )

}




// Módulo que possui o corpo da API
fun Application.myapp(){

    // Instala o conversor de objetos para JSON para o server
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }

    // Conexão com database H2
    Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")


    // Transações iniciais com o DB
    transaction {
        // Criação de Schema
        SchemaUtils.create(Cidade)

        // popular DB com cidades
        val cidades = read_city_from_file("./dataset/cidade.csv")
        cidades.forEach {
            val cidade = it
            Cidade.insert {
                it[Cidade.code] = cidade.code
                it[Cidade.date] = DateTime.parse(cidade.date)
                it[Cidade.name] = cidade.name
                it[Cidade.state] = cidade.state
                it[Cidade.cases] = cidade.cases
                it[Cidade.deaths] = cidade.deaths
            }
        }
    }

    // Inicialização de rotas da API
    install(Routing){
        route("/") {
            get("/") {
                val cidade = transaction {
                    Cidade.selectAll().map { Cidade.toCidade(it) }
                }

                call.respond(cidade)
            }

        }
    }
}


fun main(){

    // Inicializa o server na porta 8080 com Engine Netty
    embeddedServer(
        Netty,
        watchPaths = listOf("api-covid19"),
        module=Application::myapp,
        port=8080
    ).start(wait = true)
}
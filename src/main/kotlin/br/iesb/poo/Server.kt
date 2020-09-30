package br.iesb.poo
import br.iesb.poo.loaders.CSVLoader
import br.iesb.poo.resources.entesFederativos.Cidade
import br.iesb.poo.resources.schemas.CidadeSchema
import br.iesb.poo.resources.schemas.UsersSchema
import br.iesb.poo.resources.user.Login
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.cio.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.h2.engine.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.LocalTime
import java.io.File


fun createCidadeHash(map: HashMap<String, String>): Cidade {
    return Cidade(
        map["code"]!!.toFloat().toInt(),
        map["name"]!!,
        map["state"]!!,
        map["cases"]!!.toInt(),
        map["deaths"]!!.toInt(),
        map["date"]!!
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
        SchemaUtils.create(CidadeSchema)

        // popular DB com cidades
        val cityLoader = CSVLoader("./dataset/cases/cidade.csv")
        cityLoader.readDataFromFile()

        for (row in 0 until cityLoader.size) {
            var cityMap = HashMap<String, String>()
            for (column in cityLoader.columns) {
                cityMap[column] = cityLoader.data[column]!![row]
            }
                val cidade = createCidadeHash(cityMap)

                CidadeSchema.insert {
                    it[code] = cidade.code
                    it[name] = cidade.name
                    it[state] = cidade.state
                    it[cases] = cidade.cases
                    it[deaths] = cidade.deaths
                    it[date] = DateTime.parse(cidade.date)
            }
        }

        SchemaUtils.create(UsersSchema)

        val userLoader = CSVLoader("./dataset/users/login.csv")
        userLoader.readDataFromFile()

        println("\n\n${userLoader.size}")

        for (row in 0 until userLoader.size) {
            var userMap = HashMap<String, String>()
            for (column in userLoader.columns) {
                userMap[column] = userLoader.data[column]!![row]
            }
//            val user = createUserHash(userMap)
            val user = Login(null, null, null, null, null, userMap)
            UsersSchema.insert {
                it[email] = user.email!!
                it[cpf] = user.cpf!!
                it[password] = user.password!!
                it[tipo_log] = user.tipo_log!!
                it[date] = DateTime.parse(user.date)!!
            }
        }
    }


    // Inicialização de rotas da API
    install(Routing) {
        get("/") {
                val html = File("./src/templates/index.html").readText()
                call.respondText(html, ContentType.Text.Html)
        }

        get("/login"){
            val user = transaction {
                UsersSchema.selectAll().map {
                UsersSchema.toObject(it)
                }
            }

            call.respond(user)
        }


        post("/login") {
            val post_login = call.receive<Login>()

            val login_query = transaction {
                UsersSchema.select {
                    UsersSchema.email eq post_login.email!! and (UsersSchema.password eq post_login.password!!)
                }.map {
                    UsersSchema.toObject(it)
                }
            }
            if (login_query.size == 1){
                call.respondText("Login Efetuado com sucesso!")
            }
            else {
                call.respondText("Caso não lembre a senha faça um post para \"/mudar_senha\" com seu email e a nova senha")            }
        }

        post("/mudar_senha") {
            val post_login = call.receive<Login>()
            val login_query = transaction {
                UsersSchema.update ({
                    UsersSchema.email eq post_login.email!!
                }) {
                   it[UsersSchema.password] = post_login.password!!

                }
            }
            call.respondText("Senha alterada para ${post_login.password}")
        }

        post("/informacoes-usuario") {
            val post_login = call.receive<Login>()
            val login_query = transaction {
                UsersSchema.select {
                    UsersSchema.email eq post_login.email!! and (UsersSchema.password eq post_login.password!!)
                }.map {
                    UsersSchema.toObject(it)
                }
            }
            if (login_query.size == 1){
                call.respond(login_query)
            }
            else {
                call.respondText("Email/Senha Inválidos")            }
        }

        get("/cidade/top-cases") {
            val top_cidades = transaction {
                CidadeSchema.selectAll().orderBy(CidadeSchema.cases to SortOrder.DESC).limit(10).map {
                    CidadeSchema.toObject(it)
                }
            }
            call.respond(top_cidades)
        }


        get("/cidade/top-deaths") {
            val top_cidades = transaction {
                CidadeSchema.selectAll().orderBy(CidadeSchema.deaths to SortOrder.DESC).limit(10).map {
                    CidadeSchema.toObject(it)
                }
            }
            call.respond(top_cidades)
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
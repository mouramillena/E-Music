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
                    it[code] = cidade.code!!
                    it[name] = cidade.name!!
                    it[state] = cidade.state!!
                    it[cases] = cidade.cases!!
                    it[deaths] = cidade.deaths!!
                    it[date] = DateTime.parse(cidade.date!!)
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

    var login: Login? = null
    // Inicialização de rotas da API
    install(Routing) {
        get("/") {
                val html = File("./src/templates/index.html").readText()
                call.respondText(html, ContentType.Text.Html)
        }

        get("/all-users"){
            if (login == null) {
                call.respondText("Faça login em uma conta de administrador.")
            }
            if (login!!.tipo_log!! > 1) {
                val user = transaction {
                    UsersSchema.selectAll().map {
                        UsersSchema.toObject(it)
                    }
                }
                call.respond(user)
            } else {
                call.respondText("Sem permissão de administrador")
            }
        }

        get("/logout"){
            login = null
            call.respondText("Pode sair do sistema com segurança")
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
                login = login_query[0]
                call.respondText("Login Efetuado com sucesso!")
            }
            else {
                call.respondText("Email/Senha inválidos")            }
        }


        post("/login/cadastro") {
            val post_login = call.receive<Login>()

            val login_query = transaction {
                UsersSchema.select {
                    UsersSchema.email eq post_login.email!!
                }.map {
                    UsersSchema.toObject(it)
                }
            }

            if (login_query.size == 0) {
                transaction {
                    UsersSchema.insert {
                        it[email] = post_login.email!!
                        it[cpf] = post_login.cpf!!
                        it[password] = post_login.password!!
                        it[tipo_log] = 1
                        it[date] = DateTime.now()
                    }
                }
                call.respondText("Cadastro Realizado com sucesso")
                }
            call.respondText("E-mail já cadastrado.")
        }

        post("/mudar-senha") {
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
            if (login == null){
                call.respondText("Faça login primeiro")
            } else {
                call.respond(login!!)
            }

        }

        get("/cidade/top-cases") {
            if (login == null){
                call.respondText("Faça login primeiro")
            }
            else {
                val top_cidades = transaction {
                    CidadeSchema.selectAll().orderBy(CidadeSchema.cases to SortOrder.DESC).limit(10).map {
                        CidadeSchema.toObject(it)
                    }
                }
                call.respond(top_cidades)
            }
        }


        get("/cidade/top-deaths") {
            if (login == null) {
                call.respondText("Faça login primeiro")
            } else {

                val top_cidades = transaction {
                    CidadeSchema.selectAll().orderBy(CidadeSchema.deaths to SortOrder.DESC).limit(10).map {
                        CidadeSchema.toObject(it)
                    }
                }
                call.respond(top_cidades)
            }
        }

        post("/cidade") {
            val post_cidade = call.receive<Cidade>()
//            if (login == null) {
//                call.respondText("Faça login primeiro")
//            }
            val cidade_query = transaction {
                CidadeSchema.select {
                    CidadeSchema.name eq post_cidade.name!! and (CidadeSchema.state eq post_cidade.state!!)
                }.map {
                    CidadeSchema.toObject(it)
                }
            }
            if (cidade_query.size == 1){
                call.respond(cidade_query[0])
            } else {
                call.respondText("Cidade ${post_cidade.name} de ${post_cidade.state} NÃO encontrada.")

            }
        }

        post("/cidade/update-cases") {
            if (login == null) {
                call.respondText("Faça login primeiro")
            }

            val post_cidade = call.receive<Cidade>()
            val cidade_query = transaction {
                CidadeSchema.select {
                    CidadeSchema.name eq post_cidade.name!! and (CidadeSchema.state eq post_cidade.state!!)
                }.map {
                    CidadeSchema.toObject(it)
                }
            }
            if (cidade_query.size == 1){
                cidade_query[0].atualizarCasos(post_cidade.cases)
                transaction {
                    CidadeSchema.update({ CidadeSchema.name eq post_cidade.name!! and (CidadeSchema.state eq post_cidade.state!!) }) {
                        it[cases] = cidade_query[0].cases!!
                    }
                }
                call.respondText("Casos de ${cidade_query[0].name} de ${cidade_query[0].state} atualizado com sucesso")
            } else {
                call.respondText("Cidade ${post_cidade.name} de ${post_cidade.state} NÃO encontrada.")

            }
        }

        post("/cidade/update-deaths") {
            if (login == null) {
                call.respondText("Faça login primeiro")
            }

            val post_cidade = call.receive<Cidade>()
            val cidade_query = transaction {
                CidadeSchema.select {
                    CidadeSchema.name eq post_cidade.name!! and (CidadeSchema.state eq post_cidade.state!!)
                }.map {
                    CidadeSchema.toObject(it)
                }
            }
            if (cidade_query.size == 1){
                cidade_query[0].atualizarMortes(post_cidade.deaths)
                transaction {
                    CidadeSchema.update({ CidadeSchema.name eq post_cidade.name!! and (CidadeSchema.state eq post_cidade.state!!) }) {
                        it[deaths] = cidade_query[0].deaths!!
                    }
                }
                call.respondText("Casos de ${cidade_query[0].name} de ${cidade_query[0].state} atualizado com sucesso")
            } else {
                call.respondText("Cidade ${post_cidade.name} de ${post_cidade.state} NÃO encontrada.")

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
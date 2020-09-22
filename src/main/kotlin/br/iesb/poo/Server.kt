package br.iesb.poo
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.h2.engine.Database


fun Application.myapp(){
    // Inicialização de rotas
    routing{

//        Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")

        // Instala o conversor de objetos para JSON para o server
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }

        get("/") {
            call.respondText("<h1>Hello haha !!!! </h1>", ContentType.Text.Html)
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
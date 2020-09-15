package br.iesb.poo
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(){

    // Inicializa o server na porta 8080
    embeddedServer(Netty, 8080) {

        // Inicialização de rotas
        routing{

            // Instala o conversor de objetos para JSON para o server
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                }
            }

            get("/") {
                call.respondText("<h1>Hello World !!!! </h1>", ContentType.Text.Html)
            }
        }

    }.start(wait = true)
}
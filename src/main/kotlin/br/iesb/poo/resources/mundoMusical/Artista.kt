package br.iesb.poo.resources.mundoMusical

import br.iesb.poo.resources.crud.Crud
import br.iesb.poo.resources.schemas.AlbumSchema
import br.iesb.poo.resources.schemas.ArtistaSchema
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class Artista(
     var code: Int? = null,
     var name: String? = null,
     var sexo: String? = null,
     var idade: String? = null) : MundoMusical(code, name), Crud {

    override fun insert (t: Any): String {
        val post_artista = call.receive<Artista>()

        val artista_query = transaction {
            ArtistaSchema.select {
                ArtistaSchema.name eq post_artista.name!!
            }.map {
                ArtistaSchema.toObject(it)
            }
        }

        if (artista_query.size == 0) {
            transaction {
                ArtistaSchema.insert {
                    it[name] = post_artista.name!!
                    it[sexo] = post_artista.sexo!!
                    it[idade] = post_artista.idade!!
                }
            }
            return call.respondText("INSERÇÃO REALIZADA COM SUCESSO!")
        } else {
            return call.respondText("ERRO DE INSERÇÃO")
        }
    }

    override fun update (t:Any,schema:Any){
        val post_artista = call.receive<Artista>()

        val artista_query = transaction {
            ArtistaSchema.select {
                ArtistaSchema.code eq post_artista.code!!
            }.map {
                ArtistaSchema.toObject(it)
            }
        }

        if (artista_query.size == 0) {
            transaction {
                ArtistaSchema.update {
                    it[name] = post_artista.name!!
                    it[sexo] = post_artista.sexo!!
                    it[idade] = post_artista.idade!!
                }
            }
            return  call.respondText("ATUALIZAÇÃO REALIZADA COM SUCESSO")
        } else {
            return  call.respondText("ERRO DE ATUALIZAÇÃO")
        }
    }


    override fun delete (t:Any){
        val post_artista = call.receive<Artista>()

        val artista_query = transaction {
            ArtistaSchema.select {
                ArtistaSchema.name eq post_artista.name!! and (ArtistaSchema.code dif null)
            }.map {
                ArtistaSchema.toObject(it)
            }
        }

        if (artista_query.size == 0) {
            transaction {
                ArtistaSchema.delete {
                    it[name] = post_artista.name!!
                    it[sexo] = post_artista.sexo!!
                    it[idade] = post_artista.idade!!
                }
            }
            return  call.respondText("EXCLUSÃO COM SUCESSO")
        } else {
            return  call.respondText("ERRO DE EXCLUSÃO")
        }
    }

    }
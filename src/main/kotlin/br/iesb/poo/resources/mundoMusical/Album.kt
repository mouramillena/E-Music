package br.iesb.poo.resources.mundoMusical


import br.iesb.poo.resources.crud.Crud
import br.iesb.poo.resources.schemas.AlbumSchema
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


class Album (
    var code: Int? = null,
    var name: String? = null,
    var artista: String? = null,
    var ano: String? = null): MundoMusical(code, name), Crud {

    override fun insert(t: Any): String {

        val album_query = transaction {
            AlbumSchema.select {
                AlbumSchema.name eq post_album.name!!
            }.map {
                AlbumSchema.toObject(it)
            }
        }

        if (album_query.size == 0) {
            transaction {
                AlbumSchema.insert {
                    it[name] = post_album.name!!
                    it[artista] = post_album.artista!!
                    it[ano] = post_album.ano!!
                }
            }
            return call.respondText("INSERÇÃO REALIZADA COM SUCESSO!")
        } else {
            return call.respondText("ERRO DE INSERÇÃO")
        }
    }

    override fun update (t:Any,schema:Any){
        val post_album = call.receive<Album>()

        val album_query = transaction {
            AlbumSchema.select {
                AlbumSchema.code eq post_album.code!!
            }.map {
                AlbumSchema.toObject(it)
            }
        }

        if (album_query.size == 0) {
            transaction {
                AlbumSchema.update {
                    it[name] = post_album.name!!
                    it[artista] = post_album.artista!!
                    it[ano] = post_album.ano!!
                    }
            }
            return  call.respondText("ATUALIZAÇÃO REALIZADA COM SUCESSO!")
        } else {
            return  call.respondText("ERRO DE ATUALIZAÇÃO")
        }
    }

    override fun delete (t:Any){
        val post_album = call.receive<Album>()

        val album_query = transaction {
            AlbumSchema.select {
                AlbumSchema.name eq post_album.name!! and (AlbumSchema.artista eq post_album.artista!!)
            }.map {
                AlbumSchema.toObject(it)
            }
        }

        if (album_query.size == 0) {
            transaction {
                AlbumSchema.delete {
                    it[name] = post_album.name!!
                    it[artista] = post_album.artista!!
                    it[ano] = post_album.ano!!
                }
            }
            return  call.respondText("EXCLUSÃO COM SUCESSO!")
        } else {
            return  call.respondText("ERRO DE EXCLUSÃO")
        }
    }



}


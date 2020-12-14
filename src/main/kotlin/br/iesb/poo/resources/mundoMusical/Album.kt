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
        val post_album = t as Album
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
            return "SUCESSO"
        } else {
            return "ERRO"
        }
    }

    override fun update (t:Any):String{
        val post_album = t as Album
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
            return "SUCESSO"
        } else {
            return "ERRO"
        }
    }

    override fun delete (t:Any){
        val post_album = t as Album

        val album_query = transaction {
            AlbumSchema.select {
                AlbumSchema.name eq post_album.name!! and (AlbumSchema.artista eq post_album.artista!!)
            }.map {
                AlbumSchema.toObject(it)
            }
        }

        if (album_query.size == 0) {
            transaction {
                AlbumSchema.deleteWhere {
                    it[name] = post_album.name!!
                    it[artista] = post_album.artista!!
                    it[ano] = post_album.ano!!
                }
            }
            return "SUCESSO!"
        } else {
            return "ERRO"
        }
    }



}


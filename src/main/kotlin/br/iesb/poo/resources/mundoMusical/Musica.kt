package br.iesb.poo.resources.mundoMusical

import br.iesb.poo.resources.crud.Crud
import br.iesb.poo.resources.schemas.AlbumSchema
import br.iesb.poo.resources.schemas.MusicaSchema
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

class Musica (
    var code: Int? = null,
    var name: String? = null,
    var duracao: String? = null,
    var album: String? = null,
    var artista: String? = null,
    var idade: String? = null) : MundoMusical(code, name), Crud {

    override fun insert (t: Any): String {
        val post_musica = call.receive<Musica>()

        val musica_query = transaction {
            MusicaSchema.select {
                MusicaSchema.name eq post_musica.name!!
            }.map {
                MusicaSchema.toObject(it)
            }
        }

        if (musica_query.size == 0) {
            transaction {
                MusicaSchema.insert {
                    it[name] = post_musica.name!!
                    it[duracao] = post_musica.duracao!!
                    it[album] = post_musica.album!!
                    it[artista] = post_musica.artista!!
                    it[idade] = post_musica.idade!!
                }
            }
            return call.respondText("INSERÇÃO REALIZADA COM SUCESSO!")
        } else {
            return call.respondText("ERRO DE INSERÇÃO")
        }
    }

    override fun update (t:Any,schema:Any){
        val post_musica = call.receive<Musica>()

        val musica_query = transaction {
            MusicaSchema.select {
                MusicaSchema.code eq post_musica.code!!
            }.map {
                MusicaSchema.toObject(it)
            }
        }

        if (musica_query.size == 0) {
            transaction {
                MusicaSchema.update {
                    it[name] = post_musica.name!!
                    it[duracao] = post_musica.duracao!!
                    it[album] = post_musica.album!!
                    it[artista] = post_musica.artista!!
                    it[genero] = post_musica.genero!!
                }
            }
            return  call.respondText("ATUALIZAÇÃO REALIZADA COM SUCESSO!")
        } else {
            return  call.respondText("ERRO DE ATUALIZAÇÃO")
        }
    }

    override fun delete (t:Any){
        val post_musica = call.receive<Musica>()

        val musica_query = transaction {
            MusicaSchema.select {
                MusicaSchema.name eq post_musica.name!! and (MusicaSchema.artista eq post_musica.artista!!)
            }.map {
                MusicaSchema.toObject(it)
            }
        }

        if (musica_query.size == 0) {
            transaction {
                MusicaSchema.delete {
                    it[name] = post_musica.name!!
                    it[duracao] = post_musica.duracao!!
                    it[album] = post_musica.album!!
                    it[artista] = post_musica.artista!!
                    it[genero] = post_musica.genero!!
                }
            }
            return  call.respondText("EXCLUSÃO COM SUCESSO!")
        } else {
            return  call.respondText("ERRO DE EXCLUSÃO")
        }
    }
}

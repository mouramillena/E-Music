package br.iesb.poo.resources.mundoMusical

import br.iesb.poo.resources.crud.Crud

class Musica(
    var code: Int?,
    var name: String?,
    var duracao: String?,
    var album: String?,
    var artista: String?,
    var idade: String?) : MundoMusical(code, name), Crud {

    override fun insert (): String {
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

    override fun update (t:T,schema:T){
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

    override fun delete (t:T){
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

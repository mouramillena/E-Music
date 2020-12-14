package br.iesb.poo.resources.mundoMusical


import br.iesb.poo.resources.crud.Crud

class Album(
    var code: Int?,
    var name: String?,
    var artista: Int?,
    var ano: String? ): MundoMusical(code, name), Crud {

    override fun insert (): String {
        val post_album = call.receive<Album>()

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

    override fun update (t:T,schema:T){
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

    override fun delete (t:T){
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


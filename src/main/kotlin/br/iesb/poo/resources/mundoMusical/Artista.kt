package br.iesb.poo.resources.mundoMusical

import br.iesb.poo.resources.crud.Crud

class Artista(
     var code: Int?,
     var name: String?,
     var sexo: String?,
     var idade: date: String?) : MundoMusical(code, name), Crud {

    override fun insert (): String {
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

    override fun update (t:T,schema:T){
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


    override fun delete (t:T){
        val post_artista = call.receive<Artista>()

        val artista_query = transaction {
            ArtistaSchema.select {
                ArtistaSchema.name eq post_artista.name!! and (ArtistaSchema.artista dif null)
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
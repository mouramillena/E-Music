package br.iesb.poo.resources.schemas

import br.iesb.poo.resources.mundoMusical.Musica
import br.iesb.poo.resources.mundoMusical.Artista
import br.iesb.poo.resources.mundoMusical.Album
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object MusicaSchema : Table(), Schema<Musica> {
    var code = integer("code").uniqueIndex()
    var name = varchar("name", 50)
    var duracao = varchar("duracao", 10)
    var album = integer("album")
    var artista = integer("artista")
    var genero = varchar("genero",30)

    override val primaryKey = PrimaryKey(code, name="PK_MUSICA_ID")

    override fun toObject(row: ResultRow) = Musica(
        code = row[code],
        name = row[name],
        duracao = row[duracao],
        album = row[album],
        artista = row[artista],
        genero = row[genero]
    )

}
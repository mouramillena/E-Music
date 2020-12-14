package br.iesb.poo.resources.schemas

import br.iesb.poo.resources.mundoMusical.Musica
import br.iesb.poo.resources.mundoMusical.Artista
import br.iesb.poo.resources.mundoMusical.Album
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object AlbumSchema : Table(), Schema<Album> {
    var code = integer("code").uniqueIndex()
    var name = varchar("name", 50)
    var artista = varchar("artista", 50)
    var ano = integer("ano")


    override val primaryKey = PrimaryKey(code, name="PK_ALBUM_ID")

    override fun toObject(row: ResultRow) = Album(
        code = row[code],
        name = row[name],
        artista = row[artista],
        ano = row[ano].toString("yyyy-MM-dd")
    )

}
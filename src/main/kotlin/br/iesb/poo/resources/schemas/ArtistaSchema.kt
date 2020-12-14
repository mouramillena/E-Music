package br.iesb.poo.resources.schemas

import br.iesb.poo.resources.mundoMusical.Musica
import br.iesb.poo.resources.mundoMusical.Artista
import br.iesb.poo.resources.mundoMusical.Album
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object ArtistaSchema : Table(), Schema<Artista> {
    var code = integer("code").uniqueIndex()
    var name = varchar("name", 50)
    var sexo = integer("sexo")
    var idade = datetime("idade")

    override val primaryKey = PrimaryKey(code, name="PK_ARTISTA_ID")

    override fun toObject(row: ResultRow) = Artista(
        code = row[code],
        name = row[name],
        sexo = row[sexo],
        idade = row[idade].toString("yyyy-MM-dd")
    )

}
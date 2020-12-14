package br.iesb.poo.resources.schemas


import br.iesb.poo.resources.crud.Crud

import br.iesb.poo.resources.mundoMusical.Musica
import br.iesb.poo.resources.mundoMusical.Artista
import br.iesb.poo.resources.mundoMusical.Album
import br.iesb.poo.resources.mundoMusical.MundoMusical

import br.iesb.poo.resources.schemas.MusicaSchema
import br.iesb.poo.resources.schemas.ArtistaSchema
import br.iesb.poo.resources.schemas.AlbumSchema
import br.iesb.poo.resources.schemas.UsersSchema
import br.iesb.poo.resources.user.Login
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object UsersSchema : Table(), Schema<Login> {
    var email = varchar("email", 50).uniqueIndex()
    var cpf = varchar("cpf", 20)
    var password = varchar("password", 30)
    var tipo_log = integer("tipo_log")
    var date = datetime("date")

    override val primaryKey = PrimaryKey(email, name="PK_USER_EMAIL")

    override fun toObject(row: ResultRow) = Login(
        email = row[email],
        cpf = row[cpf],
        password = row[password],
        tipo_log = row[tipo_log],
        date = row[date].toString("yyyy-MM-dd")
    )

}
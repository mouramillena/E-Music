package br.iesb.poo.resources.schemas

import br.iesb.poo.resources.entesFederativos.Cidade
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object CidadeSchema : Table(), Schema<Cidade> {
    var code = integer("code").uniqueIndex()
    var name = varchar("name", 50)
    var state = varchar("state", 50)
    var cases = integer("cases")
    var deaths = integer("deaths")
    var date = datetime("date")

    override val primaryKey = PrimaryKey(code, name="PK_CIDADE_ID")

    override fun toObject(row: ResultRow) = Cidade(
        code = row[code],
        name = row[name],
        state = row[state],
        cases = row[cases],
        deaths = row[deaths],
        date = row[date].toString("yyyy-MM-dd")
    )

}
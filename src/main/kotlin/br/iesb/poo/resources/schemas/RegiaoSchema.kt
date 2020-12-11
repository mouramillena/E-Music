package br.iesb.poo.resources.schemas

import br.iesb.poo.resources.entesFederativos.Cidade
import br.iesb.poo.resources.entesFederativos.Regiao
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime


object RegiaoSchema : Table(), Schema<Regiao> {
    var code = integer("code").uniqueIndex()
    var name = varchar("name", 50)
    var cases = integer("cases")
    var deaths = integer("deaths")
    var date = datetime("date")
//MUDEI AQUI ESTAVA CIDADE
    override val primaryKey = PrimaryKey(code, name="PK_REGIAO_ID")

    override fun toObject(row: ResultRow) = Regiao(
        code = row[code],
        name = row[name],
        cases = row[cases],
        deaths = row[deaths],
        date = row[date].toString("yyyy-MM-dd")
    )

}
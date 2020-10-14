package br.iesb.poo.resources.user

import br.iesb.poo.utils.HashMapParser
import java.time.LocalDate

class Login(
    var email:String?,
    var cpf: String?,
    var password: String?,
    var tipo_log: Int?,
    var date: String?,
    map: HashMap<String, String>? = null): HashMapParser {


    override fun createFromHash(map: HashMap<String, String>): Unit {
        this.email = map["email"]!!
        this.cpf = map["cpf"]!!
        this.password = map["password"]!!
        this.tipo_log = map["tipo_log"]!!.toInt()
        this.date = map["date"]!!

    }

    init {
        if (map != null) {
            createFromHash(map)
        }
        else {
            if (this.tipo_log == null) {
                this.tipo_log = 3
            }
            if (this.date == null) {
                this.date = LocalDate.now().toString()
            }
        }
    }
}

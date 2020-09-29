package br.iesb.poo.resources

import org.joda.time.DateTime

class Cidade(
    val code: Int,
    val date: String,
    val name: String,
    val state: String,
    val cases: Int,
    val deaths: Int) {

}
}
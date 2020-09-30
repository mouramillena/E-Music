package br.iesb.poo.resources.entesFederativos

import br.iesb.poo.resources.contadores.Contadores

class Regiao(
    var name: String,
    var code: Int,
    override var cases: Int,
    override var deaths: Int,
    override var date: String): EnteFederativo(code, name), Contadores{
}
package br.iesb.poo.resources.entesFederativos

import br.iesb.poo.resources.contadores.Contadores

class Regiao(
    var code: Int?,
    var name: String?,
    override var cases: Int?,
    override var deaths: Int?,
    override var date: String? ): EnteFederativo(code, name), Contadores{
}


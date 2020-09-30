package br.iesb.poo.resources.entesFederativos

import br.iesb.poo.resources.contadores.Contadores

class Cidade(
    var code: Int,
    var name: String,
    var state: String,
    override var cases: Int,
    override var deaths: Int,
    override var date: String ) : EnteFederativo(code, name), Contadores {

}

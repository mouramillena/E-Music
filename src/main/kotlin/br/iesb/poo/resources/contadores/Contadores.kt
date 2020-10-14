package br.iesb.poo.resources.contadores

import java.time.LocalDateTime


interface Contadores {

    var cases: Int?
    var deaths: Int?
    var date: String?

    fun atualizarCasos(quantidade: Int?){
        cases = cases?.plus(quantidade ?: 1)
        date = LocalDateTime.now().toString()
    }

    fun atualizarMortes(quantidade: Int?){
        deaths = deaths?.plus(quantidade ?: 1)
        date = LocalDateTime.now().toString()
    }
}
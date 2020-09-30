package br.iesb.poo.resources.contadores

import java.time.LocalDateTime


interface Contadores {

    var cases: Int
    var deaths: Int
    var date: String

    fun atualizarCasos(quantidade: Int?){
        cases += quantidade ?: 1
        date = LocalDateTime.now().toString()
    }

    fun atualizarMortes(quantidade: Int?){
        deaths += quantidade ?: 1
        date = LocalDateTime.now().toString()
    }
}
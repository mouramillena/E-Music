package br.iesb.poo.resources.contadores

import java.time.LocalDateTime


interface Contadores {

    var cases: Int?
    var deaths: Int?
    var date: String?

    //INCREMENTA CASOS
    fun atualizarCasos(quantidade: Int?){
        cases = cases?.plus(quantidade ?: 1)
        date = LocalDateTime.now().toString()
    }

    //INCREMENTA MORTES
    fun atualizarMortes(quantidade: Int?){
        deaths = deaths?.plus(quantidade ?: 1)
        date = LocalDateTime.now().toString()
    }

    //DECREMENTA MORTES
    fun decrementaMortes(quantidade: Int?){
        deaths = deaths?.minus(quantidade ?: 1)
        date = LocalDateTime.now().toString()
    }

    //DECREMENTA CASOS
    fun decrementaCasos(quantidade: Int?){
        cases = cases?.minus(quantidade ?: 1)
        date = LocalDateTime.now().toString()
    }

}
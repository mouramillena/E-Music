package br.iesb.poo.resources.crud


interface Crud {

    fun insert(t: Any): String
    fun update(t: Any): String
    fun delete(t: Any): String

}
package br.iesb.poo.resources.crud



interface Crud {

    fun insert ():String
    fun update (t:T,schema:T)
    fun delete (t:T)

}
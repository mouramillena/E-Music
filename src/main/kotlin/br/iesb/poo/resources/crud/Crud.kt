package br.iesb.poo.resources.crud



interface Crud {

    fun insert (t:Any):String
    fun update (t:Any,schema:Any)
    fun delete (t:Any)

}
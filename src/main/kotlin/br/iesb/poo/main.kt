package br.iesb.poo

import java.io.File

fun main(){
    var myFile = File("/Users/diogotelheirodonascimento/Documents/Projects/poo_pratico/api-covid19/dataset/regiao.csv")
    myFile.forEachLine { println(it) }

}
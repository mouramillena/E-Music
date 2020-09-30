package br.iesb.poo

import br.iesb.poo.loaders.CSVLoader


fun main(){
    val userLoader = CSVLoader("./dataset/users/login.csv")
    userLoader.readDataFromFile()

    for (row in 0 until userLoader.size) {
        var userMap = HashMap<String, String>()
        for (column in userLoader.columns) {
            userMap[column] = userLoader.data[column]!![row]
        }
    }
}



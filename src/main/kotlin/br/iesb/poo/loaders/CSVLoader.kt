package br.iesb.poo.loaders

import br.iesb.poo.utils.ListAppender
import java.io.File

class CSVLoader(path: String): ListAppender<String>{
    private var file = File(path)
    lateinit var columns: List<String>
    lateinit var data: HashMap<String, List<String>>
    var size = 0



    fun readDataFromFile() {
        var firstLine = true
        this.columns = listOf<String>()
        this.data = HashMap<String, List<String>>()

        this.file.forEachLine { line ->
            if (firstLine) {
                firstLine = false
                line.split(",").toTypedArray().forEach {
                    this.data[it] = listOf()
                    this.columns = append(this.columns, it)
                }
            }
            else {
                line.split(",").toTypedArray().forEachIndexed { idx, element ->
                    val key = this.columns[idx]
                    this.data[key] = append(this.data[key]!!, element)
                }
                this.size += 1
            }
        }
    }


}
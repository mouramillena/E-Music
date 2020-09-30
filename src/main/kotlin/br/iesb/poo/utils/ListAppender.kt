package br.iesb.poo.utils

interface ListAppender<T> {
    fun append(arr: List<T>, element: T): List<T>{
        val mutable = arr.toMutableList()
        mutable.add(element)
        return mutable.toList()
    }

}
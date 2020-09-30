package br.iesb.poo.resources.schemas

import org.jetbrains.exposed.sql.ResultRow

interface Schema<T> {

    fun toObject(row: ResultRow): T
}
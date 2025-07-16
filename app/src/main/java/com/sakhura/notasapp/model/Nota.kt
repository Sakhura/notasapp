package com.sakhura.notasapp.model

data class Nota(
    val id: Long,
    var titulo: String,
    var contenido: String,
    val fecha: Long = System.currentTimeMillis()
)
package com.sakhura.notasapp.model

data class Nota(
    val id: Long,
    var titulo: String,
    var contenido: String,
    val fechaCreacion: Long = System.currentTimeMillis()
)
package com.sakhura.notasapp.data

import com.sakhura.notasapp.model.Nota

object NotasManager {
    private val notas = mutableListOf<Nota>()

    fun agregarNota(nota: Nota) {
        notas.add(0, nota)
    }

    fun obtenerNotas(): List<Nota> = notas

    fun eliminarNota(id: Long) {
        notas.removeAll { it.id == id }
    }

    fun actualizarNota(notaActualizada: Nota) {
        val index = notas.indexOfFirst { it.id == notaActualizada.id }
        if (index != -1) {
            notas[index] = notaActualizada
        }
    }

    fun buscarNotas(titulo: String): List<Nota> {
        return notas.filter { it.titulo.contains(titulo, ignoreCase = true) }
    }

    fun obtenerNotaPorId(id: Long): Nota? {
        return notas.find { it.id == id }
    }
}
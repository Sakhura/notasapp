package com.sakhura.notasapp.data

//import com.sakhura.notasapp.adapter.Nota
import com.sakhura.notasapp.model.Nota

object NotasManager {
    // Lista de notas en memoria
    private val notas = mutableListOf(
        Nota(System.currentTimeMillis(), "Nota de ejemplo", "Este es el contenido de prueba.")
    )

    // Obtener todas las notas
    fun obtenerNotas(): List<Nota> = notas

    // Agregar una nueva nota
    fun agregarNota(nota: Nota) {
        notas.add(nota)
    }

    // Eliminar una nota por ID
    fun eliminarNota(id: Long) {
        notas.removeIf { it.id == id }
    }

    // Buscar notas por título (ignorando mayúsculas)
    fun buscarNotas(query: String): List<Nota> {
        return notas.filter { it.titulo.contains(query, ignoreCase = true) }
    }

    // Obtener una nota específica por su ID
    fun obtenerNotaPorId(id: Long): Nota? {
        return notas.find { it.id == id }
    }

    // Actualizar una nota existente
    fun actualizarNota(nuevaNota: Nota) {
        val index = notas.indexOfFirst { it.id == nuevaNota.id }
        if (index != -1) {
            notas[index] = nuevaNota
        }
    }
}

package com.sakhura.notasapp.data

import android.util.Log
import com.sakhura.notasapp.model.Nota

object NotasManager {
    // Lista de notas en memoria
    private val notas = mutableListOf(
        Nota(System.currentTimeMillis(), "Nota de ejemplo", "Este es el contenido de prueba.")
    )

    init {
        Log.d("NotasManager", "NotasManager inicializado con ${notas.size} notas")
    }

    // Obtener todas las notas ordenadas por fecha (más recientes primero)
    fun obtenerNotas(): List<Nota> {
        val resultado = notas.sortedByDescending { it.fechaCreacion }
        Log.d("NotasManager", "obtenerNotas() - Devolviendo ${resultado.size} notas")
        return resultado
    }

    // Agregar una nueva nota
    fun agregarNota(nota: Nota) {
        notas.add(nota)
        Log.d("NotasManager", "agregarNota() - Nota agregada con ID: ${nota.id}. Total notas: ${notas.size}")
    }

    // Eliminar una nota por ID
    fun eliminarNota(id: Long) {
        val sizeBefore = notas.size
        notas.removeIf { it.id == id }
        val sizeAfter = notas.size
        Log.d("NotasManager", "eliminarNota($id) - Notas antes: $sizeBefore, después: $sizeAfter")
    }

    // Buscar notas por título (ignorando mayúsculas)
    fun buscarNotas(query: String): List<Nota> {
        val resultado = notas.filter {
            it.titulo.contains(query, ignoreCase = true) ||
                    it.contenido.contains(query, ignoreCase = true)
        }.sortedByDescending { it.fechaCreacion }

        Log.d("NotasManager", "buscarNotas('$query') - Encontradas ${resultado.size} notas")
        return resultado
    }

    // Obtener una nota específica por su ID
    fun obtenerNotaPorId(id: Long): Nota? {
        val resultado = notas.find { it.id == id }
        Log.d("NotasManager", "obtenerNotaPorId($id) - ${if (resultado != null) "Encontrada" else "NO encontrada"}")
        return resultado
    }

    // Actualizar una nota existente
    fun actualizarNota(nuevaNota: Nota) {
        val index = notas.indexOfFirst { it.id == nuevaNota.id }
        if (index != -1) {
            notas[index] = nuevaNota
            Log.d("NotasManager", "actualizarNota() - Nota actualizada en índice $index con ID: ${nuevaNota.id}")
        } else {
            Log.e("NotasManager", "actualizarNota() - ERROR: No se encontró nota con ID: ${nuevaNota.id}")
        }
    }
}
package com.sakhura.notasapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sakhura.notasapp.data.NotasManager
import com.sakhura.notasapp.databinding.ActivityDetalleNotaBinding
import com.sakhura.notasapp.model.Nota

class DetalleNotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleNotaBinding
    private var nota: Nota? = null
    private var notaOriginalVacia = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("DetalleNota", "onCreate iniciado")

        val idNota = intent.getLongExtra("nota_id", -1)
        Log.d("DetalleNota", "ID recibido: $idNota")

        nota = NotasManager.obtenerNotaPorId(idNota)
        Log.d("DetalleNota", "Nota encontrada: ${nota != null}")

        if (nota != null) {
            // Verificar si la nota está vacía (recién creada)
            notaOriginalVacia = nota!!.titulo.isEmpty() && nota!!.contenido.isEmpty()
            Log.d("DetalleNota", "Nota original vacía: $notaOriginalVacia")
            Log.d("DetalleNota", "Título: '${nota!!.titulo}', Contenido: '${nota!!.contenido}'")

            binding.etTitulo.setText(nota!!.titulo)
            binding.etContenido.setText(nota!!.contenido)
        } else {
            Log.e("DetalleNota", "ERROR: No se encontró la nota con ID $idNota")
            // Si no existe la nota, regresar
            finish()
            return
        }

        // BOTÓN GUARDAR - NUEVO
        binding.btnGuardar.setOnClickListener {
            Log.d("DetalleNota", "Botón guardar presionado")
            guardarNota()

            // Mostrar mensaje de confirmación
            Toast.makeText(this, "Nota guardada", Toast.LENGTH_SHORT).show()

            // Regresar a la pantalla principal
            finish()
        }

        // BOTÓN ELIMINAR
        binding.btnEliminar.setOnClickListener {
            Log.d("DetalleNota", "Botón eliminar presionado")
            nota?.let {
                NotasManager.eliminarNota(it.id)
                Log.d("DetalleNota", "Nota eliminada con ID: ${it.id}")
                Toast.makeText(this, "Nota eliminada", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

        Log.d("DetalleNota", "onCreate completado")
    }

    // FUNCIÓN PARA GUARDAR LA NOTA
    private fun guardarNota() {
        val titulo = binding.etTitulo.text.toString().trim()
        val contenido = binding.etContenido.text.toString().trim()

        Log.d("DetalleNota", "Guardando nota - Título: '$titulo', Contenido: '$contenido'")

        nota?.let { notaActual ->
            if (titulo.isNotEmpty() || contenido.isNotEmpty()) {
                Log.d("DetalleNota", "Hay contenido, actualizando nota")
                // Hay contenido, actualizar la nota
                notaActual.titulo = titulo
                notaActual.contenido = contenido
                NotasManager.actualizarNota(notaActual)
                Log.d("DetalleNota", "Nota actualizada con ID: ${notaActual.id}")
            } else if (notaOriginalVacia) {
                Log.d("DetalleNota", "Nota vacía, eliminando")
                // La nota estaba vacía y sigue vacía, eliminarla
                NotasManager.eliminarNota(notaActual.id)
                Log.d("DetalleNota", "Nota vacía eliminada con ID: ${notaActual.id}")
            }
        }
    }

    // MANTENER LA FUNCIONALIDAD DEL BOTÓN ATRÁS
    override fun onPause() {
        super.onPause()
        Log.d("DetalleNota", "onPause - guardado automático")
        // Solo guardar automáticamente si no se usó el botón guardar
        guardarNota()
        Log.d("DetalleNota", "onPause completado")
    }
}
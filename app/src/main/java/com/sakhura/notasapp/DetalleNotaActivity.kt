package com.sakhura.notasapp

import android.os.Bundle
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

        val idNota = intent.getLongExtra("nota_id", -1)
        nota = NotasManager.obtenerNotaPorId(idNota)

        if (nota != null) {
            // Verificar si la nota está vacía (recién creada)
            notaOriginalVacia = nota!!.titulo.isEmpty() && nota!!.contenido.isEmpty()

            binding.etTitulo.setText(nota!!.titulo)
            binding.etContenido.setText(nota!!.contenido)
        } else {
            // Si no existe la nota, regresar (no debería pasar con la corrección)
            finish()
            return
        }

        binding.btnEliminar.setOnClickListener {
            nota?.let {
                NotasManager.eliminarNota(it.id)
            }
            finish()
        }
    }

    override fun onPause() {
        super.onPause()

        val titulo = binding.etTitulo.text.toString().trim()
        val contenido = binding.etContenido.text.toString().trim()

        nota?.let { notaActual ->
            if (titulo.isNotEmpty() || contenido.isNotEmpty()) {
                // Hay contenido, actualizar la nota
                notaActual.titulo = titulo
                notaActual.contenido = contenido
                NotasManager.actualizarNota(notaActual)
            } else if (notaOriginalVacia) {
                // La nota estaba vacía y sigue vacía, eliminarla
                NotasManager.eliminarNota(notaActual.id)
            }
        }
    }
}
package com.sakhura.notasapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sakhura.notasapp.data.NotasManager
import com.sakhura.notasapp.databinding.ActivityDetalleNotaBinding
import com.sakhura.notasapp.model.Nota

class DetalleNotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleNotaBinding
    private var nota: Nota? = null
    private var esNuevaNota = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idNota = intent.getLongExtra("nota_id", -1)
        nota = NotasManager.obtenerNotaPorId(idNota)

        if (nota == null) {
            // Si no existe, creamos nueva nota (aún no la guardamos)
            nota = Nota(idNota, "", "")
            esNuevaNota = true
        }

        binding.etTitulo.setText(nota!!.titulo)
        binding.etContenido.setText(nota!!.contenido)

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

        if (titulo.isNotEmpty() || contenido.isNotEmpty()) {
            nota?.apply {
                this.titulo = titulo
                this.contenido = contenido
            }

            if (esNuevaNota) {
                NotasManager.agregarNota(nota!!)
            } else {
                NotasManager.actualizarNota(nota!!)
            }
        }
    }
}

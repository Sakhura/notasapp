package com.sakhura.notasapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
// import com.sakhura.notasapp.adapter.Nota
import com.sakhura.notasapp.data.NotasManager
import com.sakhura.notasapp.databinding.ActivityDetalleNotaBinding
import com.sakhura.notasapp.model.Nota

class DetalleNotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetalleNotaBinding
    private var nota: Nota? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idNota = intent.getLongExtra("nota_id", -1)
        nota = NotasManager.obtenerNotaPorId(idNota)

        if (nota == null) {
            Toast.makeText(this, "Nota no encontrada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.etTitulo.setText(nota!!.titulo)
        binding.etContenido.setText(nota!!.contenido)

        binding.btnEliminar.setOnClickListener {
            NotasManager.eliminarNota(nota!!.id)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        nota?.let {
            it.titulo = binding.etTitulo.text.toString()
            it.contenido = binding.etContenido.text.toString()
            NotasManager.actualizarNota(it)
        }
    }
}

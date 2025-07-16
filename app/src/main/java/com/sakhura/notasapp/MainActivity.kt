package com.sakhura.notasapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sakhura.notasapp.adapter.NotasAdapter
import com.sakhura.notasapp.data.NotasManager
import com.sakhura.notasapp.databinding.ActivityMainBinding
import com.sakhura.notasapp.model.Nota

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NotasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("MainActivity", "onCreate iniciado")

        // Inicializa el adapter con lambda de clic
        adapter = NotasAdapter { nota ->
            Log.d("MainActivity", "Clic en nota: ${nota.titulo}")
            val intent = Intent(this, DetalleNotaActivity::class.java)
            intent.putExtra("nota_id", nota.id)
            startActivity(intent)
        }

        // Configura RecyclerView
        binding.rvNotas.layoutManager = LinearLayoutManager(this)
        binding.rvNotas.adapter = adapter

        // Botón flotante para crear nueva nota - CON DEBUG
        binding.fabAgregarNota.setOnClickListener {
            Log.d("MainActivity", "FAB presionado - Creando nueva nota")

            val nuevaNotaId = System.currentTimeMillis()
            val nuevaNota = Nota(nuevaNotaId, "", "")

            Log.d("MainActivity", "Nota creada con ID: $nuevaNotaId")

            // Guardar la nota temporal en el manager
            NotasManager.agregarNota(nuevaNota)

            Log.d("MainActivity", "Nota agregada al manager. Total notas: ${NotasManager.obtenerNotas().size}")

            val intent = Intent(this, DetalleNotaActivity::class.java)
            intent.putExtra("nota_id", nuevaNota.id)

            Log.d("MainActivity", "Iniciando DetalleNotaActivity con ID: $nuevaNotaId")
            startActivity(intent)
        }

        // Configura búsqueda de notas por título
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filtradas = NotasManager.buscarNotas(newText.orEmpty())
                adapter.actualizarNotas(filtradas)
                return true
            }
        })

        Log.d("MainActivity", "onCreate completado")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume - Actualizando lista")

        val notas = NotasManager.obtenerNotas()
        Log.d("MainActivity", "Notas encontradas: ${notas.size}")

        notas.forEach { nota ->
            Log.d("MainActivity", "Nota: ID=${nota.id}, Titulo='${nota.titulo}', Contenido='${nota.contenido}'")
        }

        // Refresca la lista al volver del detalle
        adapter.actualizarNotas(notas)
    }
}
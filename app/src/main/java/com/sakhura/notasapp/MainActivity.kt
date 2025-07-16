package com.sakhura.notasapp

import android.content.Intent
import android.os.Bundle
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

        adapter = NotasAdapter(NotasManager.obtenerNotas()) { nota ->
            val intent = Intent(this, DetalleNotaActivity::class.java)
            intent.putExtra("nota_id", nota.id)
            startActivity(intent)
        }

        binding.rvNotas.layoutManager = LinearLayoutManager(this)
        binding.rvNotas.adapter = adapter

        binding.fabAgregarNota.setOnClickListener {
            val nuevaNota = Nota(System.currentTimeMillis(), "", "")
            NotasManager.agregarNota(nuevaNota)
            val intent = Intent(this, DetalleNotaActivity::class.java)
            intent.putExtra("nota_id", nuevaNota.id)
            startActivity(intent)
        }

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val filtradas = NotasManager.buscarNotas(newText.orEmpty())
                adapter.actualizarNotas(filtradas)
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        adapter.actualizarNotas(NotasManager.obtenerNotas())
    }
}

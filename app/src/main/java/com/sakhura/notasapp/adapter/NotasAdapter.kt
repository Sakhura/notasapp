package com.sakhura.notasapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sakhura.notasapp.R
import com.sakhura.notasapp.model.Nota
import java.text.SimpleDateFormat
import java.util.*

class NotasAdapter(
    private var lista: List<Nota>,
    private val onItemClick: (Nota) -> Unit
) : RecyclerView.Adapter<NotasAdapter.NotaViewHolder>() {

    inner class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTituloNota)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFechaNota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nota, parent, false)
        return NotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val nota = lista[position]
        holder.tvTitulo.text = nota.titulo.ifEmpty { "Sin título" }
        holder.tvFecha.text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            .format(Date(nota.fecha))

        holder.itemView.setOnClickListener { onItemClick(nota) }
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarNotas(nuevas: List<Nota>) {
        lista = nuevas
        notifyDataSetChanged()
    }
}
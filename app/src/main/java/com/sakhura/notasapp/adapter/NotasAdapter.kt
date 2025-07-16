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
    private val onNotaClick: (Nota) -> Unit
) : RecyclerView.Adapter<NotasAdapter.NotaViewHolder>() {

    private val notas = mutableListOf<Nota>()

    fun actualizarNotas(nuevasNotas: List<Nota>) {
        notas.clear()
        notas.addAll(nuevasNotas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nota, parent, false)
        return NotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        holder.bind(notas[position])
    }

    override fun getItemCount(): Int = notas.size

    inner class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(nota: Nota) {
            val tvTitulo = itemView.findViewById<TextView>(R.id.tvTitulo)
            val tvFecha = itemView.findViewById<TextView>(R.id.tvFecha)

            tvTitulo.text = nota.titulo
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            tvFecha.text = sdf.format(Date(nota.id))

            itemView.setOnClickListener {
                onNotaClick(nota)
            }
        }
    }
}

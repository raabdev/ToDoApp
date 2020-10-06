package com.example.todoapp

import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.tarea_item.view.*

class TareaAdapter(private val tareas: ArrayList<Tarea> = ArrayList(), private val listener: TareaAdapterListener) : RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    class TareaViewHolder(view : View) : RecyclerView.ViewHolder(view)

    fun agregarTarea(tarea: Tarea) {
        tareas.add(tarea)
        notifyItemInserted(itemCount)
    }

    fun getTarea(posicion: Int) : Tarea {
        return tareas[posicion]
    }

    fun eliminarTarea(posicion: Int) {
        tareas.removeAt(posicion)
        notifyItemRemoved(posicion)
    }

    fun restaurarTarea(posicion: Int, tarea: Tarea) {
        tareas.add(posicion, tarea)
        notifyItemInserted(posicion)
    }

    fun cambiarPosicionItem(posicionInicial: Int, posicionFinal: Int) {
        val tarea = tareas[posicionInicial]
        tareas.removeAt(posicionInicial)
        tareas.add(posicionFinal, tarea)
        notifyItemMoved(posicionInicial, posicionFinal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tarea_item, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        holder.itemView.nombre.text = tareas[position].nombre
        holder.itemView.tarea_terminada.isChecked = tareas[position].terminada
        if(holder.itemView.tarea_terminada.isChecked){
            val texto = holder.itemView.nombre.text
            val textoSpan = SpannableString(texto)
            textoSpan.setSpan(StrikethroughSpan(), 0, textoSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            holder.itemView.nombre.text = textoSpan
        } else {}

        holder.itemView.tarea_terminada.setOnCheckedChangeListener { buttonView, isChecked ->
            listener.onTareaChecked(tareas[position], isChecked)
            if(isChecked){
                val texto = holder.itemView.nombre.text
                val textoSpan = SpannableString(texto)
                textoSpan.setSpan(StrikethroughSpan(), 0, textoSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                holder.itemView.nombre.text = textoSpan
            } else {

            }

        }
    }

    override fun getItemCount(): Int {
        return tareas.size
    }

interface TareaAdapterListener {
    fun onTareaChecked(tarea: Tarea, terminada: Boolean)
}

}
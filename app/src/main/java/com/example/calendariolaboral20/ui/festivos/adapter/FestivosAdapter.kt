package com.example.calendariolaboral20.ui.festivos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.data.models.DatosFestivos

class FestivosAdapter(
    var listaFestivos: List<DatosFestivos>,
    val onClickLambda: (DatosFestivos) -> Unit
): RecyclerView.Adapter<FestivosViewHolder>() {

    fun funRefrescaLista(listaNueva: List<DatosFestivos>) {
        listaFestivos = listaNueva
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FestivosViewHolder {
        return FestivosViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_festivos, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: FestivosViewHolder,
        position: Int
    ) {
        holder.render(
            listaFestivos[position],
            onClickLambda
        )
    }

    override fun getItemCount(): Int = listaFestivos.size
}
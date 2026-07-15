package com.example.calendariolaboral20.ui.resvacas.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.data.models.DatosVacaciones

class ResVacasAdapter(
    var listaVacaciones: List<DatosVacaciones>,
    val onClickLambda: (DatosVacaciones) -> Unit
): RecyclerView.Adapter<ResVacasViewHolder>() {

    fun refrescaLista(listaNew: List<DatosVacaciones>){
        listaVacaciones = listaNew
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ResVacasViewHolder {
        return ResVacasViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_res_vacas, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ResVacasViewHolder,
        position: Int
    ) {
        holder.render(
            listaVacaciones[position],
            onClickLambda
        )
    }

    override fun getItemCount(): Int = listaVacaciones.size
}
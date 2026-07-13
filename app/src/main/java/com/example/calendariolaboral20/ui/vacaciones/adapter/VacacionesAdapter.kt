package com.example.calendariolaboral20.ui.vacaciones.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.data.models.DatosVacaciones

class VacacionesAdapter(
    val listaVacaciones: List<DatosVacaciones>,
    val onClickLambda: (DatosVacaciones) -> Unit
): RecyclerView.Adapter<VacacionesViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VacacionesViewHolder {
        return VacacionesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_vacaciones, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: VacacionesViewHolder,
        position: Int
    ) {
        holder.render(
            listaVacaciones[position],
            onClickLambda
        )
    }

    override fun getItemCount(): Int = listaVacaciones.size
}
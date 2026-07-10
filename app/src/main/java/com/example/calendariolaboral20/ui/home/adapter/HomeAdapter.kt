package com.example.calendariolaboral20.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.data.models.DatosMenuPrincipal

class HomeAdapter(
    val listaMenuPrincipal: List<DatosMenuPrincipal>,
    val onClickLambda: (DatosMenuPrincipal) -> Unit
): RecyclerView.Adapter<HomeViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_menu_principal, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: HomeViewHolder,
        position: Int
    ) {
        holder.render(listaMenuPrincipal[position], onClickLambda)
    }

    override fun getItemCount(): Int = listaMenuPrincipal.size
}
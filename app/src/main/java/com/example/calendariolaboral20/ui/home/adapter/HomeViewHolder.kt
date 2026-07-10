package com.example.calendariolaboral20.ui.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral20.data.models.DatosMenuPrincipal
import com.example.calendariolaboral20.databinding.ItemMenuPrincipalBinding

class HomeViewHolder(view: View): RecyclerView.ViewHolder(view) {

    var binding = ItemMenuPrincipalBinding.bind(view)

    fun render(
        datoMenuPrincipal: DatosMenuPrincipal,
        onClickLambda: (DatosMenuPrincipal) -> Unit
    ){

        binding.ivImagen.setImageResource(datoMenuPrincipal.iImagen)
        binding.tvTitulo.text = datoMenuPrincipal.strTitulo
        binding.tvDescripcion.text = datoMenuPrincipal.strDescripcion

        //
        // Si se pulsa un item del recyclerview
        //
        itemView.setOnClickListener {
            onClickLambda(datoMenuPrincipal)
        }

    }
}
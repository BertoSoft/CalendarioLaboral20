package com.example.calendariolaboral20.ui.vacaciones.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral20.data.models.DatosVacaciones
import com.example.calendariolaboral20.databinding.ItemVacacionesBinding
import com.example.calendariolaboral20.domain.FuncAux
import com.example.calendariolaboral20.domain.FuncVacaciones

class VacacionesViewHolder(view: View): RecyclerView.ViewHolder(view) {

    var binding = ItemVacacionesBinding.bind(view)

    fun render(
        datoVacaciones: DatosVacaciones,
        onClickLambda: (DatosVacaciones) -> Unit
    ) {
        val strFecha1 = FuncAux().strFechaLargaFromFechaCorta(
            datoVacaciones.strFecha1
        )

        val strFecha2 = FuncAux().strFechaLargaFromFechaCorta(
            datoVacaciones.strFecha2
        )

        val iDias = FuncVacaciones().getDiasLaborables(itemView.context, datoVacaciones)
        var str = ""
        if( iDias == 1){
            str = "1 día laborable."
        }
        else{
            str = "${iDias.toString()} días laborables."
        }

        binding.tvFecha12.text = strFecha1
        binding.tvFecha22.text = strFecha2
        binding.tvFecha32.text = str

        //
        // Metodo setOnClickListener de Rv de Vacaciones
        //
        itemView.setOnClickListener {
            onClickLambda(datoVacaciones)
        }

    }
}
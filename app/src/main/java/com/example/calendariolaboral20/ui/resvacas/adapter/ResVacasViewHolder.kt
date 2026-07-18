package com.example.calendariolaboral20.ui.resvacas.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral20.data.models.DatosVacaciones
import com.example.calendariolaboral20.databinding.ItemResVacasBinding
import com.example.calendariolaboral20.domain.FuncAux
import com.example.calendariolaboral20.domain.FuncFestivos
import com.example.calendariolaboral20.domain.FuncVacaciones

class ResVacasViewHolder(view: View): RecyclerView.ViewHolder(view) {

    var str = ""
    var binding = ItemResVacasBinding.bind(view)

    fun render(
        datoVacaciones: DatosVacaciones,
        onClickLambda: (DatosVacaciones) -> Unit
    ){

        //
        // Si solo es un día lo ponemos en formato fecha larga
        //
        if (datoVacaciones.strFecha1 == datoVacaciones.strFecha2) {
            str = FuncAux().strFechaLargaFromFechaCorta(datoVacaciones.strFecha1)
            str += " ---> 1 Día     "
            binding.tvTexto.text = str
        }

        //
        // Si es mas de un día ponemos la fecha inicial y final en formato corto
        //
        else {
            val iDiasLaborables = FuncVacaciones().getDiasLaborables(itemView.context, datoVacaciones)

            str = "Del "
            str += datoVacaciones.strFecha1
            str += " al "
            str += datoVacaciones.strFecha2
            str += " --->  "
            str += iDiasLaborables
            str += "Días  "
            binding.tvTexto.text = str
        }

        //
        // Metodo setOnClickListener de Rv de Vacaciones
        //
        itemView.setOnClickListener {
            onClickLambda(datoVacaciones)
        }
    }
}
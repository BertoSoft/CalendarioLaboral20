package com.example.calendariolaboral20.ui.festivos.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.data.models.DatosFestivos
import com.example.calendariolaboral20.databinding.ItemFestivosBinding
import com.example.calendariolaboral20.domain.FuncAux

class FestivosViewHolder(view: View): RecyclerView.ViewHolder(view) {

    var binding = ItemFestivosBinding.bind(view)

    fun render(
        datoFestivos: DatosFestivos,
        onClickLambda: (DatosFestivos) -> Unit
    ){
        val strFecha: String = " " + FuncAux().strFechaLargaFromFechaCorta(datoFestivos.strDia)
        val strTipoFestivo: String = datoFestivos.strTipo + " "

        binding.tvFecha.text = strFecha
        binding.tvTipoFestivo.text = strTipoFestivo

        when (datoFestivos.strTipo) {
            "Nacional" -> {
                binding.tvTipoFestivo.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.azul_claro
                    )
                )
                binding.tvFecha.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.azul_claro
                    )
                )
            }

            "Autonomico" -> {
                binding.tvTipoFestivo.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.azul_oscuro
                    )
                )

                binding.tvFecha.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.azul_oscuro
                    )
                )
            }

            "Local" -> {
                binding.tvTipoFestivo.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.amarillo_oscuro
                    )
                )

                binding.tvFecha.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.amarillo_oscuro
                    )
                )
            }

            "Convenio" -> {
                binding.tvTipoFestivo.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.rojo
                    )
                )

                binding.tvFecha.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.rojo
                    )
                )
            }

            "Exceso de Jornada" -> {
                binding.tvTipoFestivo.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.violeta
                    )
                )

                binding.tvFecha.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.violeta
                    )
                )
            }

            "Vacaciones" -> {
                binding.tvTipoFestivo.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.gris_oscuro
                    )
                )

                binding.tvFecha.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.gris_oscuro
                    )
                )
            }
        }

        //
        // Metodo setOnClickListener de rvFestivos
        //
        itemView.setOnClickListener {
            onClickLambda(datoFestivos)
        }
    }

}
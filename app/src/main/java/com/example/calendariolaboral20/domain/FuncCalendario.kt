package com.example.calendariolaboral20.domain

import android.content.Context
import com.example.calendariolaboral20.R

class FuncCalendario {

    fun getBackColorDia(miContexto: Context, strFecha: String): Int {
        var iColor = R.color.transparente
        val strAno = strFecha.substring(6, strFecha.length)

        //
        // Comprobamos las vacaciones
        //
        val listaVacaciones = FuncVacaciones().getListaVacacionesAnuales(miContexto, strAno)
        val calFecha = FuncAux().calFechaFromstrFechaCorta(strFecha)

        var i = 0
        while (i < listaVacaciones.size){
            val calFecha1 = FuncAux().calFechaFromstrFechaCorta(listaVacaciones[i].strFecha1)
            val calFecha2 = FuncAux().calFechaFromstrFechaCorta(listaVacaciones[i].strFecha2)

            if(calFecha in calFecha1 .. calFecha2){

                //
                // Si la fecha esta dentro de las vacaciones y no es sabado ni domingo, color gris
                //
                val str = FuncAux().strFechaLargaFromFechaCorta(strFecha).substring(0, 4)
                if(
                    str != "Satu" &&
                    str != "sába" &&
                    str != "domi" &&
                    str != "Sund"
                ){
                    iColor = R.color.gris_oscuro
                }
            }
            i++
        }
        //
        // Comprobamos los festivos
        //
        val listaFestivos = FuncFestivos().getListaFestivosAndVacacionesAnuales(miContexto, strAno)
        i = 0
        while (i < listaFestivos.size) {
            if(listaFestivos[i].strDia == strFecha){
                when (listaFestivos[i].strTipo) {
                    "Nacionales"          -> iColor = R.color.azul_claro
                    "Autonomico"        -> iColor = R.color.azul_oscuro
                    "Local"             -> iColor = R.color.amarillo_oscuro
                    "Convenio"          -> iColor = R.color.rojo
                    "Exceso de Jornada" -> iColor = R.color.violeta
                    "Vacaciones"        -> iColor = R.color.gris_oscuro
                }
                i = listaFestivos.size
            }
            i++
        }

        return iColor
    }

    fun getForeColorDia(strFecha: String): Int {
        var iColor = R.color.white
        val strFechaLarga = FuncAux().strFechaLargaFromFechaCorta(strFecha)
        val str = strFechaLarga.substring(0,4)

        when (str) {
            "Mond" -> iColor = R.color.black
            "lune" -> iColor = R.color.black
            "Tues" -> iColor = R.color.black
            "mart" -> iColor = R.color.black
            "Wedn" -> iColor = R.color.black
            "miér" -> iColor = R.color.black
            "Thur" -> iColor = R.color.black
            "juev" -> iColor = R.color.black
            "Frid" -> iColor = R.color.black
            "vier" -> iColor = R.color.black
            "Satu" -> iColor = R.color.rojo
            "sába" -> iColor = R.color.rojo
            "Sund" -> iColor = R.color.rojo
            "domi" -> iColor = R.color.rojo
        }

        return iColor
    }


}
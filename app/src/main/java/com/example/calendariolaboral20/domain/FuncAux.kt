package com.example.calendariolaboral20.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FuncAux {

    fun calFechaFromstrFechaCorta(strFechaCorta: String): Calendar{

        //
        // Esta funcion si recibe strFecha vacio devuelve 01/01/1900
        //
        val calFecha = Calendar.getInstance()

        //
        // Si la strFecha no esta vacia
        //
        if (strFechaCorta != "") {
            val iAno = strFechaCorta.substring(6, 10).toInt()
            var iMes = strFechaCorta.substring(3, 5).toInt()
            val iDia = strFechaCorta.substring(0, 2).toInt()

            iMes--
            calFecha.set(iAno, iMes, iDia)
        }

        //
        // strFechaCorta esta vacia, o es nula, ponemos 1 de enero de 1900
        //
        else {
            calFecha.set(1900, 0, 1)
        }

        return calFecha
    }

    fun strFechaLargaFromCalendar(fecha: Calendar): String {
        val sdfLarga = SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", Locale.getDefault())
        return sdfLarga.format(fecha.time)
    }

    fun strFechaLargaFromFechaCorta(strFechaCorta: String): String {
        val calFecha = Calendar.getInstance()

        val iDia = strFechaCorta.substring(0, 2).toInt()
        val iMes = strFechaCorta.substring(3, 5).toInt() - 1
        val iAno = strFechaCorta.substring(6, 10).toInt()
        calFecha.set(iAno, iMes, iDia)
        val strFechaLarga = strFechaLargaFromCalendar(calFecha)

        return strFechaLarga
    }

    fun strFechaCortaToCalendar(calFecha: Calendar): String?{
        val sdfCorta = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return  sdfCorta.format(calFecha.time)
    }

    fun strHoraToCalendar(calFecha: Calendar): String?{
        val sdfCorta = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return  sdfCorta.format(calFecha.time)
    }
}
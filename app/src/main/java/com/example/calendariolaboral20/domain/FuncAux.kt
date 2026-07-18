package com.example.calendariolaboral20.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FuncAux {

    fun calFechaFromstrFechaCorta(strFechaCorta: String):Calendar{

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

        return strFechaLargaFromCalendar(calFecha)
    }

    fun strFechaCortaToCalendar(calFecha: Calendar): String{
        val sdfCorta = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return  sdfCorta.format(calFecha.time)
    }

    fun strHoraToCalendar(calFecha: Calendar): String{
        val sdfCorta = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return  sdfCorta.format(calFecha.time)
    }

    fun iMesTostrMes(strMes: String): Int {
        var iMes = -1
        when (strMes) {
            "Enero" -> iMes = 1
            "Febrero" -> iMes = 2
            "Marzo" -> iMes = 3
            "Abril" -> iMes = 4
            "Mayo" -> iMes = 5
            "Junio" -> iMes = 6
            "Julio" -> iMes = 7
            "Agosto" -> iMes = 8
            "Septiembre" -> iMes = 9
            "Octubre" -> iMes = 10
            "Noviembre" -> iMes = 11
            "Diciembre" -> iMes = 12
        }

        return iMes
    }

    fun intDiasTo2Cals(strFecha2: String, strFecha1: String): Int {
        var iDias = 0
        val calFecha1 = calFechaFromstrFechaCorta(strFecha1)
        val calFecha2 = calFechaFromstrFechaCorta(strFecha2)

        iDias = ((calFecha2.timeInMillis - calFecha1.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
        iDias++

        return iDias
    }

}
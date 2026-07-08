package com.example.calendariolaboral20.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FuncAux {

    fun strFechaCortaToCalendar(calFecha: Calendar): String?{
        val sdfCorta = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return  sdfCorta.format(calFecha.time)
    }

    fun strHoraToCalendar(calFecha: Calendar): String?{
        val sdfCorta = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return  sdfCorta.format(calFecha.time)
    }
}
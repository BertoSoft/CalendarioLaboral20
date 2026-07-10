package com.example.calendariolaboral20.domain

import android.content.Context
import com.example.calendariolaboral20.data.models.DatosFestivos
import kotlin.coroutines.Continuation

class FuncFestivos {

    fun getDatoFestivos(id: Int): DatosFestivos {
        var datoFestivos = DatosFestivos("", "")

        return datoFestivos
    }

    fun getIdFestivosByDato(miContexto: Context, miDato: DatosFestivos): Int{
        var id = -1

        return id
    }

    fun setFestivo(miContexto: Context, miDato: DatosFestivos): Boolean{

        return false
    }

}
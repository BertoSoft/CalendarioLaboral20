package com.example.calendariolaboral20.domain

import android.content.Context
import java.util.Calendar

class FuncExcesoJornada {
    fun getSabados(strAno: String): Int {
        var iSabados = 0
        val calFecha = Calendar.getInstance()

        calFecha.set(strAno.toInt(), 0, 1)
        while (calFecha.get(Calendar.YEAR) == strAno.toInt()){
            val str = FuncAux().strFechaLargaFromCalendar(calFecha).substring(0,4)

            if( str == "sába" || str == "Satu"){
                iSabados++
            }

            calFecha.add(Calendar.DAY_OF_MONTH, 1)
        }
        return iSabados
    }

    fun getDomingos(strAno: String): Int {
        var iDomingos = 0
        val calFecha = Calendar.getInstance()

        calFecha.set(strAno.toInt(), 0, 1)
        while (calFecha.get(Calendar.YEAR) == strAno.toInt()){
            val str = FuncAux().strFechaLargaFromCalendar(calFecha).substring(0,4)

            if( str == "domi" || str == "Sund"){
                iDomingos++
            }

            calFecha.add(Calendar.DAY_OF_MONTH, 1)
        }
        return iDomingos
    }

    fun getFestivosNacionales(miContexto: Context, strAno: String): Int {
        var iFestivosNacionales = 0
        val listadoFestivos = FuncFestivos().getListaFestivosAnuales(
            miContexto,
            strAno
        )
        var iContador = 0
        while (iContador < listadoFestivos.size){
            if(listadoFestivos[iContador].strTipo == "Nacional"){
                iFestivosNacionales++
            }
            iContador++
        }

        return iFestivosNacionales
    }

    fun getFestivosAutonomicos(miContexto: Context, strAno: String): Int {
        var iFestivosAutonomicos = 0
        val listadoFestivos = FuncFestivos().getListaFestivosAnuales(
            miContexto,
            strAno
        )
        var iContador = 0
        while (iContador < listadoFestivos.size){
            if(listadoFestivos[iContador].strTipo == "Autonomico"){
                iFestivosAutonomicos++
            }
            iContador++
        }

        return iFestivosAutonomicos
    }

    fun getFestivosLocales(miContexto: Context, strAno: String): Int {
        var iFestivosLocales = 0
        val listadoFestivos = FuncFestivos().getListaFestivosAnuales(
            miContexto,
            strAno
        )
        var iContador = 0
        while (iContador < listadoFestivos.size){
            if(listadoFestivos[iContador].strTipo == "Local"){
                iFestivosLocales++
            }
            iContador++
        }

        return iFestivosLocales
    }
}
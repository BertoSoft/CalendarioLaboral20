package com.example.calendariolaboral20.domain

import android.content.Context
import com.example.calendariolaboral20.data.databases.AdminDb
import com.example.calendariolaboral20.data.models.DatosFestivos

class FuncFestivos {

    fun getDatoFestivos(id: Int): DatosFestivos {
        var datoFestivos = DatosFestivos("", "")

        return datoFestivos
    }

    fun getIdFestivosByDato(miContexto: Context, miDato: DatosFestivos): Int{
        var id = -1
        val adminDb = AdminDb(miContexto, null)
        val sqlRead = adminDb.readableDatabase
        val cFestivos = sqlRead.rawQuery("SELECT *FROM Festivos", null)
        var i = 0

        if(cFestivos.moveToFirst()){
            while (!cFestivos.isAfterLast){
                val colId = cFestivos.getColumnIndex("_id")
                val colDia = cFestivos.getColumnIndex("Dia")
                val colTipo = cFestivos.getColumnIndex("Tipo")
                val strDiaDb = cFestivos.getString(colDia)
                val strTipoDb = cFestivos.getString(colTipo)

                if(miDato.strDia == strDiaDb && miDato.strTipo == strTipoDb){
                    id = cFestivos.getInt(colId)
                    cFestivos.moveToLast()
                }
                cFestivos.moveToNext()
            }
        }

        cFestivos.close()
        sqlRead.close()
        adminDb.close()

        return id
    }

    fun setFestivo(miContexto: Context, miDato: DatosFestivos): Boolean{

        return false
    }

}
package com.example.calendariolaboral20.domain

import android.content.Context
import com.example.calendariolaboral20.data.databases.AdminDb
import com.example.calendariolaboral20.data.models.DatosCalendarFestivos
import com.example.calendariolaboral20.data.models.DatosFestivos
import com.example.calendariolaboral20.data.models.DatosVacaciones
import com.example.calendariolaboral20.ui.festivos.Festivos

class FuncFestivos {

    fun ordenarListaFestivos(listaFestivos: List<DatosFestivos>): List<DatosFestivos>{
        val listaFestivosOrdenada = mutableListOf<DatosFestivos>()
        val listaCalendar = mutableListOf<DatosCalendarFestivos>()

        //
        // Creamos la lista Calendar
        //
        var i = 0
        while (i < listaFestivos.size){
            listaCalendar.add(
                DatosCalendarFestivos(
                    FuncAux().calFechaFromstrFechaCorta(listaFestivos[i].strDia),
                    listaFestivos[i].strDia,
                    listaFestivos[i].strTipo
                )
            )
            i++
        }

        //
        // Ordenamos la lista Calendar
        //
        val listaCalendarOrdenada = listaCalendar.sortedBy { it.calFecha }

        //
        // Creamos la lista Festivos Ordenada
        //
        i = 0
        while (i < listaCalendarOrdenada.size){
            listaFestivosOrdenada.add(
                DatosFestivos(
                    listaCalendarOrdenada[i].strFecha,
                    listaCalendarOrdenada[i].strTipo
                )
            )
            i++
        }

        return  listaFestivosOrdenada
    }

    fun getDatoFestivosById(miContexto: Context, id: Int): DatosFestivos {
        val adminDb = AdminDb(miContexto, null)
        val sqlRead = adminDb.readableDatabase
        val cFestivos = sqlRead.rawQuery("SELECT *FROM Festivos", null)
        val colId = cFestivos.getColumnIndex("_id")
        val colDia = cFestivos.getColumnIndex("Dia")
        val colTipo = cFestivos.getColumnIndex("Tipo")
        val datoFestivos = DatosFestivos("", "")

        if (cFestivos.moveToFirst()){
            while (!cFestivos.isAfterLast){
                if(id == cFestivos.getInt(colId)){
                    datoFestivos.strDia = cFestivos.getString(colDia)
                    datoFestivos.strTipo = cFestivos.getString(colTipo)
                    cFestivos.moveToLast()
                }
                cFestivos.moveToNext()
            }
        }

        cFestivos.close()
        sqlRead.close()
        adminDb.close()

        return datoFestivos
    }

    fun getIdFestivosByDato(miContexto: Context, miDato: DatosFestivos): Int{
        var id = -1
        val adminDb = AdminDb(miContexto, null)
        val sqlRead = adminDb.readableDatabase
        val cFestivos = sqlRead.rawQuery("SELECT *FROM Festivos", null)

        if(cFestivos.moveToFirst())
            while (!cFestivos.isAfterLast){
                val colId = cFestivos.getColumnIndex("_id")
                val colDia = cFestivos.getColumnIndex("Dia")
                val strDiaDb = cFestivos.getString(colDia)

                if(miDato.strDia == strDiaDb){
                    id = cFestivos.getInt(colId)
                    cFestivos.moveToLast()
                }
                cFestivos.moveToNext()
            }

        cFestivos.close()
        sqlRead.close()
        adminDb.close()

        return id
    }

    fun getListaFestivosAnuales(miContexto: Context, strAno: String): List<DatosFestivos>{
        val listaFestivosAno = mutableListOf<DatosFestivos>()
        val adminDb = AdminDb(miContexto, null)
        val sqlReadDb = adminDb.readableDatabase
        val cFestivos = sqlReadDb.rawQuery("SELECT *FROM Festivos", null)
        val iColDia = cFestivos.getColumnIndex("Dia")
        val iColTipo = cFestivos.getColumnIndex("Tipo")

        if (cFestivos.moveToFirst()) {
            while (!cFestivos.isAfterLast) {
                val strDiaDb = cFestivos.getString(iColDia)
                val strTipoDb = cFestivos.getString(iColTipo)
                val strAnoDb = strDiaDb.substring(6)

                if (strAnoDb == strAno) {
                    listaFestivosAno.add(DatosFestivos(strDiaDb, strTipoDb))
                }

                cFestivos.moveToNext()
            }
        }
        cFestivos.close()
        sqlReadDb.close()
        adminDb.close()

        //
        // Ordenamos la lista
        //
        val listaFestivosAnoOrdenada = ordenarListaFestivos(listaFestivosAno)

        return listaFestivosAnoOrdenada
    }

    fun getListaFestivosAndVacacionesAnuales(miContexto: Context, strAno: String): List<DatosFestivos>{
        var listaFestivos = getListaFestivosAnuales(miContexto, strAno)
        val listaVacaciones = FuncVacaciones().getListaVacacionesAnuales(miContexto, strAno)

        var i = 0
        while (i < listaVacaciones.size){
            if(listaVacaciones[i].strFecha1 == listaVacaciones[i].strFecha2){
                listaFestivos += DatosFestivos(listaVacaciones[i].strFecha1, "Vacaciones")
            }

            i++
        }

        val listaOrdenada = ordenarListaFestivos(listaFestivos)

        return listaOrdenada
    }

    fun setDatoFestivo(miContexto: Context, miDato: DatosFestivos): Boolean{
        val adminDb = AdminDb(miContexto, null)
        val sqlWrite = adminDb.writableDatabase
        val id = getIdFestivosByDato(miContexto, miDato)
        var strSql: String? = null

        //
        // si id < 0 datos no existe, si existe id > 0
        //
        if(id < 0){
            strSql = "INSERT INTO Festivos (Dia, " +
                    "Tipo) VALUES ('${miDato.strDia }'," +
                    "'${miDato.strTipo}');"
        }

        //
        // El Registro de ese año ya existe
        //
        else{
            strSql = "UPDATE Festivos SET Dia = '" +
                    "${miDato.strDia}', Tipo = '" +
                    "${miDato.strTipo}' WHERE _id = ${id};"
        }

        if (strSql != null) {
            sqlWrite.execSQL(strSql)
            sqlWrite.close()
            adminDb.close()
            return true
        }

        sqlWrite.close()
        adminDb.close()
        return false
    }

    fun delDatoFestivo(miContexto: Context, miDato: DatosFestivos): Boolean{
        val id = getIdFestivosByDato(miContexto, miDato)
        val adminDb = AdminDb(miContexto, null)
        val sqlWrite = adminDb.writableDatabase

        if(id < 0){
            sqlWrite.close()
            adminDb.close()
            return false
        }
        else{
            val strSql = "DELETE FROM Festivos WHERE _id = $id"

            sqlWrite.execSQL(strSql)
            sqlWrite.close()
            adminDb.close()
        }
        return  true
    }

}
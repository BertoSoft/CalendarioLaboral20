package com.example.calendariolaboral20.domain

import android.content.Context
import android.icu.util.Calendar
import androidx.annotation.IdRes
import com.example.calendariolaboral20.data.databases.AdminDb
import com.example.calendariolaboral20.data.models.DatosCalendarVacaciones
import com.example.calendariolaboral20.data.models.DatosVacaciones
import com.example.calendariolaboral20.ui.resvacas.ResVacas

class FuncVacaciones {

    fun getIdVacacionesByDato(miContexto: Context, miDato: DatosVacaciones): Int {
        var id = -1
        val adminDb = AdminDb(miContexto, null)
        val sqlRead = adminDb.readableDatabase
        val cVacaciones = sqlRead.rawQuery("SELECT *FROM Vacaciones", null)
        val colId = cVacaciones.getColumnIndex("_id")
        val iColFecha1 = cVacaciones.getColumnIndex("Fecha1")
        val iColFecha2 = cVacaciones.getColumnIndex("Fecha2")
        val calFecha1Dato = FuncAux().calFechaFromstrFechaCorta(miDato.strFecha1)
        val calFecha2Dato = FuncAux().calFechaFromstrFechaCorta(miDato.strFecha2)

        if (cVacaciones.moveToFirst()) {
            while (!cVacaciones.isAfterLast) {
                val calFecha1Db =
                    FuncAux().calFechaFromstrFechaCorta(cVacaciones.getString(iColFecha1))
                val calFecha2Db =
                    FuncAux().calFechaFromstrFechaCorta(cVacaciones.getString(iColFecha2))

                var existe = false
                if (calFecha1Dato in calFecha1Db..calFecha2Db) existe = true
                if (calFecha2Dato in calFecha1Db..calFecha2Db) existe = true
                if (miDato.strFecha1 == cVacaciones.getString(iColFecha1)) existe = true
                if (miDato.strFecha2 == cVacaciones.getString(iColFecha2)) existe = true

                if(existe) {
                    id = cVacaciones.getInt(colId)
                    cVacaciones.close()
                    sqlRead.close()
                    adminDb.close()
                    return id
                }
                cVacaciones.moveToNext()
            }
        }
        cVacaciones.close()
        sqlRead.close()
        adminDb.close()
        return id
    }

    fun getDatoVacacionesById(miContexto: Context, id: Int): DatosVacaciones {
        val adminDb = AdminDb(miContexto, null)
        val sqlRead = adminDb.readableDatabase
        val cVacaciones = sqlRead.rawQuery("SELECT *FROM Vacaciones", null)
        val colId = cVacaciones.getColumnIndex("_id")
        val colFecha1 = cVacaciones.getColumnIndex("Fecha1")
        val colFecha2 = cVacaciones.getColumnIndex("Fecha2")
        val datoVacaciones = DatosVacaciones("", "")

        if (cVacaciones.moveToFirst()){
            while (!cVacaciones.isAfterLast){
                if(id == cVacaciones.getInt(colId)){
                    datoVacaciones.strFecha1 = cVacaciones.getString(colFecha1)
                    datoVacaciones.strFecha2 = cVacaciones.getString(colFecha2)
                    cVacaciones.moveToLast()
                }
                cVacaciones.moveToNext()
            }
        }

        cVacaciones.close()
        sqlRead.close()
        adminDb.close()

        return datoVacaciones
    }

    fun getListaVacacionesAnuales(miContexto: Context, strAno: String): List<DatosVacaciones> {
        val listaVacacionesAno = mutableListOf<DatosVacaciones>()
        val adminDb = AdminDb(miContexto, null)
        val sqlReadDb = adminDb.readableDatabase
        val cVacaciones = sqlReadDb.rawQuery("SELECT *FROM Vacaciones", null)
        val iColFecha1 = cVacaciones.getColumnIndex("Fecha1")
        val iColFecha2 = cVacaciones.getColumnIndex("Fecha2")

        if (cVacaciones.moveToFirst()) {
            while (!cVacaciones.isAfterLast) {
                val strFecha1Db = cVacaciones.getString(iColFecha1)
                val strFecha2Db = cVacaciones.getString(iColFecha2)
                val strAnoDb = strFecha1Db.substring(6)

                if (strAnoDb == strAno) {
                    listaVacacionesAno.add(DatosVacaciones(strFecha1Db, strFecha2Db))
                }
                cVacaciones.moveToNext()
            }
        }
        cVacaciones.close()
        sqlReadDb.close()
        adminDb.close()

        //
        // Ordenamos la lista
        //
        val listaVacacionesOrdenada = ordenarListaVacaciones(listaVacacionesAno)

        return listaVacacionesOrdenada
    }

    fun getDiasLaborables(miContexto: Context, miDato: DatosVacaciones): Int {
        var iDiasLaborables = 0
        val calFecha1 = FuncAux().calFechaFromstrFechaCorta(miDato.strFecha1)
        val calFecha2 = FuncAux().calFechaFromstrFechaCorta(miDato.strFecha2)
        var strFechaLarga = FuncAux().strFechaLargaFromCalendar(calFecha1)
        var isFinSemana = false
        var isFestivo = false

        if(miDato.strFecha1 == miDato.strFecha2){
            if(
                strFechaLarga.substring(0,4) == "sába" ||
                strFechaLarga.substring(0,4) == "Satu" ||
                strFechaLarga.substring(0,4) == "domi" ||
                strFechaLarga.substring(0,4) == "Sund"
            ){
                isFinSemana = true
            }
            else{
                isFestivo = FuncFestivos().isFestivo(miContexto, FuncAux().strFechaCortaToCalendar(calFecha1))
            }

            //
            // Si no es festivo ni fin de semana es laborable
            //
            if(!isFinSemana && !isFestivo){
                return 1
            }
        }
        else{
            while (calFecha1 < calFecha2){
                if(
                    strFechaLarga.substring(0,4) == "sába" ||
                    strFechaLarga.substring(0,4) == "Satu" ||
                    strFechaLarga.substring(0,4) == "domi" ||
                    strFechaLarga.substring(0,4) == "Sund"
                ){
                    isFinSemana = true
                }
                else{
                    isFestivo = FuncFestivos().isFestivo(miContexto, FuncAux().strFechaCortaToCalendar(calFecha1))
                }

                //
                // Si no es festivo ni fin de semana es laborable
                //
                if(!isFinSemana && !isFestivo){
                    iDiasLaborables++
                }

                //
                // Aumentamos un dia
                //

                val tmp = FuncAux().strFechaCortaToCalendar(calFecha1)

                calFecha1.add(Calendar.DAY_OF_MONTH, 1)
                strFechaLarga = FuncAux().strFechaLargaFromCalendar(calFecha1)
                isFestivo = false
                isFinSemana = false
            }
        }

        //
        // Aumentamo un dia porque elcontador empieza e -1
        //
        iDiasLaborables++
        return iDiasLaborables
    }

    fun setDatoVacaciones(miContexto: Context, miDato: DatosVacaciones): Boolean {
        val adminDb = AdminDb(miContexto, null)
        val sqlWrite = adminDb.writableDatabase
        val id = FuncVacaciones().getIdVacacionesByDato(miContexto, miDato)
        var strSql: String? = null
        var datoEditado = DatosVacaciones("", "")

        //
        // si id < 0 datos no existe, insertamos el nuevo valor
        //
        if (id < 0) {
            strSql = "INSERT INTO Vacaciones (Fecha1, " +
                    "Fecha2) VALUES ('${miDato.strFecha1}'," +
                    "'${miDato.strFecha2}');"

            //
            // Restamos dias VacasPendientes
            //
            FuncVacasPendientes().restaVacasPendientes(miContexto, miDato)
        }

        //
        // El datoVacaciones esta contenido en un Registro de ese año, existe, reasignamos
        //
        else {
            //
            // Obtenemos el dato de la Base de Datos
            //
            val datoDb = FuncVacaciones().getDatoVacacionesById(miContexto, id)

            //
            // Fecha1 esta fuera, Fecha2 dentro ---> dato.Fecha1 hasta Fecha2Db
            //
            datoEditado.strFecha1 = miDato.strFecha1
            datoEditado.strFecha2 = datoDb.strFecha2

            //
            // Restamos los dias añadidos al comienzo
            //
            FuncVacasPendientes().restaVacasPendientes(
                miContexto,
                DatosVacaciones(miDato.strFecha1, datoDb.strFecha1)
            )

            //
            // Fecha1 dentro, Fecha2 fuera ---> Fecha1Db hasta dato.Fecha2
            //
            datoEditado.strFecha1 = datoDb.strFecha1
            datoEditado.strFecha2 = miDato.strFecha2

            //
            // Restamos  los dias aumentados al final
            //
            FuncVacasPendientes().restaVacasPendientes(
                miContexto,
                DatosVacaciones(datoDb.strFecha2, miDato.strFecha2)
            )
            //
            // Fecha1 fuera Fecha2 fuera ---> Fecha1 hasta Fecha2
            //
            datoEditado.strFecha1 = miDato.strFecha1
            datoEditado.strFecha2 = miDato.strFecha2

            //
            // Restamos los dias aumentados al principio y al final
            //
            FuncVacasPendientes().restaVacasPendientes(
                miContexto,
                DatosVacaciones(miDato.strFecha1, datoDb.strFecha1)
            )
            FuncVacasPendientes().restaVacasPendientes(
                miContexto,
                DatosVacaciones(datoDb.strFecha2, miDato.strFecha2)
            )

            //
            // Grabamos el nuevo periodo
            //
            strSql = "UPDATE Vacaciones SET Fecha1 = '" +
                    "${datoEditado.strFecha1}', Fecha2 = '" +
                    "${datoEditado.strFecha2}' WHERE _id = ${id};"
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

    fun delDatoVacaciones(miContexto: Context, miDato: DatosVacaciones): Boolean {
        val id = getIdVacacionesByDato(miContexto, miDato)
        val adminDb = AdminDb(miContexto, null)
        val sqlWrite = adminDb.writableDatabase

        if(id < 0){
            sqlWrite.close()
            adminDb.close()
            return false
        }
        else{
            val strSql = "DELETE FROM Vacaciones WHERE _id = $id"

            sqlWrite.execSQL(strSql)
            sqlWrite.close()
            adminDb.close()

            //
            // Sumamos Dias a Vacaciones Pendientes
            //
            FuncVacasPendientes().sumaVacasPendientes(miContexto, miDato)
        }
        return  true
    }

    fun ordenarListaVacaciones(listaVacacionesAno: List<DatosVacaciones>): List<DatosVacaciones> {
        val listaVacacionesOrdenada = mutableListOf<DatosVacaciones>()
        val listaCalendar = mutableListOf<DatosCalendarVacaciones>()

        //
        // Creamos la lista Calendar
        //
        var i = 0
        while (i < listaVacacionesAno.size){
            listaCalendar.add(
                DatosCalendarVacaciones(
                    FuncAux().calFechaFromstrFechaCorta(listaVacacionesAno[i].strFecha1),
                    listaVacacionesAno[i].strFecha1,
                    listaVacacionesAno[i].strFecha2
                )
            )
            i++
        }

        //
        // Ordenamos la lista Calendar
        //
        val listaCalendarOrdenada = listaCalendar.sortedBy { it.calFecha }

        //
        // Creamos la lista Vacaciones Ordenada
        //
        i = 0
        while (i < listaCalendarOrdenada.size){
            listaVacacionesOrdenada.add(
                DatosVacaciones(
                    listaCalendarOrdenada[i].strFecha1,
                    listaCalendarOrdenada[i].strFecha2
                )
            )
            i++
        }

        return  listaVacacionesOrdenada
    }

    fun getVacacionesDisfrutadas(miContexto: Context, strAno: String):Int {
        var iDias = 0
        val listaVacaciones = getListaVacacionesAnuales(miContexto, strAno)
        var i = 0

        while (i < listaVacaciones.size){
            iDias += getDiasLaborables(miContexto, listaVacaciones[i])
            i++
        }
        return iDias
    }

}
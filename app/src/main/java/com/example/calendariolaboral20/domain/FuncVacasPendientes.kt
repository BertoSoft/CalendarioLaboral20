package com.example.calendariolaboral20.domain

import android.content.Context
import com.example.calendariolaboral20.data.databases.AdminDb
import com.example.calendariolaboral20.data.models.DatosVacaciones
import com.example.calendariolaboral20.data.models.DatosVacasPendientes
import java.util.Calendar

class FuncVacasPendientes {

    fun isVacasPendientesVacia(miContexto: Context): Boolean{
        val datoVacasPendientes = getDatoVacasPendientesById(miContexto, 0)

        if(datoVacasPendientes.strYear == ""){
            return true
        }
        return false
    }

    fun getDatoVacasPendientesById(miContexto: Context, id: Int): DatosVacasPendientes{
        var id = -1
        val adminDb = AdminDb(miContexto, null)
        val sqlRead = adminDb.readableDatabase
        val cVacasPendientes = sqlRead.rawQuery("SELECT *FROM VacasPendientes", null)
        val colId = cVacasPendientes.getColumnIndex("_id")
        val colYear = cVacasPendientes.getColumnIndex("Year")
        val colDias = cVacasPendientes.getColumnIndex("Dias")
        var datoVacasPendientes = DatosVacasPendientes("", "")

        if (cVacasPendientes.moveToFirst()){
            while (!cVacasPendientes.isAfterLast){
                if(id == cVacasPendientes.getInt(colId)){
                    datoVacasPendientes.strYear = cVacasPendientes.getString(colYear)
                    datoVacasPendientes.strDias = cVacasPendientes.getString(colDias)
                    cVacasPendientes.moveToLast()
                }
                cVacasPendientes.moveToNext()
            }
        }

        cVacasPendientes.close()
        sqlRead.close()
        adminDb.close()

        return datoVacasPendientes
    }

    fun initVacasPendientes(miContexto: Context) {
        val calFecha = Calendar.getInstance()
        val iMaxAno = calFecha.get(Calendar.YEAR) + 1
        var iAno = 2022
        var iError = 0

        while (iAno <= iMaxAno && iError == 0){
            iError = setDatoVacasPendientes(
                miContexto,
                DatosVacasPendientes(
                    iAno.toString(),
                    "0"
                )
            )
            iAno++
        }
    }

    fun setDatoVacasPendientes(miContexto: Context, miDato: DatosVacasPendientes): Int {
        val adminDb = AdminDb(miContexto, null)
        val sqlWrite = adminDb.writableDatabase
        val id = getIdVacaspendientesByDato(miContexto, miDato)
        var strSql: String? = null

        //
        // si id < 0 datos no existe, si existe id > 0
        //
        if(id < 0){
            strSql = "INSERT INTO VacasPendientes (Year, " +
                    "Dias) VALUES ('${miDato.strYear}'," +
                    "'${miDato.strDias}');"
        }

        //
        // El Registro de ese año ya existe
        //
        else{
            strSql = "UPDATE VacasPendientes SET Year = '" +
                    "${miDato.strYear}', Dias = '" +
                    "${miDato.strDias}' WHERE _id = ${id};"
        }

        if (strSql != null) {
            sqlWrite.execSQL(strSql)
            sqlWrite.close()
            adminDb.close()
            return 0
        }

        sqlWrite.close()
        adminDb.close()
        return -1
    }

    fun getIdVacaspendientesByDato(miContexto: Context, miDato: DatosVacasPendientes): Int {
        var id = -1
        val adminDb = AdminDb(miContexto, null)
        val sqlRead = adminDb.readableDatabase
        val cVacasPendientes = sqlRead.rawQuery("SELECT *FROM VacasPendientes", null)
        var i = 0

        if(cVacasPendientes.moveToFirst()){
            while (!cVacasPendientes.isAfterLast){
                val colId = cVacasPendientes.getColumnIndex("id")
                val colYear = cVacasPendientes.getColumnIndex("Year")
                val strYear = cVacasPendientes.getString(colYear)

                if(strYear == miDato.strYear){
                    id = cVacasPendientes.getInt(colId)
                    cVacasPendientes.moveToLast()
                }
                cVacasPendientes.moveToNext()
            }
        }

        cVacasPendientes.close()
        sqlRead.close()
        adminDb.close()

        return id
    }

    fun restaVacasPendientes(miContexto: Context, miDato: DatosVacaciones) {
        TODO("Not yet implemented")
    }

}
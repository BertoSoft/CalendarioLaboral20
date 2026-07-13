package com.example.calendariolaboral20.domain

import android.content.Context
import android.provider.Telephony
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

    fun initVacasPendientes(miContexto: Context) {
        val calFecha = Calendar.getInstance()
        val iMaxAno = calFecha.get(Calendar.YEAR) + 1
        var iAno = 2022
        var iVacaciones = 9
        var iError = 0

        while (iAno <= iMaxAno && iError == 0){
            iError = setDatoVacasPendientes(
                miContexto,
                DatosVacasPendientes(
                    iAno.toString(),
                    iVacaciones.toString()
                )
            )
            iAno++
            iVacaciones += 22
        }
    }

    fun getDatoVacasPendientesById(miContexto: Context, id: Int): DatosVacasPendientes{
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

    fun getIdVacaspendientesByDato(miContexto: Context, miDato: DatosVacasPendientes): Int {
        var id = -1
        val adminDb = AdminDb(miContexto, null)
        val sqlRead = adminDb.readableDatabase
        val cVacasPendientes = sqlRead.rawQuery("SELECT *FROM VacasPendientes", null)
        var i = 0

        if(cVacasPendientes.moveToFirst()){
            while (!cVacasPendientes.isAfterLast){
                val colId = cVacasPendientes.getColumnIndex("_id")
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

    fun getDiasPendientesByAno(miContexto: Context, strAno: String): Int{
        var iDias = -1
        val listaVacasPendientes = getListaVacasPendientes(miContexto)
        var i = 0

        while (i < listaVacasPendientes.size){
        if(listaVacasPendientes[i].strYear == strAno){
            return listaVacasPendientes[i].strDias.toInt()
        }
            i++
        }
        return  iDias
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

    fun restaVacasPendientes(miContexto: Context, miDato: DatosVacaciones) {
        var iAno = miDato.strFecha1.substring(6, 10).toInt()
        val iMAxAno = FuncAux().strFechaCortaToCalendar(Calendar.getInstance()).substring(6, 10).toInt() + 1
        var i = 0

        //
        // obtenemos los dias laborables
        //
        val iDiasLaborables = FuncVacaciones().getDiasLaborables(miContexto, miDato)
        while (iAno <= iMAxAno){
            val iDiasOld = getDiasPendientesByAno(miContexto, iAno.toString())
            val iDias = iDiasOld - iDiasLaborables

            setDatoVacasPendientes(
                miContexto,
                DatosVacasPendientes(
                    iAno.toString(),
                    iDias.toString()
                )
            )
            iAno++
        }
    }

    private fun getListaVacasPendientes(miContexto: Context): List<DatosVacasPendientes> {
        val adminDb = AdminDb(miContexto, null)
        val sqlRead = adminDb.readableDatabase
        val cVacasPendientes = sqlRead.rawQuery("SELECT *FROM VacasPendientes", null)
        val listaVacasPendientes = mutableListOf<DatosVacasPendientes>()

        if(cVacasPendientes.moveToFirst()){
            while (!cVacasPendientes.isAfterLast){
                val colYear = cVacasPendientes.getColumnIndex("Year")
                val colDias = cVacasPendientes.getColumnIndex("Dias")

                val strYear = cVacasPendientes.getString(colYear)
                val strDias = cVacasPendientes.getString(colDias)

                listaVacasPendientes.add(
                    DatosVacasPendientes(
                        strYear,
                        strDias
                    )
                )
                cVacasPendientes.moveToNext()
            }
        }

        cVacasPendientes.close()
        sqlRead.close()
        adminDb.close()

        return listaVacasPendientes
    }

}
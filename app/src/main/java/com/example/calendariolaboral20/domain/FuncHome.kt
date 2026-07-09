package com.example.calendariolaboral20.domain

import android.content.Context
import com.example.calendariolaboral20.data.databases.AdminDb
import com.example.calendariolaboral20.data.models.DatosRegistro
import java.io.File
import java.util.Calendar

class FuncHome {

    //
    // Devuelve true, si existe, null
    fun isPrimeraEjecucion(miContexto: Context): Boolean {

        //
        // Comprobamos si existe el directorio databases
        //
        val dbName = "CalendarioLaboral20.db"
        val dbFile = miContexto.getDatabasePath(dbName)

        if (dbFile.exists()) {

            //
            // La base de datos ya fue creada anteriormente
            //
            return false
        } else {

            //
            // La base de datos no existe físicamente en el almacenamiento
            //
            return true
        }
    }

    fun setPrimeraEjecucion(miContexto: Context): Boolean {

        //
        // Hacemos una consulta para que se creen todas las tablas
        //
        val datosRegistro = getDatoRegistroById(miContexto, 0)

        //
        // Registramos el inicio de la App
        //
        val calFecha = Calendar.getInstance()
        val strFecha = FuncAux().strFechaCortaToCalendar(calFecha)
        val strHora = FuncAux().strHoraToCalendar(calFecha)

        val respuestaDatoRegistro = setDatoRegistro(
            miContexto,
            DatosRegistro(
                strFecha,
                strHora
            )
        )

        //
        // Iniciamos la tabla VacasPendientes, si no esta iniciada
        //
        if(FuncVacasPendientes().isVacasPendientesVacia(miContexto)){
            FuncVacasPendientes().initVacasPendientes(miContexto)
        }


        // aqui debemos de rellenar la tabla VacasPendientes, pero desde 2022 con 9 Días








        return  respuestaDatoRegistro
    }

    fun setDatoRegistro(miContexto: Context, miDato: DatosRegistro): Boolean {
        val adminDb = AdminDb(miContexto, null)
        val sqlWrite = adminDb.writableDatabase
        val id = getIdRegistroByDato(miContexto, miDato)
        var strSql: String? = null

        //
        // si id < 0 datos no existe, si existe id > 0
        //
        if (id < 0) {
            strSql = "INSERT INTO Registro (Dia, " +
                    "Hora) VALUES ('${miDato.strDia}'," +
                    "'${miDato.strHora}');"
        } else {
            strSql = "UPDATE Registro SET Dia = '" +
                    "${miDato.strDia}', Hora = '" +
                    "${miDato.strHora}' WHERE _id = ${id};"
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

    fun getIdRegistroByDato(miContexto: Context, miDato: DatosRegistro): Int {
        var id = -1
        val adminDb = AdminDb(miContexto, null)
        val sqlRead = adminDb.readableDatabase
        val cRegistro = sqlRead.rawQuery("SELECT *FROM Registro", null)
        var i = 0

        if (cRegistro.moveToFirst()) {
            while (!cRegistro.isAfterLast) {
                val colId = cRegistro.getColumnIndex("_id")
                val colDia = cRegistro.getColumnIndex("Dia")
                val colHora = cRegistro.getColumnIndex("Hora")
                val strDiaDb = cRegistro.getString(colDia)
                val strHoraDb = cRegistro.getString(colHora)

                if (strDiaDb == miDato.strDia && strHoraDb == miDato.strHora) {
                    id = cRegistro.getInt(colId)
                    cRegistro.moveToLast()
                }
                cRegistro.moveToNext()
            }
        }
        cRegistro.close()
        sqlRead.close()
        adminDb.close()

        return id
    }

    fun getDatoRegistroById(miContexto: Context, id: Int): DatosRegistro {
        var datoRegistro = DatosRegistro(null, null)
        val adminDb = AdminDb(miContexto, null)
        val sqlReadDb = adminDb.readableDatabase
        val cRegistro = sqlReadDb.rawQuery("SELECT *FROM Registro", null)

        if (cRegistro.moveToFirst()) {
            while (!cRegistro.isAfterLast) {
                val colId = cRegistro.getColumnIndex("_id")
                val colDia = cRegistro.getColumnIndex("Dia")
                val colHora = cRegistro.getColumnIndex("Hora")

                if (id == cRegistro.getInt(colId)) {
                    datoRegistro.strDia = cRegistro.getString(colDia)
                    datoRegistro.strHora = cRegistro.getString(colHora)
                    cRegistro.moveToLast()
                }
                cRegistro.moveToNext()
            }
        }

        cRegistro.close()
        sqlReadDb.close()
        adminDb.close()

        return datoRegistro
    }

}
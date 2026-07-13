package com.example.calendariolaboral20.domain

import android.content.Context
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.data.databases.AdminDb
import com.example.calendariolaboral20.data.models.DatosMenuPrincipal
import com.example.calendariolaboral20.data.models.DatosRegistro
import java.util.Calendar

class FuncHome {

    //
    // Devuelve true, si existe, null
    //
    fun isPrimeraEjecucion(miContexto: Context): Boolean {

        //
        // Comprobamos si existe el directorio databases
        //
        val dbName = "CalendarioLaboral20.db"
        val dbFile = miContexto.getDatabasePath(dbName)

        if (dbFile.exists()) {
            return false
        }
        return true
    }

    fun getIdRegistroByDato(miContexto: Context, miDato: DatosRegistro): Int {
        var id = -1
        val adminDb = AdminDb(miContexto, null)
        val sqlRead = adminDb.readableDatabase
        val cRegistro = sqlRead.rawQuery("SELECT *FROM Registro", null)

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
        val adminDb = AdminDb(miContexto, null)
        val sqlReadDb = adminDb.readableDatabase
        val cRegistro = sqlReadDb.rawQuery("SELECT *FROM Registro", null)
        val datoRegistro = DatosRegistro(null, null)

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

    fun getListaMenuPrincipal(): List<DatosMenuPrincipal>{
        return listOf(
            DatosMenuPrincipal(
                R.drawable.exceso_jornada,
                "Exceso Jornadas",
                "Mostrar todos los días de exceso de jornada que disponemos en el año ..."
            ),
            DatosMenuPrincipal(
                R.drawable.vacaciones,
                "Vacaciones",
                "Establecer periodos de vacaciones, y consultar vacaciones pendientes..."
            ),
            DatosMenuPrincipal(
                R.drawable.festivos,
                "Festivos",
                "Establecer los distintos días y tipos de festivos del año..."
            ),
            DatosMenuPrincipal(
                R.drawable.backaup,
                "Backup",
                "Guardar o cargar una copia de seguridad de los datos del programa..."
            ),
            DatosMenuPrincipal(
                R.drawable.calendario_laboral,
                "Calendario Laboral",
                "Visualizar un calendario labral anual, con los distintos festivos..."
            ),
            DatosMenuPrincipal(
                R.drawable.salir,
                "Salir",
                "\nSalir de App..."
            )

        )
    }

    fun setPrimeraEjecucion(miContexto: Context): Boolean {

        //
        // Hacemos una consulta para que se creen todas las tablas
        //
        val datoCeroRegistro = getDatoRegistroById(miContexto, 0)

        //
        // Registramos el inicio de la App
        //
        val calFecha = Calendar.getInstance()
        val strFecha = FuncAux().strFechaCortaToCalendar(calFecha)
        val strHora = FuncAux().strHoraToCalendar(calFecha)

        val isSetRegistro = setDatoRegistro(
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
        //
        //Si se grabo el registro sin problemas devolvemos true
        //

        if(isSetRegistro){
            return true
        }
        return false
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

}
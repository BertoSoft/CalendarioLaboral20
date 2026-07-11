package com.example.calendariolaboral20.data.databases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminDb(
    miContexto: Context,
    factory: SQLiteDatabase.CursorFactory?
) : SQLiteOpenHelper(
    miContexto, DATABASE_NAME,
    factory, VERSION_DATABASE
) {

    companion object {
        private const val DATABASE_NAME = "CalendarioLaboral20.db"
        private const val VERSION_DATABASE = 1
    }

    //
    // Se ejecuta de forma automática si la base de datos no existe en el dispositivo
    //
    override fun onCreate(db: SQLiteDatabase) {

        //
        // Estructura de la Tabla Registro
        //
        var TABLE = "Registro"
        var KEY_ID = "_id"
        var KEY_1 = "Dia"
        var KEY_2 = "Hora"


        var strSql = ("CREATE TABLE $TABLE (" +
                "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_1 TEXT, " +
                "$KEY_2 TEXT)")
        db.execSQL(strSql)

        //
        // Estructura de la Tabla Festivos
        //
        TABLE = "Festivos"
        KEY_ID = "_id"
        KEY_1 = "Dia"
        KEY_2 = "Tipo"


        strSql = ("CREATE TABLE $TABLE (" +
                "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_1 TEXT, " +
                "$KEY_2 TEXT)")
        db.execSQL(strSql)


        //
        // Estructura de la Tabla Vacaciones
        //
        TABLE = "Vacaciones"
        KEY_ID = "_id"
        KEY_1 = "Fecha1"
        KEY_2 = "Fecha2"


        strSql = ("CREATE TABLE $TABLE (" +
                "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_1 TEXT, " +
                "$KEY_2 TEXT)")
        db.execSQL(strSql)


        //
        // Estructura de la Tabla VacasPendientes
        //
        TABLE = "VacasPendientes"
        KEY_ID = "_id"
        KEY_1 = "Year"
        KEY_2 = "Dias"


        strSql = ("CREATE TABLE $TABLE (" +
                "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_1 TEXT, " +
                "$KEY_2 TEXT)")
        db.execSQL(strSql)



    }

    override fun onUpgrade(
        p0: SQLiteDatabase?,
        p1: Int,
        p2: Int
    ) {

        p0?.execSQL(/* sql = */ "DROP TABLE IF EXISTS Registro")
        p0?.execSQL(/* sql = */ "DROP TABLE IF EXISTS Festivos")
        p0?.execSQL(/* sql = */ "DROP TABLE IF EXISTS Vacaciones")
        p0?.execSQL(/* sql = */ "DROP TABLE IF EXISTS VacasPendientes")

        onCreate(p0!!)
    }
}
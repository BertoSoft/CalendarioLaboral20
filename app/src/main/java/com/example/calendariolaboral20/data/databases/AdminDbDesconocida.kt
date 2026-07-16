package com.example.calendariolaboral20.data.databases

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminDbDesconocido(
    context: Context,
    factory: SQLiteDatabase.CursorFactory?,
    private var strSql: String = "",
    strNombreBd: String
): SQLiteOpenHelper(
    context,
    strNombreBd,
    null,
    DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {

    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {

    }
}
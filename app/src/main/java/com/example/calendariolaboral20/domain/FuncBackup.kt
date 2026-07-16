package com.example.calendariolaboral20.domain

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.calendariolaboral20.data.databases.AdminDbDesconocido
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class FuncBackup {

    private var strSql: String? = null

    fun strNombreArchivoFromUri(miContexto: Context, uri: Uri): String {
        var strNombreArchivo = ""

        //
        // Obtenemos el nombre del archivo por la uri
        //
        miContexto.contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (columnIndex != -1) {
                    strNombreArchivo = cursor.getString(columnIndex)
                }
            }
        }

        return strNombreArchivo
    }

    fun fileFromUri(miContexto: Context, uri: Uri): File?{

        //
        // Abrir el InputStream a través del ContentResolver
        //
        val inputStream: InputStream? = miContexto.contentResolver.openInputStream(uri)

        //
        // Crear un archivo de destino en el almacenamiento privado de la app
        //
        val dirDatabases = miContexto.getDatabasePath("CalendarioLaboral20.db").parentFile
        val tempFile = File(dirDatabases, "temp_database.db")
        val outputStream = FileOutputStream(tempFile)

        //
        // Copiar los bytes
        //
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return tempFile
    }

    fun isSqlite(miContexto: Context, uri: Uri): Boolean {
        var isSqlite = false

        //
        // Obtenemos la ruta del archivo y comprobamos contenido
        //
        miContexto.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                val stringBuilder = StringBuilder()
                var linea: String? = reader.readLine()

                while (linea != null) {
                    stringBuilder.append(linea).append("\n")
                    linea = reader.readLine()
                }
                val strInicioArchivo = stringBuilder.toString().substring(0, 13)

                //
                // Aqui comprobamos el archivo
                //
                if (strInicioArchivo == "SQLite format") {
                    isSqlite = true
                }
            }
        }

        return isSqlite
    }

    fun isDatosCalendarioDb(miContexto: Context, uri: Uri): Boolean {
        var isDatosCalendarioDb = false
        var existeVacaciones = false
        var existeFestivos = false
        var existeVacacionesPendientes = false
        var existeRegistro = false
        val listaTablas = mutableListOf<String>()

        //
        // Obtenemos el nombre del archivo por la uri
        //
        val file = fileFromUri(miContexto, uri)

        //
        // Abrimos la Base de datos SQLite solo si file no es nulo
        //
        val adminDbHelperDesconocido = AdminDbDesconocido(
            miContexto,
            null,
            "",
            file?.absolutePath.toString()
        )

        val sqlReadDb = adminDbHelperDesconocido.readableDatabase

        //
        // Obtenemos la tabla Maestra
        //
        strSql = "SELECT name FROM sqlite_master WHERE type='table' OR type='view'"
        val cMaster = sqlReadDb.rawQuery(strSql!!, null)

        //
        // Obtenemos todas las tablas de la Base de Datos
        //
        if (cMaster.moveToFirst()) {
            while (!cMaster.isAfterLast) {
                listaTablas.add(cMaster.getString(0))
                cMaster.moveToNext()
            }
        }

        //
        // Comrpobamos si existen todas las tablas
        //
        var i = 0
        while (i < listaTablas.size) {
            if (listaTablas[i] == "Registro") existeRegistro = true
            if (listaTablas[i] == "Festivos") existeFestivos = true
            if (listaTablas[i] == "Vacaciones") existeVacaciones = true
            if (listaTablas[i] == "VacasPendientes") existeVacacionesPendientes = true
            i++
        }

        if (existeFestivos &&
            existeVacaciones &&
            existeVacacionesPendientes &&
            existeRegistro
        ){
            isDatosCalendarioDb = true
        }

        cMaster.close()
        sqlReadDb.close()
        adminDbHelperDesconocido.close()

        //
        // Borramos el archivo creado por adminDbHelperDesconocido
        // el archivo esta en databases y es temp_database
        //
        miContexto.deleteDatabase("temp_database.db")

        return isDatosCalendarioDb
    }

    fun copyNewDatosToCalendarioDb(miContexto: Context, uri: Uri): Boolean {
        val strNombreDb = "CalendarioLaboral20.db"
        var todoBien = false
        val dirDatabases = miContexto.getDatabasePath(strNombreDb).parentFile

        //
        // 2. Crear la carpeta si no existe
        //
        if (dirDatabases != null && !dirDatabases.exists()) {
            dirDatabases.mkdirs()
        }

        val archivoDestino = File(dirDatabases, strNombreDb)

        //
        // 3. Abrir el Stream de lectura desde la Uri y el de escritura al destino
        //
        miContexto.contentResolver.openInputStream(uri).use { input ->
            if (input == null) return false

            FileOutputStream(archivoDestino).use { output ->

                //
                // 4. Copiar los bytes en bloques de 4KB
                //
                val buffer = ByteArray(4096)
                var bytesLeidos: Int
                while (input.read(buffer).also { bytesLeidos = it } != -1) {
                    output.write(buffer, 0, bytesLeidos)
                }
                todoBien = true
            }

            return todoBien
        }
    }

    fun copyCalendarioDbToDatosOld(miContexto: Context, uri: Uri): Boolean {

        // 1. Obtener la ruta del archivo SQLite interno original
        val archivoOrigen: File = miContexto.getDatabasePath("CalendarioLaboral20.db")

        // Si la base de datos no existe, no hay nada que copiar
        if (!archivoOrigen.exists()) return false

        return try {
            // 2. Abrir el stream de origen (Lectura)
            val inputStream = FileInputStream(archivoOrigen)

            // 3. Abrir el stream de destino usando el ContentResolver y la URI (Escritura)
            val outputStream = miContexto.contentResolver.openOutputStream(uri)
                ?: throw IOException("No se pudo abrir el stream de destino para la URI proporcionada")

            // 4. Copiar los bytes en bloques de 4KB
            val buffer = ByteArray(4096)
            var bytesLeidos: Int

            inputStream.use { input ->
                outputStream.use { output ->
                    while (input.read(buffer).also { bytesLeidos = it } != -1) {
                        output.write(buffer, 0, bytesLeidos)
                    }
                }
            }
            true // Copia exitosa
        } catch (e: Exception) {
            e.printStackTrace()
            false // Error durante la copia
        }
    }

}
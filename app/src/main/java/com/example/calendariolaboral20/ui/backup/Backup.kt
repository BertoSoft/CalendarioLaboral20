package com.example.calendariolaboral20.ui.backup

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.databinding.ActivityBackupBinding
import com.example.calendariolaboral20.domain.FuncBackup

//
// Variables globales
//
private lateinit var binding: ActivityBackupBinding
private lateinit var uriGlobal: Uri


class Backup : AppCompatActivity() {

    //
    // Declaramos el Dialogo de Abrir Archivo
    //
    val abrirArchivoDialog = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri: Uri? ->

        //
        // En uri recibimos la ruta del archivo seleccionado por el usuario
        //
        if(uri != null){

            uriGlobal = uri
            //
            // Aqui debemos rellenar el tvRutaArchivoIn2
            //
            val strNombreArchivo = FuncBackup().strNombreArchivoFromUri(this, uriGlobal)

            //
            // Mostramos la ruta y activamos el boton importarArchivo
            //
            val str = "   " + strNombreArchivo
            binding.tvRutaArchivoIn2.text = str
            binding.btnImportar.isEnabled = true

        }
        else{
            limpiaControles()
            binding.btnImportar.isEnabled = false
        }
    }

    //
    // Declaramos el dialogo de GuardarArchivo
    //
    val guardarArchivoDialog = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            if (uri != null) {

                uriGlobal = uri
                //
                // Aqui debemos rellenar el tvRutaArchivoIn2
                //
                val strNombreArchivo = FuncBackup().strNombreArchivoFromUri(this, uriGlobal)

                //
                // Mostramos la ruta y activamos el boton importarArchivo
                //
                val str = "   " + strNombreArchivo
                binding.tvRutaArchivoOut2.text = str
                binding.btnExportar.isEnabled = true

                //
                // Borramos el archivo vacio creado por el dialogo
                //
                //DocumentsContract.deleteDocument(contentResolver, uriGlobal)
            }
            else{
                limpiaControles()
                binding.btnExportar.isEnabled = false
            }
        }
    }

    //
    // Metodo onCreate()
    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
        initListeners()

    }

    private fun initListeners() {

        //
        // Si se clicka en ivAbrir
        //
        binding.ivAbrir.setOnClickListener {
            abrirArchivoDialog.launch("*/*")
        }

        //
        // Si se clicka en ivGuardar
        //
        binding.ivGuardar.setOnClickListener {

            //
            // Situamos el dialog en el directorio Download
            //
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "application/vnd.sqlite3"
                putExtra(Intent.EXTRA_TITLE, "CalendrioLaboral20.old")

                // URI específico para abrir la carpeta Descargas en Android 13+
                val downloadsUri = DocumentsContract.buildRootsUri("com.android.providers.downloads.documents")
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, downloadsUri)
            }

            guardarArchivoDialog.launch(intent)
        }

        //
        // Si se clicka en btnImportar
        //
        binding.btnImportar.setOnClickListener {
            if(importarArchivo(uriGlobal)){
                Toast.makeText(
                    binding.btnImportar.context,
                    "Archivo de datos importado con exito ...",
                    Toast.LENGTH_SHORT
                ).show()

                limpiaControles()
                binding.btnImportar.isEnabled = false
            }
        }

        //
        // Si se cllicka en btnExportar
        //
        binding.btnExportar.setOnClickListener {
            if(exportarArchivo(uriGlobal)){
                Toast.makeText(
                    binding.btnImportar.context,
                    "Archivo de datos exportado con exito ...",
                    Toast.LENGTH_SHORT
                ).show()

                limpiaControles()
                binding.btnExportar.isEnabled = false
            }
        }

    }

    private fun exportarArchivo(uri: Uri): Boolean {
        return FuncBackup().copyCalendarioDbToDatosOld(this, uriGlobal)
    }

    private fun importarArchivo(uri: Uri): Boolean {
        var strError = ""
        var existeDirectorio = false
        var existeArchivo = false
        var esSqlite = false
        var esDatosCalendarioDb = false

        //
        // Comprobamos si existe el directorio /data/data/databases/
        //
        val dirDatabases = getDatabasePath("dummy").parentFile
        if(
            dirDatabases != null &&
            dirDatabases.exists() &&
            dirDatabases.isDirectory
        ){
            existeDirectorio = true
        }
        else{
            if(dirDatabases.mkdirs()){
                existeDirectorio = true
            }
            else{
                strError = " Error al crear el directorio Databases..."
            }
        }

        //
        // Comprobamos que existe el fichero seleccionado
        //
        val cursor = contentResolver.query(
            uri,
            arrayOf(OpenableColumns.DISPLAY_NAME),
            null,
            null,
            null
        )

        cursor?.use {
            //
            // Si el cursor tiene al menos una fila, el archivo existe en el sistema
            //
            if(it.moveToFirst()){
                existeArchivo = true
            }
            else{
                strError = " El archivo seleccionado no es accesible..."
            }

        }

        //
        // Comprobamos si es archivo SQLite
        //
        if(existeArchivo){
            esSqlite = FuncBackup().isSqlite(this, uri)
            if(!esSqlite){
                strError = "No se reconoce el formato del archivo seleccionado..."
            }
        }


        //
        // Comprobamos si es un archivo valido
        //
        if(esSqlite){
            esDatosCalendarioDb = FuncBackup().isDatosCalendarioDb(this, uri)
            if(!esDatosCalendarioDb){
                strError = "No se reconoce el archivo o esta dañado..."
            }
        }

        //
        // Si todo Correcto copiamos archivo nuevo, sino damos el error
        //
        if(
            existeDirectorio &&
            existeArchivo &&
            esSqlite &&
            esDatosCalendarioDb &&
            strError == ""
        )
        {
            //
            // Procedemos a hacer la copia del archivo
            //
            if(!FuncBackup().copyNewDatosToCalendarioDb(this, uri)){
                strError = "Se produjo un error al copiar el archivo de Datos... "
                Toast.makeText(
                    this,
                    strError,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        else{
            Toast.makeText(
                this,
                strError,
                Toast.LENGTH_SHORT
            ).show()
        }

        //
        // Si strError esta vacio todo ok
        //
        if(strError == ""){
            return true
        }
        else{
            return false
        }
    }

    private fun initUi() {

        limpiaControles()
        binding.btnImportar.isEnabled = false
        binding.btnExportar.isEnabled = false
    }

    private fun limpiaControles() {
        binding.tvRutaArchivoIn2.text = ""
        binding.tvRutaArchivoOut2.text = ""
    }

}
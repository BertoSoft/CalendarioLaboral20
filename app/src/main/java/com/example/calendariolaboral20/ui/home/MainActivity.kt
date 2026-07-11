package com.example.calendariolaboral20.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calendariolaboral20.data.models.DatosMenuPrincipal
import com.example.calendariolaboral20.data.models.DatosRegistro
import com.example.calendariolaboral20.databinding.ActivityMainBinding
import com.example.calendariolaboral20.domain.FuncAux
import com.example.calendariolaboral20.domain.FuncHome
import com.example.calendariolaboral20.ui.festivos.Festivos
import com.example.calendariolaboral20.ui.home.adapter.HomeAdapter
import java.util.Calendar

//
// Variables Globales
//
private lateinit var miAdapter: HomeAdapter
private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //
        // aqui inicia mi codigo
        //
        initUi()

    }

    private fun initUi() {
        initApp()
        initRv()

    }

    private fun initApp() {

        //
        // Si es la primera ejecucion setPrimeraEjecucion()
        //
        if(FuncHome().isPrimeraEjecucion(this)){

            //
            //Si falla setPrimeraEjecucion, cerramos la App
            //
            if(!FuncHome().setPrimeraEjecucion(this)){

                Toast.makeText(
                    this,
                    "Imposible acceder al archivo de datos...",
                    Toast.LENGTH_SHORT
                ).show()
                closeApp(-1)
            }
        }

        //
        // Si no es la primera ejecucion, anotamos el registro de inicio
        //
        else{
            val calFecha = Calendar.getInstance()

            //
            // Si hay un error al grabar el registro de inicio, cerramos App
            //
            if(!FuncHome().setDatoRegistro(
                    this,
                    DatosRegistro(
                        FuncAux().strFechaCortaToCalendar(calFecha),
                        FuncAux().strHoraToCalendar(calFecha)
                    )
                )
            ){

                //
                // Se produjo un error al grabar datos en la tabla registro
                //
                Toast.makeText(
                    this,
                    "Se produjo un error al grabar datos en la Base de Datos...",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }

    }

    private fun initRv() {
        miAdapter = HomeAdapter(
            FuncHome().getListaMenuPrincipal(this),
            {datoMenuPrincipal -> onClickLambda(datoMenuPrincipal) }
        )

        binding.rvMenuPrincipal.layoutManager = LinearLayoutManager(this)
        binding.rvMenuPrincipal.adapter = miAdapter
    }

    private fun onClickLambda(datoMenuPrincipal: DatosMenuPrincipal){
        when (datoMenuPrincipal.strTitulo) {
            "Salir" -> callSalirApp()
            "Festivos" -> callFestivos()
            //"Vacaciones" -> funCallVacaciones()
            //"Resumen Exceso Jornadas" -> funExcesoJornadas()
            //"Calendario Laboral" -> funCalendarioLaboral()
            //"Backup" -> funBackup()
        }
    }

    //
    // Opciones del menu principal
    //
    private fun callSalirApp(){
        closeApp(0)
    }

    private fun callFestivos(){
        val intent = Intent(this, Festivos::class.java)
        startActivity(intent)
    }

    private fun closeApp(iError: Int) {
        if(iError < 0){
            Toast.makeText(
                this,
                "Se produjo un error crítico, se cierra la App...",
                Toast.LENGTH_SHORT
            ).show()
        }
        finish()
    }
}
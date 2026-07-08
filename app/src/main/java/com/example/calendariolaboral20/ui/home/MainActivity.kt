package com.example.calendariolaboral20.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.data.models.DatosRegistro
import com.example.calendariolaboral20.domain.FuncAux
import com.example.calendariolaboral20.domain.FuncHome
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //
        // aqui inicia mi codigo
        //
        initUi()



    }

    private fun initUi() {
        initApp()
    }

    private fun initApp() {

        //
        // Si es la primera ejecucion setPrimeraEjecucion()
        //
        if(FuncHome().isPrimeraEjecucion(this)){
            if(!FuncHome().setPrimeraEjecucion(this)){

                //
                // si la fun setPrimeraEjecucion devuelve falso, avisamos y cerramos App
                //
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

    private fun closeApp(iError: Int) {
        val i = 0
        finish()
    }


}
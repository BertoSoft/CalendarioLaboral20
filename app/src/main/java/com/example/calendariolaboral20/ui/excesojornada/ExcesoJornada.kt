package com.example.calendariolaboral20.ui.excesojornada

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.databinding.ActivityExcesoJornadaBinding
import com.example.calendariolaboral20.domain.FuncExcesoJornada
import java.util.Calendar


//
// Declaraciones
//
private lateinit var binding: ActivityExcesoJornadaBinding
private var calFecha = Calendar.getInstance()


class ExcesoJornada : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExcesoJornadaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

    }

    private fun initUi() {

        initSpAnos()
        limpiaControles()
        rellenaControles()
        initListener()
    }

    private fun initListener() {

        //
        // Si la seleccion del spAnos cambia
        //
        binding.spAno.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if(position != 0){
                    limpiaControles()
                    rellenaControles()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

                //
                // Si no existe seleccion
                //
            }

        }
    }

    private fun rellenaControles() {

        //
        // Rellenamos todos los textview del uno el seis
        //
        binding.tv12.text = FuncExcesoJornada().getSabados(binding.spAno.selectedItem.toString()).toString()

        binding.tv22.text = FuncExcesoJornada().getDomingos(binding.spAno.selectedItem.toString()).toString()

        binding.tv32.text = FuncExcesoJornada().getFestivosNacionales(this, binding.spAno.selectedItem.toString()).toString()

        binding.tv42.text = FuncExcesoJornada().getFestivosAutonomicos(this, binding.spAno.selectedItem.toString()).toString()

        binding.tv52.text = FuncExcesoJornada().getFestivosLocales(
            binding.tv52.context,
            binding.spAno.selectedItem.toString()
        ).toString()

        binding.tv62.text = "1"

        //
        // Ahora seguimos a partir del siete
        //
        calFecha.set(binding.spAno.selectedItem.toString().toInt(), 0, 1)

        var iDias = calFecha.getActualMaximum(Calendar.DAY_OF_YEAR)
        iDias -= binding.tv12.text.toString().toInt()
        iDias -= binding.tv22.text.toString().toInt()
        iDias -= binding.tv32.text.toString().toInt()
        iDias -= binding.tv42.text.toString().toInt()
        iDias -= binding.tv52.text.toString().toInt()
        iDias -= 1
        val iHoras = iDias * 8

        var str = "("
        str += calFecha.getActualMaximum(Calendar.DAY_OF_YEAR)
        str += "-"
        str += binding.tv12.text.toString()
        str += "-"
        str += binding.tv22.text.toString()
        str += "-"
        str += binding.tv32.text.toString()
        str += "-"
        str += binding.tv42.text.toString()
        str += "-"
        str += binding.tv52.text.toString()
        str += "-1) x 8h. = "
        str += iHoras
        str += " h."

        binding.tv7.text = str

        val iHorasTrabajadas = (iHoras - 176)
        str = iHoras.toString()
        str += "h. Totales - 176 h. Vacaciones = "
        str += iHorasTrabajadas.toString()
        str += " h."

        binding.tv9.text = str

        val iHorasExceso = iHorasTrabajadas - 1752
        str = iHorasTrabajadas.toString()
        str += " h. - 1752 h. = "
        str += iHorasExceso
        str += " h."

        binding.tv10.text = str

        val iDiasExceso = (iHorasExceso / 8)
        str = "("
        str += iHorasExceso.toString()
        str += " h./ 8 h.) = "
        str += iDiasExceso.toString()
        str += " Días de Exceso Jornada"

        binding.tvFinal.text = str



    }

    private fun initSpAnos() {
        var listaAnos = emptyList<String>()
        val calFecha = Calendar.getInstance()
        var iAnoActual = calFecha.get(Calendar.YEAR)

        iAnoActual++
        while (iAnoActual >= 2022) {
            listaAnos += iAnoActual.toString()
            iAnoActual--
        }

        val adaptadorSpinner = ArrayAdapter<String>(
            this,
            R.layout.item_sp,
            listaAnos
        )
        adaptadorSpinner.setDropDownViewResource(R.layout.item_sp)
        binding.spAno.adapter = adaptadorSpinner
        binding.spAno.setSelection(1)
    }

    private fun limpiaControles() {
        binding.tv12.text = ""
        binding.tv22.text = ""
        binding.tv32.text = ""
        binding.tv42.text = ""
        binding.tv52.text = ""
        binding.tv62.text = ""
    }

}
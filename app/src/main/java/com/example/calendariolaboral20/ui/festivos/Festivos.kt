package com.example.calendariolaboral20.ui.festivos

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import androidx.core.view.isVisible
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.data.models.DatosFestivos
import com.example.calendariolaboral20.databinding.ActivityFestivosBinding
import com.example.calendariolaboral20.domain.FuncAux
import com.example.calendariolaboral20.domain.FuncFestivos
import java.util.Calendar

private lateinit var binding: ActivityFestivosBinding
private var calFecha = Calendar.getInstance()

class Festivos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFestivosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()

    }

    private fun initUi() {
        binding.ivEliminar.isVisible = false
        limpiaControles()
        desactivaControles()
        initSps()
        initRv()
        initListeners()
    }

    private fun initListeners() {

        //
        // Establece la nueva fecha que devuelve DatePicker
        //
        val setFechaListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calFecha.set(Calendar.YEAR, year)
                calFecha.set(Calendar.MONTH, monthOfYear)
                calFecha.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.tvFecha.text = FuncAux().strFechaCortaToCalendar(calFecha)
            }

        //
        // Se pulsa ivNuevo
        //
        binding.ivNuevo.setOnClickListener {
            limpiaControles()
            activaControles()
            binding.ivEliminar.isVisible = false
            binding.spFestivo.performClick()
        }

        //
        // Cambio seleccion spFestivos
        //
        binding.spFestivo.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {

                //
                // Se ha hecho una seleccion, si ivEliminar esta invisible, lanzamos el selector de fechas
                //
                if(p2 != 0 && !binding.ivEliminar.isVisible){

                    //
                    // Lanzamos el datepicker
                    //
                    binding.tvFecha.text = FuncAux().strFechaCortaToCalendar(calFecha)
                    DatePickerDialog(
                        binding.spFestivo.context,
                        setFechaListener,
                        calFecha.get(Calendar.YEAR),
                        calFecha.get(Calendar.MONTH),
                        calFecha.get(Calendar.DAY_OF_MONTH)
                    ).show()
                    activaControles()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

                //
                // No se ha seleccionado nada
                //
            }

        }

        //
        // Se pulsa en tvFecha
        //
        binding.tvFecha.setOnClickListener {

            //
            // Si pinchamos en tvFecha, lanzamos el dialogo de selector de fecha
            //
            if(binding.tvFecha.text != ""){
                DatePickerDialog(
                    binding.tvFecha.context,
                    setFechaListener,
                    calFecha.get(Calendar.YEAR),
                    calFecha.get(Calendar.MONTH),
                    calFecha.get(Calendar.DAY_OF_MONTH)
                ).show()

            }
        }

        //
        // Se pulsa en btnGuardar
        //
        binding.btnGuardar.setOnClickListener {
            if(binding.tvFecha.text != "" && binding.spFestivo.selectedItem != 0){
                guardarDatos()
                limpiaControles()
                desactivaControles()
                binding.ivEliminar.isVisible = false
            }
        }
    }

    fun guardarDatos() {
        var res = false
        //
        // Si son vacaciones llamo setVacaciones, si es otro setFestivo
        //
        if(binding.spFestivo.selectedItem.toString() == "Vacaciones"){

            //
            // Llammamos setVacaciones
            //


        }
        else{

            res = FuncFestivos().setFestivo(
                this,
                DatosFestivos(
                    binding.tvFecha.text.toString(),
                    binding.spFestivo.selectedItem.toString()
                )
            )
        }

        //
        // Segun resultado de res informamos como ha ido la grabacion del dato
        //






    }


    private fun initRv() {

    }

    private fun initSps() {
        initSpAno()
        initSpFestivos()
    }

    private fun initSpFestivos() {
        var arrayFestivos = arrayOf<String>()

        arrayFestivos += ""
        arrayFestivos += "Nacionales"
        arrayFestivos += "Autonomico"
        arrayFestivos += "Local"
        arrayFestivos += "Convenio"
        arrayFestivos += "Exceso de Jornada"
        arrayFestivos += "Vacaciones"

        val miAdaptadorSp = ArrayAdapter<String>(
            this,
            R.layout.item_sp,
            arrayFestivos
        )
        miAdaptadorSp.setDropDownViewResource(R.layout.item_sp)
        binding.spFestivo.adapter = miAdaptadorSp
    }

    private fun initSpAno() {

        //
        // Hacemos una lista desde 2022 hasta 1 año desùes del actual
        //
        var arrayAnos = arrayOf<String>()
        var iAno = Calendar.getInstance().get(Calendar.YEAR)

        iAno++
        while (iAno >= 2022){
            arrayAnos += iAno.toString()
            iAno --
        }

        val miAdaptadorSp = ArrayAdapter<String>(
            this,
            R.layout.item_sp,
            arrayAnos
        )
        miAdaptadorSp.setDropDownViewResource(R.layout.item_sp)
        binding.spAno.adapter = miAdaptadorSp


    }

    private fun limpiaControles() {
        binding.tvFecha.text = ""
        binding.spFestivo.setSelection(0)

    }

    private fun activaControles() {
        binding.spFestivo.isEnabled = true
        binding.tvFecha.isEnabled = true
        binding.btnGuardar.isEnabled = true
    }

    private fun desactivaControles() {
        binding.spFestivo.isEnabled = false
        binding.tvFecha.isEnabled = false
        binding.btnGuardar.isEnabled = false
    }
}
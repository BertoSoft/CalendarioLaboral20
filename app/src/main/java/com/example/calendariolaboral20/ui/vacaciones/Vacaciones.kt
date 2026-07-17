package com.example.calendariolaboral20.ui.vacaciones

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.data.models.DatosVacaciones
import com.example.calendariolaboral20.databinding.ActivityVacacionesBinding
import com.example.calendariolaboral20.domain.FuncAux
import com.example.calendariolaboral20.domain.FuncVacaciones
import com.example.calendariolaboral20.ui.resvacas.ResVacas
import com.example.calendariolaboral20.ui.vacaciones.adapter.VacacionesAdapter
import java.util.Calendar

private lateinit var binding: ActivityVacacionesBinding
private lateinit var miAdaptador: VacacionesAdapter
private  var calFecha = Calendar.getInstance()
private  var calFecha1 = Calendar.getInstance()
private  var calFecha2 = Calendar.getInstance()


class Vacaciones : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVacacionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
    }

    private fun initUi() {
        binding.ivEliminar.isVisible = false
        limpiaControles()
        desactivaControles()
        initSp()
        initRv()
        initListeners()
    }

    private fun initListeners() {

        //
        // Establece la nueva fecha que devuelve DatePicker para Fecha1
        //
        val setFechaListener1 =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calFecha1.set(Calendar.YEAR, year)
                calFecha1.set(Calendar.MONTH, monthOfYear)
                calFecha1.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.tvFecha1.text = FuncAux().strFechaCortaToCalendar(calFecha1)
            }

        //
        // Establece la nueva fecha que devuelve DatePicker para Fecha2
        //
        val setFechaListener2 =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calFecha2.set(Calendar.YEAR, year)
                calFecha2.set(Calendar.MONTH, monthOfYear)
                calFecha2.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.tvFecha2.text = FuncAux().strFechaCortaToCalendar(calFecha2)
            }

        //
        // Se pulsa en tvFecha1, cuando esta activo
        //
        binding.tvFecha1.setOnClickListener {
            var calFechaDatePicker = Calendar.getInstance()

            if(calFecha.get(Calendar.YEAR) == binding.spAno.selectedItem.toString().toInt()){
                calFechaDatePicker = calFecha
            }
            else{
                calFechaDatePicker.set(
                    binding.spAno.selectedItem.toString().toInt(),
                    0,
                    1
                )
            }
            val miDialogoCalendario1 = DatePickerDialog(
                binding.tvFecha1.context,
                setFechaListener1,
                calFechaDatePicker.get(Calendar.YEAR),
                calFechaDatePicker.get(Calendar.MONTH),
                calFechaDatePicker.get(Calendar.DAY_OF_MONTH)
            )
            miDialogoCalendario1.setTitle("Fecha Inicio Vacaciones ...")
            miDialogoCalendario1.show()
        }

        //
        // Se pulsa en tvFecha2, cuando esta activo
        //
        binding.tvFecha2.setOnClickListener {
            var calFechaDatePicker = Calendar.getInstance()

            if(calFecha.get(Calendar.YEAR) == binding.spAno.selectedItem.toString().toInt()){
                calFechaDatePicker = calFecha
            }
            else{
                calFechaDatePicker.set(
                    binding.spAno.selectedItem.toString().toInt(),
                    0,
                    1
                )
            }
            val miDialogoCalendario2 = DatePickerDialog(
                binding.tvFecha2.context,
                setFechaListener2,
                calFechaDatePicker.get(Calendar.YEAR),
                calFechaDatePicker.get(Calendar.MONTH),
                calFechaDatePicker.get(Calendar.DAY_OF_MONTH)
            )
            miDialogoCalendario2.setTitle("Fecha Final Vacaciones ...")
            miDialogoCalendario2.show()
        }

        //
        // Se Pulsa el Boton +
        //
        binding.ivSumar.setOnClickListener {
            var calFechaDatePicker = Calendar.getInstance()

            if(calFecha.get(Calendar.YEAR) == binding.spAno.selectedItem.toString().toInt()){
                calFechaDatePicker = calFecha
            }
            else{
                calFechaDatePicker.set(
                    binding.spAno.selectedItem.toString().toInt(),
                    0,
                    1
                )
            }
            binding.ivEliminar.isVisible = false
            limpiaControles()
            activaControles()

            //
            // Lanzamos el calendario de inicio vacaciones
            //
            val miDatePickerDialogo1 = DatePickerDialog(
                binding.ivSumar.context,
                setFechaListener1,
                calFechaDatePicker.get(Calendar.YEAR),
                calFechaDatePicker.get(Calendar.MONTH),
                calFechaDatePicker.get(Calendar.DAY_OF_MONTH)
            )
            miDatePickerDialogo1.setTitle("Fecha Inicio Vacaciones ...")
            miDatePickerDialogo1.show()

            //
            // Cuando se cierra el calendario de inicio de vacaciones
            //
            miDatePickerDialogo1.setOnDismissListener {
                if (binding.tvFecha1.text != "") {

                    //
                    // Lanzamos el calendario final de vacaciobnes
                    //
                    val miDatePickerDialogo2 = DatePickerDialog(
                        binding.ivSumar.context,
                        setFechaListener2,
                        calFechaDatePicker.get(Calendar.YEAR),
                        calFechaDatePicker.get(Calendar.MONTH),
                        calFechaDatePicker.get(Calendar.DAY_OF_MONTH)
                    )
                    miDatePickerDialogo2.setTitle("Fecha Final Vacaciones ...")
                    miDatePickerDialogo2.show()

                    //
                    // Si cancelamos el calendario final, colocamos la fecha de inicio
                    //
                    miDatePickerDialogo2.setOnCancelListener {
                        binding.tvFecha2.text = FuncAux().strFechaCortaToCalendar(calFecha1)
                    }
                }
            }

            //
            // Si cancelamos el calendario de inicio desactivamos controles
            //
            miDatePickerDialogo1.setOnCancelListener {
                limpiaControles()
                desactivaControles()
            }
        }

        //
        // Pulsamos el ivResumen
        //
        binding.ivResumen.setOnClickListener {
            callResumenVacaciones()
        }

        //
        // Si se pulsa el boton eliminar
        //
        binding.ivEliminar.setOnClickListener {
            if(binding.tvFecha1.text != "" && binding.tvFecha2.text != ""){
                borrarDatos()
            }
        }

        //
        // Boton Guardar
        //
        binding.btnGuardar.setOnClickListener {

            //
            // Si una de las fechas esta vacia avisamos
            //
            if(binding.tvFecha1.text == "" || binding.tvFecha2.text == ""){
                if(binding.tvFecha1.text == ""){
                    Toast.makeText(
                        binding.btnGuardar.context,
                        "Debes de elegir una fecha inicial...",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.tvFecha1.performClick()
                }

                if(binding.tvFecha2.text == ""){
                    Toast.makeText(
                        binding.btnGuardar.context,
                        "Debes de elegir una fecha Final...",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.tvFecha2.performClick()
                }
            }

            //
            // Los dos tvFechas estan cubiertas
            //
            else{
                calFecha1 = FuncAux().calFechaFromstrFechaCorta(binding.tvFecha1.text.toString())
                calFecha2 = FuncAux().calFechaFromstrFechaCorta(binding.tvFecha2.text.toString())

                //
                // Si la Fecha1 es posterior a Fecha2 avisamos
                //
                if(calFecha1 > calFecha2){
                    Toast.makeText(
                        binding.btnGuardar.context,
                        "La fecha inicial no puede ser posterior a la fecha final...",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.ivSumar.performClick()
                }

                //
                // Todo Correcto grabamos
                //
                else{
                    guardarDatos()
                }

            }

        }

        //
        // Si spAno cambia de seleccion
        //
        binding.spAno.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                limpiaControles()
                desactivaControles()
                binding.ivEliminar.isVisible = false
                miAdaptador.funRefrescaLista(
                    FuncVacaciones().getListaVacacionesAnuales(
                        binding.spAno.context,
                        binding.spAno.selectedItem.toString()
                    )
                )

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }

    fun borrarDatos() {
        val strAno = binding.tvFecha1.text.toString().substring(6, 10)
        val datoVacaciones = DatosVacaciones(
            binding.tvFecha1.text.toString(),
            binding.tvFecha2.text.toString()
        )

        val res = FuncVacaciones().delDatoVacaciones(this, datoVacaciones)

        if(res){
            limpiaControles()
            desactivaControles()
            binding.ivEliminar.isVisible = false
            miAdaptador.funRefrescaLista(
                FuncVacaciones().getListaVacacionesAnuales(
                    this,
                    strAno
                )
            )
        }
    }

    fun guardarDatos() {
        var res = false
        val datoVacaciones = DatosVacaciones(
            binding.tvFecha1.text.toString(),
            binding.tvFecha2.text.toString()
        )

        res = FuncVacaciones().setDatoVacaciones(
            this,
            datoVacaciones
        )

        if(res){
            Toast.makeText(
                this,
                "Periodo de Vacaciones guardado con exito...",
                Toast.LENGTH_SHORT
            ).show()
            miAdaptador.funRefrescaLista(FuncVacaciones().getListaVacacionesAnuales(
                this,
                binding.spAno.selectedItem.toString()
            ))
            limpiaControles()
            desactivaControles()
            binding.ivEliminar.isVisible = false
        }


    }

    private fun initRv() {
        miAdaptador = VacacionesAdapter(
            FuncVacaciones().getListaVacacionesAnuales(
                this,
                binding.spAno.selectedItem.toString()
            ),
            {datoVacaciones -> onClickLambda(datoVacaciones)  }
        )
        binding.rvVacaciones.layoutManager = GridLayoutManager(this, 1)
        binding.rvVacaciones.adapter = miAdaptador

    }

    fun onClickLambda(datoVacaciones: DatosVacaciones) {
        activaControles()
        binding.ivEliminar.isVisible = true
        binding.tvFecha1.text = datoVacaciones.strFecha1
        binding.tvFecha2.text = datoVacaciones.strFecha2
    }

    private fun initSp() {

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
        binding.spAno.setSelection(1)

    }

    private fun activaControles(){
        binding.tvFecha1.isEnabled = true
        binding.tvFecha2.isEnabled = true
        binding.btnGuardar.isEnabled = true
    }

    private fun desactivaControles() {
        binding.tvFecha1.isEnabled = false
        binding.tvFecha2.isEnabled = false
        binding.btnGuardar.isEnabled = false
    }

    private fun limpiaControles() {
        binding.tvFecha1.text = ""
        binding.tvFecha2.text = ""
    }

    private fun callResumenVacaciones(){
        intent = Intent(this, ResVacas::class.java)
        startActivity(intent)
    }
}
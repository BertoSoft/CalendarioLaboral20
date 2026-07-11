package com.example.calendariolaboral20.ui.festivos

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.helper.widget.Carousel
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.data.models.DatosFestivos
import com.example.calendariolaboral20.databinding.ActivityFestivosBinding
import com.example.calendariolaboral20.domain.FuncAux
import com.example.calendariolaboral20.domain.FuncFestivos
import com.example.calendariolaboral20.ui.festivos.adapter.FestivosAdapter
import java.util.Calendar

private lateinit var binding: ActivityFestivosBinding
private var calFecha = Calendar.getInstance()
private lateinit var miAdapter: FestivosAdapter

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

            //
            // Se Ha echo una seleccion
            //
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {

                //
                // Se ha hecho una seleccion, si ivEliminar esta invisible, lanzamos el selector de fechas
                //
                if(
                    p2 != 0 &&
                    !binding.ivEliminar.isVisible &&
                    binding.tvFecha.text == ""
                    ){

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

            //
            // No se ha seleccionado nada
            //
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        //
        // Cambia sp Ano
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
                miAdapter.funRefrescaLista(
                    FuncFestivos().getListaFestivosAnuales(
                        binding.spAno.context,
                        binding.spAno.selectedItem.toString()
                    )
                )
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
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
            }
        }
    }

    fun guardarDatos() {
        var res = false

        val datoFestivo = DatosFestivos(
            binding.tvFecha.text.toString(),
            binding.spFestivo.selectedItem.toString()
        )
        //
        // Si son vacaciones llamo setVacaciones
        //
        if(binding.spFestivo.selectedItem.toString() == "Vacaciones"){

            //
            // Llammamos setVacaciones
            //


        }

        //
        // Es Festivo pero no vacaciones
        //
        else{

            //
            // Si tipo no esta vacio grabo el dato, si esta vacio devuelvo control a Festivos
            //
            if(binding.spFestivo.selectedItem.toString() == ""){
                Toast.makeText(
                    binding.btnGuardar.context,
                    "Debes señalar que tipo de festivo deseas guardar...",
                    Toast.LENGTH_SHORT
                ).show()
                binding.spFestivo.performClick()
            }
            else{
                res = FuncFestivos().setDatoFestivo(
                    this,
                    datoFestivo
                )
            }


        }

        //
        // Segun resultado de res informamos como ha ido la grabacion del dato
        //
        if(res){
            Toast.makeText(
                binding.btnGuardar.context,
                "Fecha Festiva Añadida con Exito...",
                Toast.LENGTH_SHORT
            ).show()
            limpiaControles()
            desactivaControles()
            binding.ivEliminar.isVisible = false

            miAdapter.funRefrescaLista(
                FuncFestivos().getListaFestivosAnuales(
                    binding.btnGuardar.context,
                    binding.spAno.selectedItem.toString()
                )
            )



        }

        //
        // Se produjo un error al grabar el dato de Festivos en la tabla
        //
        else{

        }
    }


    private fun initRv() {

        val tmp = FuncFestivos().getListaFestivosAnuales(
            this,
            binding.spAno.selectedItem.toString()
        )



        miAdapter = FestivosAdapter(
            FuncFestivos().getListaFestivosAnuales(
                this,
                binding.spAno.selectedItem.toString()
            ),
            { datoFestivos -> onClickLambda(datoFestivos) }
        )

        binding.rvFestivos.layoutManager = GridLayoutManager(this, 1)
        binding.rvFestivos.adapter = miAdapter
    }

    fun onClickLambda(datoFestivos: DatosFestivos){

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
        binding.spAno.setSelection(1)

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
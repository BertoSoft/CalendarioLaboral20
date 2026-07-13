package com.example.calendariolaboral20.ui.vacaciones

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.data.models.DatosVacaciones
import com.example.calendariolaboral20.databinding.ActivityVacacionesBinding
import com.example.calendariolaboral20.domain.FuncVacaciones
import com.example.calendariolaboral20.ui.vacaciones.adapter.VacacionesAdapter

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
        //initListeners()
    }

    private fun initListeners() {

        //
        // Si pulsamos ivNuevo
        //
        binding.ivSumar.setOnClickListener {

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
        TODO("Not yet implemented")
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
        miAdaptadorSp.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
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
}
package com.example.calendariolaboral20.ui.festivos

import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.databinding.ActivityFestivosBinding

private lateinit var binding: ActivityFestivosBinding


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
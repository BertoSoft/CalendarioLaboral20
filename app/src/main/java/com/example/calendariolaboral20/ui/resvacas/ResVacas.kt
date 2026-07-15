package com.example.calendariolaboral20.ui.resvacas

import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.data.models.DatosVacaciones
import com.example.calendariolaboral20.databinding.ActivityResVacasBinding
import com.example.calendariolaboral20.domain.FuncVacaciones
import com.example.calendariolaboral20.domain.FuncVacasPendientes
import com.example.calendariolaboral20.ui.resvacas.adapter.ResVacasAdapter

private lateinit var binding: ActivityResVacasBinding
private lateinit var miAdapter: ResVacasAdapter

class ResVacas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResVacasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()


    }

    private fun initUi() {
        initSp()
        initVacasPendientes()
        initRv()
        initVacasDisfrutadas()
        initListeners()
    }

    private fun initListeners() {

        //
        // Si se cambia seleccion de spAno
        //
        binding.spAno.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {

                //
                // Obtenemos la nueva lista
                //
                val listaNew = FuncVacaciones().getListaVacacionesAnuales(
                    binding.spAno.context,
                    binding.spAno.selectedItem.toString()
                )
                miAdapter.refrescaLista(listaNew)

                //
                // Refrescamos las vacaciones pendientes
                //
                initVacasPendientes()

                //
                // Refrescampos las vacaciones disfrutadas
                //
                initVacasDisfrutadas()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        //
        // Si se pulsa ivAtras
        //
        binding.ivVolver.setOnClickListener {
            finish()
        }

    }

    private fun initVacasDisfrutadas() {

        //
        //Debemos de rellenar las vacaciones disfrutadas en el año y las pendientes
        //
        val iDiasDisfrutados = FuncVacaciones().getVacacionesDisfrutadas(this, binding.spAno.selectedItem.toString())
        val iDiasPendientes = (binding.tv12.text.toString().toInt() + 22) - iDiasDisfrutados

        binding.tv32.text = iDiasDisfrutados.toString()
        binding.tv42.text = iDiasPendientes.toString()

    }

    private fun initRv() {
        val listaVacacionesTotales = FuncVacaciones().getListaVacacionesAnuales(
            this,
            binding.spAno.selectedItem.toString()
        )
        miAdapter = ResVacasAdapter(
            listaVacacionesTotales,
            {datoVacaciones -> onClickLambda(datoVacaciones)}
        )
        binding.rvVacaciones.layoutManager = GridLayoutManager(this, 1)
        binding.rvVacaciones.adapter = miAdapter
    }

    private fun onClickLambda(datoVacaciones: DatosVacaciones){

    }

    private fun initVacasPendientes() {
        val listaVacasPendientes = FuncVacasPendientes().getListaVacasPendientes( this )

        var i = 0
        binding.tv12.text = "0"
        while (i < listaVacasPendientes.size){
            var iAno = binding.spAno.selectedItem.toString().toInt()
            iAno--

            if(listaVacasPendientes[i].strYear == iAno.toString()){
                binding.tv12.text = listaVacasPendientes[i].strDias
                i = listaVacasPendientes.size
            }
            i++
        }

    }

    private fun initSp() {
        var array = arrayOf<String>()
        var iAnoActual = Calendar.getInstance().get(Calendar.YEAR)

        iAnoActual++
        while (iAnoActual >= 2022){
            array += iAnoActual.toString()
            iAnoActual--
        }

        val adaptadorSpinner = ArrayAdapter<String>(
            this,
            R.layout.item_sp,
            array
        )
        adaptadorSpinner.setDropDownViewResource(R.layout.item_sp)
        binding.spAno.adapter = adaptadorSpinner
        binding.spAno.setSelection(1)

    }

}
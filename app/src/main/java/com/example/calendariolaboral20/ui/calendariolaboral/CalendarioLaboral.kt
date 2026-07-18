package com.example.calendariolaboral20.ui.calendariolaboral

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.calendariolaboral20.R
import com.example.calendariolaboral20.data.models.DatosCalendario
import com.example.calendariolaboral20.databinding.ActivityCalendarioBinding
import com.example.calendariolaboral20.domain.FuncAux
import com.example.calendariolaboral20.domain.FuncCalendario
import java.util.Calendar

private lateinit var binding: ActivityCalendarioBinding
private var calFecha = Calendar.getInstance()

class CalendarioLaboral : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarioBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initUi()

    }

    private fun initUi() {
        initSps()
        initCalendario()
        initListeners()
    }

    private fun initListeners() {

        //
        // Si spMeses cambia seleccion
        //
        binding.spMes.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                dibujaCalendario(
                    binding.spMes.selectedItem.toString(),
                    binding.spAno.selectedItem.toString()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

                //
                //No hay seleccion
                //
            }
        }

        //
        // Si spAnos Cambia de seleccion
        //
        binding.spAno.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                dibujaCalendario(
                    binding.spMes.selectedItem.toString(),
                    binding.spAno.selectedItem.toString()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

                //
                // No hay seleccion
                //
            }

        }
    }

    private fun initCalendario() {
        dibujaCalendario(
            binding.spMes.selectedItem.toString(),
            binding.spAno.selectedItem.toString()
        )

    }

    private fun dibujaCalendario(strMes: String, strAno: String) {
        val iMes = FuncAux().iMesTostrMes(strMes)
        val iAno = strAno.toInt()

        calFecha.set(iAno, iMes - 1, 1)

        //
        // Miramos donde empieza la primera fila del calendario
        //
        val strFechaLarga = FuncAux().strFechaLargaFromCalendar(calFecha)
        val strDiaSemana = strFechaLarga.substring(0, 4)
        var iColumnaPrimeraLinea = -1
        when (strDiaSemana) {
            "Mond" -> iColumnaPrimeraLinea = 1
            "lune" -> iColumnaPrimeraLinea = 1
            "Tues" -> iColumnaPrimeraLinea = 2
            "mart" -> iColumnaPrimeraLinea = 2
            "Wedn" -> iColumnaPrimeraLinea = 3
            "miér" -> iColumnaPrimeraLinea = 3
            "Thur" -> iColumnaPrimeraLinea = 4
            "juev" -> iColumnaPrimeraLinea = 4
            "Frid" -> iColumnaPrimeraLinea = 5
            "vier" -> iColumnaPrimeraLinea = 5
            "Satu" -> iColumnaPrimeraLinea = 6
            "sába" -> iColumnaPrimeraLinea = 6
            "Sund" -> iColumnaPrimeraLinea = 7
            "domi" -> iColumnaPrimeraLinea = 7
        }

        //
        // Ahora Creamos la lista del mes
        //
        var listaMes = mutableListOf<DatosCalendario>()

        //
        // hasta la primera columna de la primera fila, rellenamos en vacio
        //
        var iFila = 1
        var iColumna = 1
        while (iColumna < iColumnaPrimeraLinea){
            listaMes.add(
                DatosCalendario(
                    "",
                    iFila,
                    iColumna,
                    R.color.transparente,
                    R.color.transparente
                )
            )
            iColumna++
        }

        //
        // Ahora sin variar los valores de iFila e iColumna, recorremos los dias del mes
        //
        while (calFecha.get(Calendar.MONTH) == iMes - 1) {
            if(iColumna > 7){
                iColumna = 1
                iFila++
            }

            //
            // Añadimos cada dia y sus propiedades a la lista
            //
            val backColorDia = FuncCalendario().getBackColorDia( this, FuncAux().strFechaCortaToCalendar(calFecha))
            val foreColorDia = FuncCalendario().getForeColorDia(FuncAux().strFechaCortaToCalendar(calFecha))

            listaMes.add(
                DatosCalendario(
                    calFecha.get(Calendar.DAY_OF_MONTH).toString(),
                    iFila,
                    iColumna,
                    backColorDia,
                    foreColorDia
                )
            )
            iColumna++
            calFecha.add(Calendar.DAY_OF_YEAR, 1)
        }

        //
        // Ahora rellenamos en vacio hasta 42 6 Filas por 7 Columnas
        //
        while (listaMes.size < 42){
            if(iColumna > 7){
                iColumna = 1
                iFila++
            }
            listaMes.add(
                DatosCalendario(
                    "",
                    iFila,
                    iColumna,
                    R.color.transparente,
                    R.color.transparente
                )
            )
            iColumna++
        }

        //
        // Primera Fila
        //
        binding.tv11.text = listaMes[0].strDia
        binding.tv11.setBackgroundColor(getColor(listaMes[0].iBackColor))
        binding.tv11.setTextColor(getColor(listaMes[0].iForeColor))

        binding.tv12.text = listaMes[1].strDia
        binding.tv12.setBackgroundColor(getColor(listaMes[1].iBackColor))
        binding.tv12.setTextColor(getColor(listaMes[1].iForeColor))

        binding.tv13.text = listaMes[2].strDia
        binding.tv13.setBackgroundColor(getColor(listaMes[2].iBackColor))
        binding.tv13.setTextColor(getColor(listaMes[2].iForeColor))

        binding.tv14.text = listaMes[3].strDia
        binding.tv14.setBackgroundColor(getColor(listaMes[3].iBackColor))
        binding.tv14.setTextColor(getColor(listaMes[3].iForeColor))

        binding.tv15.text = listaMes[4].strDia
        binding.tv15.setBackgroundColor(getColor(listaMes[4].iBackColor))
        binding.tv15.setTextColor(getColor(listaMes[4].iForeColor))

        binding.tv16.text = listaMes[5].strDia
        binding.tv16.setBackgroundColor(getColor(listaMes[5].iBackColor))
        binding.tv16.setTextColor(getColor(listaMes[5].iForeColor))

        binding.tv17.text = listaMes[6].strDia
        binding.tv17.setBackgroundColor(getColor(listaMes[6].iBackColor))
        binding.tv17.setTextColor(getColor(listaMes[6].iForeColor))


        //
        // Segunda Fila
        //
        binding.tv21.text = listaMes[7].strDia
        binding.tv21.setBackgroundColor(getColor(listaMes[7].iBackColor))
        binding.tv21.setTextColor(getColor(listaMes[7].iForeColor))

        binding.tv22.text = listaMes[8].strDia
        binding.tv22.setBackgroundColor(getColor(listaMes[8].iBackColor))
        binding.tv22.setTextColor(getColor(listaMes[8].iForeColor))

        binding.tv23.text = listaMes[9].strDia
        binding.tv23.setBackgroundColor(getColor(listaMes[9].iBackColor))
        binding.tv23.setTextColor(getColor(listaMes[9].iForeColor))

        binding.tv24.text = listaMes[10].strDia
        binding.tv24.setBackgroundColor(getColor(listaMes[10].iBackColor))
        binding.tv24.setTextColor(getColor(listaMes[10].iForeColor))

        binding.tv25.text = listaMes[11].strDia
        binding.tv25.setBackgroundColor(getColor(listaMes[11].iBackColor))
        binding.tv25.setTextColor(getColor(listaMes[11].iForeColor))

        binding.tv26.text = listaMes[12].strDia
        binding.tv26.setBackgroundColor(getColor(listaMes[12].iBackColor))
        binding.tv26.setTextColor(getColor(listaMes[12].iForeColor))

        binding.tv27.text = listaMes[13].strDia
        binding.tv27.setBackgroundColor(getColor(listaMes[13].iBackColor))
        binding.tv27.setTextColor(getColor(listaMes[13].iForeColor))



        //
        // Tercera Fila
        //
        binding.tv31.text = listaMes[14].strDia
        binding.tv31.setBackgroundColor(getColor(listaMes[14].iBackColor))
        binding.tv31.setTextColor(getColor(listaMes[14].iForeColor))

        binding.tv32.text = listaMes[15].strDia
        binding.tv32.setBackgroundColor(getColor(listaMes[15].iBackColor))
        binding.tv32.setTextColor(getColor(listaMes[15].iForeColor))

        binding.tv33.text = listaMes[16].strDia
        binding.tv33.setBackgroundColor(getColor(listaMes[16].iBackColor))
        binding.tv33.setTextColor(getColor(listaMes[16].iForeColor))

        binding.tv34.text = listaMes[17].strDia
        binding.tv34.setBackgroundColor(getColor(listaMes[17].iBackColor))
        binding.tv34.setTextColor(getColor(listaMes[17].iForeColor))

        binding.tv35.text = listaMes[18].strDia
        binding.tv35.setBackgroundColor(getColor(listaMes[18].iBackColor))
        binding.tv35.setTextColor(getColor(listaMes[18].iForeColor))

        binding.tv36.text = listaMes[19].strDia
        binding.tv36.setBackgroundColor(getColor(listaMes[19].iBackColor))
        binding.tv36.setTextColor(getColor(listaMes[19].iForeColor))

        binding.tv37.text = listaMes[20].strDia
        binding.tv37.setBackgroundColor(getColor(listaMes[20].iBackColor))
        binding.tv37.setTextColor(getColor(listaMes[20].iForeColor))


        //
        // Cuarta Fila
        //
        binding.tv41.text = listaMes[21].strDia
        binding.tv41.setBackgroundColor(getColor(listaMes[21].iBackColor))
        binding.tv41.setTextColor(getColor(listaMes[21].iForeColor))

        binding.tv42.text = listaMes[22].strDia
        binding.tv42.setBackgroundColor(getColor(listaMes[22].iBackColor))
        binding.tv42.setTextColor(getColor(listaMes[22].iForeColor))

        binding.tv43.text = listaMes[23].strDia
        binding.tv43.setBackgroundColor(getColor(listaMes[23].iBackColor))
        binding.tv43.setTextColor(getColor(listaMes[23].iForeColor))

        binding.tv44.text = listaMes[24].strDia
        binding.tv44.setBackgroundColor(getColor(listaMes[24].iBackColor))
        binding.tv44.setTextColor(getColor(listaMes[24].iForeColor))

        binding.tv45.text = listaMes[25].strDia
        binding.tv45.setBackgroundColor(getColor(listaMes[25].iBackColor))
        binding.tv45.setTextColor(getColor(listaMes[25].iForeColor))

        binding.tv46.text = listaMes[26].strDia
        binding.tv46.setBackgroundColor(getColor(listaMes[26].iBackColor))
        binding.tv46.setTextColor(getColor(listaMes[26].iForeColor))

        binding.tv47.text = listaMes[27].strDia
        binding.tv47.setBackgroundColor(getColor(listaMes[27].iBackColor))
        binding.tv47.setTextColor(getColor(listaMes[27].iForeColor))



        //
        // Qinta Fila
        //
        binding.tv51.text = listaMes[28].strDia
        binding.tv51.setBackgroundColor(getColor(listaMes[28].iBackColor))
        binding.tv51.setTextColor(getColor(listaMes[28].iForeColor))

        binding.tv52.text = listaMes[29].strDia
        binding.tv52.setBackgroundColor(getColor(listaMes[29].iBackColor))
        binding.tv52.setTextColor(getColor(listaMes[29].iForeColor))

        binding.tv53.text = listaMes[30].strDia
        binding.tv53.setBackgroundColor(getColor(listaMes[30].iBackColor))
        binding.tv53.setTextColor(getColor(listaMes[30].iForeColor))

        binding.tv54.text = listaMes[31].strDia
        binding.tv54.setBackgroundColor(getColor(listaMes[31].iBackColor))
        binding.tv54.setTextColor(getColor(listaMes[31].iForeColor))

        binding.tv55.text = listaMes[32].strDia
        binding.tv55.setBackgroundColor(getColor(listaMes[32].iBackColor))
        binding.tv55.setTextColor(getColor(listaMes[32].iForeColor))

        binding.tv56.text = listaMes[33].strDia
        binding.tv56.setBackgroundColor(getColor(listaMes[33].iBackColor))
        binding.tv56.setTextColor(getColor(listaMes[33].iForeColor))

        binding.tv57.text = listaMes[34].strDia
        binding.tv57.setBackgroundColor(getColor(listaMes[34].iBackColor))
        binding.tv57.setTextColor(getColor(listaMes[34].iForeColor))



        //
        // Sexta Fila
        //
        binding.tv61.text = listaMes[35].strDia
        binding.tv61.setBackgroundColor(getColor(listaMes[35].iBackColor))
        binding.tv61.setTextColor(getColor(listaMes[35].iForeColor))

        binding.tv62.text = listaMes[36].strDia
        binding.tv62.setBackgroundColor(getColor(listaMes[36].iBackColor))
        binding.tv62.setTextColor(getColor(listaMes[36].iForeColor))

        binding.tv63.text = listaMes[37].strDia
        binding.tv63.setBackgroundColor(getColor(listaMes[37].iBackColor))
        binding.tv63.setTextColor(getColor(listaMes[37].iForeColor))

        binding.tv64.text = listaMes[38].strDia
        binding.tv64.setBackgroundColor(getColor(listaMes[38].iBackColor))
        binding.tv64.setTextColor(getColor(listaMes[38].iForeColor))

        binding.tv65.text = listaMes[39].strDia
        binding.tv65.setBackgroundColor(getColor(listaMes[39].iBackColor))
        binding.tv65.setTextColor(getColor(listaMes[39].iForeColor))

        binding.tv66.text = listaMes[40].strDia
        binding.tv66.setBackgroundColor(getColor(listaMes[40].iBackColor))
        binding.tv66.setTextColor(getColor(listaMes[40].iForeColor))

        binding.tv67.text = listaMes[41].strDia
        binding.tv67.setBackgroundColor(getColor(listaMes[41].iBackColor))
        binding.tv67.setTextColor(getColor(listaMes[41].iForeColor))


    }

    private fun initSps() {

        //
        // Sp Meses
        //
        val listaMeses = listOf(
            "Enero",
            "Febrero",
            "Marzo",
            "Abril",
            "Mayo",
            "Junio",
            "Julio",
            "Agosto",
            "Septiembre",
            "Octubre",
            "Noviembre",
            "Diciembre"
        )
        val adaptadorSpinnerMeses = ArrayAdapter<String>(
            this,
            R.layout.item_sp,
            listaMeses
        )
        adaptadorSpinnerMeses.setDropDownViewResource(R.layout.item_sp)
        binding.spMes.adapter = adaptadorSpinnerMeses
        val iMes = calFecha.get(Calendar.MONTH)
        binding.spMes.setSelection(iMes)


        //
        // Sp Anos
        //
        var listaAnos = arrayOf<String>()
        var iAnoActual = calFecha.get(Calendar.YEAR)

        iAnoActual++
        while (iAnoActual >= 2022) {
            listaAnos += iAnoActual.toString()
            iAnoActual--
        }

        val adaptadorSpinnerAnos = ArrayAdapter<String>(
            this,
            R.layout.item_sp,
            listaAnos
        )
        adaptadorSpinnerAnos.setDropDownViewResource(R.layout.item_sp)
        binding.spAno.adapter = adaptadorSpinnerAnos
        binding.spAno.setSelection(1)

    }

}
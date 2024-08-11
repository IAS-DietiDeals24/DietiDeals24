package com.iasdietideals24.dietideals24.controller

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.text.DateFormat
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Bundle
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.model.ModelControllerCreazioneProfilo
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import java.util.Locale

class ControllerCreazioneProfiloFase1 : Controller(R.layout.creazioneprofilofase1) {
    private val viewModel: ModelControllerCreazioneProfilo = ModelControllerCreazioneProfilo()

    private lateinit var layout: LinearLayout
    private lateinit var pulsanteIndietro: ImageButton
    private lateinit var campoNomeUtente: TextInputLayout
    private lateinit var nomeUtente: TextInputEditText
    private lateinit var campoNome: TextInputLayout
    private lateinit var nome: TextInputEditText
    private lateinit var campoCognome: TextInputLayout
    private lateinit var cognome: TextInputEditText
    private lateinit var campoDataNascita: TextInputLayout
    private lateinit var dataNascita: TextInputEditText
    private lateinit var pulsanteAvanti: MaterialButton

    @UIBuilder
    override fun trovaElementiInterfaccia() {
        layout = findViewById(R.id.creazioneProfiloFase1_linearLayout)
        pulsanteIndietro = findViewById(R.id.creazioneProfiloFase1_pulsanteIndietro)
        campoNomeUtente = findViewById(R.id.creazioneProfiloFase1_campoNomeUtente)
        nomeUtente = findViewById(R.id.creazioneProfiloFase1_nomeUtente)
        campoNome = findViewById(R.id.creazioneProfiloFase1_campoNome)
        nome = findViewById(R.id.creazioneProfiloFase1_nome)
        campoCognome = findViewById(R.id.creazioneProfiloFase1_campoCognome)
        cognome = findViewById(R.id.creazioneProfiloFase1_cognome)
        campoDataNascita = findViewById(R.id.creazioneProfiloFase1_campoDataNascita)
        dataNascita = findViewById(R.id.creazioneProfiloFase1_dataNascita)
        pulsanteAvanti = findViewById(R.id.creazioneProfiloFase1_pulsanteAvanti)
    }

    @UIBuilder
    override fun impostaEventiClick() {
        pulsanteIndietro.setOnClickListener { clickIndietro() }
        pulsanteAvanti.setOnClickListener { clickAvanti() }
        campoDataNascita.setEndIconOnClickListener { clickDataNascita() }
    }


    @EventHandler
    private fun clickIndietro() {
        cambiaAttivita(ControllerRegistrazione::class.java)
    }

    @EventHandler
    private fun clickAvanti() {
        viewModel.nomeUtente = estraiTestoDaElemento(nomeUtente)
        viewModel.nome = estraiTestoDaElemento(nome)
        viewModel.cognome = estraiTestoDaElemento(cognome)
        viewModel.dataNascita = estraiDataDaElemento(dataNascita)

        try {
            viewModel.validate()

            cambiaAttivita(ControllerCreazioneProfiloFase2::class.java)
        } catch (eccezione: EccezioneCampiNonCompilati) {
            rimuoviMessaggioErrore(layout, 2)
            erroreCampo(
                layout,
                getString(R.string.registrazione_erroreCampiObbligatoriNonCompilati),
                4,
                campoNomeUtente, campoNome, campoCognome, campoDataNascita
            )
        }
    }

    @EventHandler
    private fun clickDataNascita() {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }

    //region Fragment
    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dialog = DatePickerDialog(requireContext(), this, year, month, day)
            dialog.datePicker.maxDate = c.timeInMillis

            return dialog
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            val activity: ControllerCreazioneProfiloFase1 =
                activity as ControllerCreazioneProfiloFase1

            val date = GregorianCalendar(year, month, day).time
            val formatter = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
            val formattedDate = formatter.format(date)
            activity.dataNascita.setText(formattedDate)
        }
    }
    //endregion Fragment
}
package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.icu.util.Calendar
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.toLocalDate
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.classes.toMillis
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentBackButton
import java.time.LocalDate


class ControllerCreazioneProfiloFase1 : Controller(R.layout.creazioneprofilofase1) {

    private lateinit var viewModel: ModelRegistrazione

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

    private var listener: OnFragmentBackButton? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnFragmentBackButton) {
            listener = requireContext() as OnFragmentBackButton
        }
    }

    override fun onDetach() {
        super.onDetach()

        listener = null
    }

    @UIBuilder
    override fun trovaElementiInterfaccia() {
        layout = fragmentView.findViewById(R.id.creazioneProfiloFase1_linearLayout)
        pulsanteIndietro = fragmentView.findViewById(R.id.creazioneProfiloFase1_pulsanteIndietro)
        campoNomeUtente = fragmentView.findViewById(R.id.creazioneProfiloFase1_campoNomeUtente)
        nomeUtente = fragmentView.findViewById(R.id.creazioneProfiloFase1_nomeUtente)
        campoNome = fragmentView.findViewById(R.id.creazioneProfiloFase1_campoNome)
        nome = fragmentView.findViewById(R.id.creazioneProfiloFase1_nome)
        campoCognome = fragmentView.findViewById(R.id.creazioneProfiloFase1_campoCognome)
        cognome = fragmentView.findViewById(R.id.creazioneProfiloFase1_cognome)
        campoDataNascita = fragmentView.findViewById(R.id.creazioneProfiloFase1_campoDataNascita)
        dataNascita = fragmentView.findViewById(R.id.creazioneProfiloFase1_dataNascita)
        pulsanteAvanti = fragmentView.findViewById(R.id.creazioneProfiloFase1_pulsanteAvanti)
    }

    @UIBuilder
    override fun impostaEventiClick() {
        pulsanteIndietro.setOnClickListener { clickIndietro() }
        pulsanteAvanti.setOnClickListener { clickAvanti() }
        campoDataNascita.setEndIconOnClickListener { clickDataNascita() }
    }

    @UIBuilder
    override fun impostaEventiDiCambiamentoCampi() {
        nomeUtente.addTextChangedListener {
            rimuoviErroreCampo(campoNomeUtente)
            rimuoviErroreCampo(campoNome)
            rimuoviErroreCampo(campoCognome)
        }

        nome.addTextChangedListener {
            rimuoviErroreCampo(campoNomeUtente)
            rimuoviErroreCampo(campoNome)
            rimuoviErroreCampo(campoCognome)
        }

        cognome.addTextChangedListener {
            rimuoviErroreCampo(campoNomeUtente)
            rimuoviErroreCampo(campoNome)
            rimuoviErroreCampo(campoCognome)
        }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity).get(ModelRegistrazione::class)
    }

    @UIBuilder
    override fun impostaOsservatori() {
        val nomeUtenteObserver = Observer<String> { newNomeUtente ->
            nomeUtente.setText(newNomeUtente)
        }
        viewModel.nomeUtente.observe(viewLifecycleOwner, nomeUtenteObserver)

        val nomeObserver = Observer<String> { newNome ->
            nome.setText(newNome)
        }
        viewModel.nome.observe(viewLifecycleOwner, nomeObserver)

        val cognomeObserver = Observer<String> { newCognome ->
            cognome.setText(newCognome)
        }
        viewModel.cognome.observe(viewLifecycleOwner, cognomeObserver)

        val dataNascitaObserver = Observer<LocalDate> { newData ->
            if (newData != LocalDate.MIN)
                dataNascita.setText(newData.toLocalStringShort())
            else
                dataNascita.setText("")
        }
        viewModel.dataNascita.observe(viewLifecycleOwner, dataNascitaObserver)
    }


    @EventHandler
    private fun clickIndietro() {
        val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            LoginManager.getInstance().logOut()
        }

        listener?.onFragmentBackButton()
    }

    @EventHandler
    private fun clickAvanti() {
        viewModel.nomeUtente.value = estraiTestoDaElemento(nomeUtente)
        viewModel.nome.value = estraiTestoDaElemento(nome)
        viewModel.cognome.value = estraiTestoDaElemento(cognome)

        try {
            viewModel.validateProfile()

            navController.navigate(R.id.action_controllerCreazioneProfiloFase1_to_controllerCreazioneProfiloFase2)
        } catch (eccezione: EccezioneCampiNonCompilati) {
            erroreCampo(
                R.string.registrazione_erroreCampiObbligatoriNonCompilati,
                campoNomeUtente,
                campoNome,
                campoCognome,
                campoDataNascita
            )
        }
    }

    @EventHandler
    private fun clickDataNascita() {
        val calendarConstraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .setFirstDayOfWeek(Calendar.MONDAY)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.creazioneProfiloFase1_titoloPopupData)
            .setCalendarConstraints(calendarConstraints.build())
            .setSelection(
                if (viewModel.dataNascita.value == LocalDate.MIN)
                    MaterialDatePicker.todayInUtcMilliseconds()
                else
                    viewModel.dataNascita.value?.toMillis()
            )
            .setTheme(R.style.DatePicker)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            if (datePicker.selection != null) {
                val localDate = datePicker.selection!!.toLocalDate()

                viewModel.dataNascita.value = localDate

                dataNascita.setText(localDate.toLocalStringShort())
            }
        }

        datePicker.show(requireActivity().supportFragmentManager, "datePicker")
    }
}
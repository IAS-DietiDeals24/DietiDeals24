package com.iasdietideals24.dietideals24.controller

import android.annotation.SuppressLint
import android.content.Context
import android.icu.util.Calendar
import android.view.MotionEvent
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.Creazioneprofilofase1Binding
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneNomeUtenteUsato
import com.iasdietideals24.dietideals24.utilities.kscripts.OnBackButton
import com.iasdietideals24.dietideals24.utilities.kscripts.OnNextStep
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalDate
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.kscripts.toMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.time.LocalDate

class ControllerCreazioneProfiloFase1 : Controller<Creazioneprofilofase1Binding>() {

    // ViewModel
    private val viewModel: ModelRegistrazione by activityViewModel()

    // Listeners
    private var listenerBackButton: OnBackButton? = null
    private var listenerNextStep: OnNextStep? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnBackButton) {
            listenerBackButton = requireContext() as OnBackButton
        }
        if (requireContext() is OnNextStep) {
            listenerNextStep = requireContext() as OnNextStep
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerBackButton = null
        listenerNextStep = null
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.creazioneProfiloFase1PulsanteIndietro.setOnClickListener { clickIndietro() }
        binding.creazioneProfiloFase1PulsanteAvanti.setOnClickListener { clickAvanti() }
        binding.creazioneProfiloFase1CampoDataNascita.setStartIconOnClickListener { clickDataNascita() }
    }

    @SuppressLint("ClickableViewAccessibility")
    @UIBuilder
    override fun impostaEventiDiCambiamentoCampi() {
        binding.creazioneProfiloFase1NomeUtente.addTextChangedListener {
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoNomeUtente)
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoNome)
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoCognome)
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoDataNascita)
        }

        binding.creazioneProfiloFase1Nome.addTextChangedListener {
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoNomeUtente)
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoNome)
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoCognome)
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoDataNascita)
        }

        binding.creazioneProfiloFase1Cognome.addTextChangedListener {
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoNomeUtente)
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoNome)
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoCognome)
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoDataNascita)
        }

        binding.creazioneProfiloFase1DataNascita.addTextChangedListener {
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoNomeUtente)
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoNome)
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoCognome)
            rimuoviErroreCampo(binding.creazioneProfiloFase1CampoDataNascita)
        }

        binding.creazioneProfiloFase1DataNascita.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.clearFocus()

                clickDataNascita()

                return@OnTouchListener true
            }
            false
        })
    }

    @UIBuilder
    override fun impostaOsservatori() {
        val nomeUtenteObserver = Observer<String> { newNomeUtente ->
            binding.creazioneProfiloFase1NomeUtente.setText(newNomeUtente)
        }
        viewModel.nomeUtente.observe(viewLifecycleOwner, nomeUtenteObserver)

        val nomeObserver = Observer<String> { newNome ->
            binding.creazioneProfiloFase1Nome.setText(newNome)
        }
        viewModel.nome.observe(viewLifecycleOwner, nomeObserver)

        val cognomeObserver = Observer<String> { newCognome ->
            binding.creazioneProfiloFase1Cognome.setText(newCognome)
        }
        viewModel.cognome.observe(viewLifecycleOwner, cognomeObserver)

        val dataNascitaObserver = Observer<LocalDate> { newData ->
            if (newData != LocalDate.MIN)
                binding.creazioneProfiloFase1DataNascita.setText(newData.toLocalStringShort())
            else
                binding.creazioneProfiloFase1DataNascita.setText("")
        }
        viewModel.dataNascita.observe(viewLifecycleOwner, dataNascitaObserver)
    }

    @EventHandler
    private fun clickIndietro() {
        MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(R.string.modifica_titoloConfermaIndietro)
            .setMessage(R.string.modifica_testoConfermaIndietro)
            .setPositiveButton(R.string.ok) { _, _ ->
                val accessToken: AccessToken? = AccessToken.getCurrentAccessToken()
                if (accessToken != null && !accessToken.isExpired) {
                    LoginManager.getInstance().logOut()
                }

                listenerBackButton?.onBackButton()
            }
            .setNegativeButton(R.string.annulla) { _, _ -> }
            .show()
    }

    @EventHandler
    private fun clickAvanti() {
        viewModel.nomeUtente.value =
            estraiTestoDaElemento(binding.creazioneProfiloFase1NomeUtente).replace(" ", "")
        viewModel.nome.value = estraiTestoDaElemento(binding.creazioneProfiloFase1Nome)
        viewModel.cognome.value = estraiTestoDaElemento(binding.creazioneProfiloFase1Cognome)

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) { viewModel.validateProfile() }

                listenerNextStep?.onNextStep(ControllerCreazioneProfiloFase1::class)
            } catch (_: EccezioneCampiNonCompilati) {
                erroreCampo(
                    R.string.registrazione_erroreCampiObbligatoriNonCompilati,
                    binding.creazioneProfiloFase1CampoNomeUtente,
                    binding.creazioneProfiloFase1CampoNome,
                    binding.creazioneProfiloFase1CampoCognome,
                    binding.creazioneProfiloFase1CampoDataNascita
                )
            } catch (_: EccezioneNomeUtenteUsato) {
                erroreCampo(
                    R.string.registrazione_erroreNomeUtenteGiàUsato,
                    binding.creazioneProfiloFase1CampoNomeUtente
                )
            } catch (_: Exception) {
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.arancione, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
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

                binding.creazioneProfiloFase1DataNascita.setText(localDate.toLocalStringShort())
            }
        }

        datePicker.show(requireActivity().supportFragmentManager, "datePicker")
    }
}
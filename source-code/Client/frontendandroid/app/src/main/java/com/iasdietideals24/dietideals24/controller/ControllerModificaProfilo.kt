package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.icu.util.Calendar
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.ModificaprofiloBinding
import com.iasdietideals24.dietideals24.model.ModelProfilo
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.data.Profilo
import com.iasdietideals24.dietideals24.utilities.dto.ProfiloDto
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToProfile
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalDate
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.kscripts.toMillis
import com.iasdietideals24.dietideals24.utilities.repositories.ProfiloRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.time.LocalDate

class ControllerModificaProfilo : Controller<ModificaprofiloBinding>() {

    // ViewModel
    private val viewModel: ModelProfilo by activityViewModel()

    // Repositories
    private val repositoryProfilo: ProfiloRepository by inject()

    private lateinit var selectPhoto: ActivityResultLauncher<PickVisualMediaRequest>

    // Listeners
    private var listenerProfile: OnGoToProfile? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnGoToProfile) {
            listenerProfile = requireContext() as OnGoToProfile
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerProfile = null
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.modificaProfiloPulsanteIndietro.setOnClickListener { clickIndietro() }

        binding.modificaProfiloPulsanteConferma.setOnClickListener { clickConferma() }

        binding.modificaProfiloPulsante.setOnClickListener { clickPulsanteImmagine() }

        binding.modificaProfiloFacebook.setOnClickListener { clickFacebook() }

        binding.modificaProfiloInstagram.setOnClickListener { clickInstagram() }

        binding.modificaProfiloGithub.setOnClickListener { clickGitHub() }

        binding.modificaProfiloX.setOnClickListener { clickX() }

        binding.modificaProfiloCampoDataNascita.setStartIconOnClickListener { clickDataNascita() }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        selectPhoto =
            registerForActivityResult(PickVisualMedia()) { uri: Uri? ->
                viewModel.immagineProfilo.value =
                    com.iasdietideals24.dietideals24.utilities.tools.ImageHandler.encodeImage(
                        uri,
                        fragmentContext
                    )
            }
    }

    @UIBuilder
    override fun impostaOsservatori() {
        val nomeUtenteObserver = Observer<String> { newNomeUtente ->
            binding.modificaProfiloNomeUtente.text = newNomeUtente
        }
        viewModel.nomeUtente.observe(viewLifecycleOwner, nomeUtenteObserver)

        val immagineObserver = Observer { newByteArray: ByteArray? ->
            when {
                newByteArray == null || newByteArray.isEmpty() -> {
                    binding.modificaProfiloImmagineUtente.scaleType =
                        ImageView.ScaleType.CENTER_INSIDE
                    binding.modificaProfiloImmagineUtente.setImageResource(R.drawable.icona_profilo)
                }

                else -> {
                    binding.modificaProfiloImmagineUtente.load(newByteArray) {
                        crossfade(true)
                    }
                    binding.modificaProfiloImmagineUtente.scaleType =
                        ImageView.ScaleType.CENTER_CROP
                }
            }
        }
        viewModel.immagineProfilo.observe(viewLifecycleOwner, immagineObserver)

        val nomeObserver = Observer<String> { newNome ->
            binding.modificaProfiloNome.setText(newNome)
        }
        viewModel.nome.observe(viewLifecycleOwner, nomeObserver)

        val cognomeObserver = Observer<String> { newCognome ->
            binding.modificaProfiloCognome.setText(newCognome)
        }
        viewModel.cognome.observe(viewLifecycleOwner, cognomeObserver)

        val emailObserver = Observer<String> { newEmail ->
            binding.modificaProfiloEmail.text = newEmail
        }
        viewModel.email.observe(viewLifecycleOwner, emailObserver)

        val dataNascitaObserver = Observer<LocalDate> { newData ->
            binding.modificaProfiloDataNascita.setText(newData.toLocalStringShort())
        }
        viewModel.dataNascita.observe(viewLifecycleOwner, dataNascitaObserver)

        val genereObserver = Observer<String> { newGenere ->
            binding.modificaProfiloGenere.setText(newGenere)
        }
        viewModel.genere.observe(viewLifecycleOwner, genereObserver)

        val areaGeograficaObserver = Observer<String> { newAreaGeografica ->
            binding.modificaProfiloAreaGeografica.setText(newAreaGeografica)
        }
        viewModel.areaGeografica.observe(viewLifecycleOwner, areaGeograficaObserver)

        val biografiaObserver = Observer<String> { newBiografia ->
            binding.modificaProfiloBiografia.setText(newBiografia)
        }
        viewModel.biografia.observe(viewLifecycleOwner, biografiaObserver)

        val linkPersonaleObserver = Observer<String> { newLinkPersonale ->
            binding.modificaProfiloLinkPersonale.setText(newLinkPersonale)
        }
        viewModel.linkPersonale.observe(viewLifecycleOwner, linkPersonaleObserver)
    }

    @EventHandler
    private fun clickIndietro() {
        MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(R.string.modifica_titoloConfermaIndietro)
            .setMessage(R.string.modifica_testoConfermaIndietro)
            .setPositiveButton(R.string.ok) { _, _ ->
                viewModel.clear()
                listenerProfile?.onGoToProfile(CurrentUser.id, ControllerModificaProfilo::class)
            }
            .setNegativeButton(R.string.annulla) { _, _ -> }
            .show()
    }

    @EventHandler
    private fun clickConferma() {
        viewModel.nome.value = estraiTestoDaElemento(binding.modificaProfiloNome)
        viewModel.cognome.value = estraiTestoDaElemento(binding.modificaProfiloCognome)
        viewModel.genere.value = estraiTestoDaElemento(binding.modificaProfiloGenere)
        viewModel.areaGeografica.value =
            estraiTestoDaElemento(binding.modificaProfiloAreaGeografica)
        viewModel.biografia.value = estraiTestoDaElemento(binding.modificaProfiloBiografia)
        viewModel.linkPersonale.value = estraiTestoDaElemento(binding.modificaProfiloLinkPersonale)

        lifecycleScope.launch {
            try {
                viewModel.validate()

                val returned: Profilo =
                    withContext(Dispatchers.IO) { aggiornaProfilo().toProfilo() }

                when (returned.nomeUtente) {
                    "" -> Snackbar.make(
                        fragmentView,
                        R.string.modificaProfilo_fallimentoModifica,
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(resources.getColor(R.color.blu, null))
                        .setTextColor(resources.getColor(R.color.grigio, null))
                        .show()

                    else -> {
                        Snackbar.make(
                            fragmentView,
                            R.string.modificaProfilo_successoModifica,
                            Snackbar.LENGTH_SHORT
                        )
                            .setBackgroundTint(resources.getColor(R.color.blu, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()

                        withContext(Dispatchers.IO) {
                            logger.scriviLog("Profile edited successfully")
                        }

                        listenerProfile?.onGoToProfile(
                            CurrentUser.id,
                            ControllerModificaProfilo::class
                        )
                    }
                }
            } catch (_: EccezioneCampiNonCompilati) {
                erroreCampo(
                    R.string.registrazione_erroreCampiObbligatoriNonCompilati,
                    binding.modificaProfiloCampoNome,
                    binding.modificaProfiloCampoCognome,
                    binding.modificaProfiloCampoDataNascita,
                    binding.modificaProfiloCampoGenere
                )
            } catch (_: Exception) {
                Snackbar.make(
                    fragmentView,
                    R.string.apiError,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    private suspend fun aggiornaProfilo(): ProfiloDto {
        return repositoryProfilo.aggiornaProfilo(
            viewModel.toProfilo(),
            viewModel.nomeUtente.value!!
        )
    }

    @EventHandler
    private fun clickPulsanteImmagine() {
        selectPhoto.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }

    @EventHandler
    private fun clickFacebook() {
        val viewInflated: View = LayoutInflater.from(context)
            .inflate(R.layout.popuplinksocial, view as ViewGroup?, false)
        val input: TextInputEditText = viewInflated.findViewById(R.id.popupLinkSocial_link)
        input.setText(viewModel.linkFacebook.value)

        MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(R.string.modifica_titoloPopupLinkSocial)
            .setView(viewInflated)
            .setPositiveButton(R.string.ok) { _, _ ->
                viewModel.linkFacebook.value = estraiTestoDaElemento(input)
            }
            .setNegativeButton(R.string.annulla) { _, _ -> }
            .show()
    }

    @EventHandler
    private fun clickInstagram() {
        val viewInflated: View = LayoutInflater.from(context)
            .inflate(R.layout.popuplinksocial, view as ViewGroup?, false)
        val input: TextInputEditText = viewInflated.findViewById(R.id.popupLinkSocial_link)
        input.setText(viewModel.linkInstagram.value)

        MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(R.string.modifica_titoloPopupLinkSocial)
            .setView(viewInflated)
            .setPositiveButton(R.string.ok) { _, _ ->
                viewModel.linkInstagram.value = estraiTestoDaElemento(input)
            }
            .setNegativeButton(R.string.annulla) { _, _ -> }
            .show()
    }

    @EventHandler
    private fun clickGitHub() {
        val viewInflated: View = LayoutInflater.from(context)
            .inflate(R.layout.popuplinksocial, view as ViewGroup?, false)
        val input: TextInputEditText = viewInflated.findViewById(R.id.popupLinkSocial_link)
        input.setText(viewModel.linkGitHub.value)

        MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(R.string.modifica_titoloPopupLinkSocial)
            .setView(viewInflated)
            .setPositiveButton(R.string.ok) { _, _ ->
                viewModel.linkGitHub.value = estraiTestoDaElemento(input)
            }
            .setNegativeButton(R.string.annulla) { _, _ -> }
            .show()
    }

    @EventHandler
    private fun clickX() {
        val viewInflated: View = LayoutInflater.from(context)
            .inflate(R.layout.popuplinksocial, view as ViewGroup?, false)
        val input: TextInputEditText = viewInflated.findViewById(R.id.popupLinkSocial_link)
        input.setText(viewModel.linkX.value)

        MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(R.string.modifica_titoloPopupLinkSocial)
            .setView(viewInflated)
            .setPositiveButton(R.string.ok) { _, _ ->
                viewModel.linkX.value = estraiTestoDaElemento(input)
            }
            .setNegativeButton(R.string.annulla) { _, _ -> }
            .show()
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

                binding.modificaProfiloDataNascita.setText(localDate.toLocalStringShort())
            }
        }

        datePicker.show(requireActivity().supportFragmentManager, "datePicker")
    }
}
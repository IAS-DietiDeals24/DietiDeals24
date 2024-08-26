package com.iasdietideals24.dietideals24.controller

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Context
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.CreaastaBinding
import com.iasdietideals24.dietideals24.model.ModelAsta
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.ImageHandler
import com.iasdietideals24.dietideals24.utilities.classes.toLocalDate
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.classes.toMillis
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentGoToHome
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalTime

class ControllerCreaAsta : Controller<CreaastaBinding>() {

    private lateinit var viewModel: ModelAsta

    private var listenerGoToHome: OnFragmentGoToHome? = null

    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>
    private lateinit var selectPhoto: ActivityResultLauncher<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnFragmentGoToHome) {
            listenerGoToHome = requireContext() as OnFragmentGoToHome
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerGoToHome = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tipoAccount = runBlocking { caricaPreferenzaStringa("tipoAccount") }

        val asteCompratore = arrayOf(getString(R.string.tipoAsta_astaInversa))
        val asteVenditore = arrayOf(
            getString(R.string.tipoAsta_astaTempoFisso),
            getString(R.string.tipoAsta_astaSilenziosa)
        )
        var selezionato: Int = -1

        MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(R.string.crea_titoloPopupTipoAsta)
            .setSingleChoiceItems(
                when (tipoAccount) {
                    "compratore" -> asteCompratore
                    "venditore" -> asteVenditore
                    else -> arrayOf()
                },
                selezionato
            ) { _, which ->
                selezionato = which
            }
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                when (tipoAccount) {
                    "compratore" -> viewModel.tipo.value = asteCompratore[selezionato]
                    "venditore" -> viewModel.tipo.value = asteVenditore[selezionato]
                }
            }
            .show()
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        when (viewModel.tipo.value) {
            "Inversa" -> {
                binding.creaTipoAsta.text = getString(
                    R.string.crea_tipoAsta,
                    getString(R.string.tipoAsta_astaInversa)
                )
                binding.creaEtichettaPrezzo.text = getString(
                    R.string.crea_prezzo,
                    getString(R.string.crea_prezzoPartenza)
                )
            }

            "Tempo fisso" -> {
                binding.creaTipoAsta.text = getString(
                    R.string.crea_tipoAsta,
                    getString(R.string.tipoAsta_astaTempoFisso)
                )
                binding.creaEtichettaPrezzo.text = getString(
                    R.string.crea_prezzo,
                    getString(R.string.crea_prezzoMinimo)
                )
            }

            "Silenziosa" -> {
                binding.creaTipoAsta.text = getString(
                    R.string.crea_tipoAsta,
                    getString(R.string.tipoAsta_astaSilenziosa)
                )
                binding.creaConstraintLayout5.visibility = ConstraintLayout.GONE
            }
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.creaPulsanteCrea.setOnClickListener {
            clickCrea()
        }

        binding.creaCampoDataScadenza.setEndIconOnClickListener {
            clickDataScadenza()
        }

        binding.creaCampoOra.setEndIconOnClickListener {
            clickOraScadenza()
        }

        binding.creaCampoFoto.setOnClickListener {
            clickFoto()
        }
    }

    @UIBuilder
    override fun impostaEventiDiCambiamentoCampi() {
        binding.creaDataScadenza.addTextChangedListener {
            rimuoviErroreCampi()
        }

        binding.creaOra.addTextChangedListener {
            rimuoviErroreCampi()
        }

        binding.creaPrezzo.addTextChangedListener {
            rimuoviErroreCampi()
        }

        binding.creaNome.addTextChangedListener {
            rimuoviErroreCampi()
        }

        binding.creaCategoria.addTextChangedListener {
            rimuoviErroreCampi()
        }

        binding.creaDescrizione.addTextChangedListener {
            rimuoviErroreCampi()
        }
    }

    private fun rimuoviErroreCampi() {
        rimuoviErroreCampo(
            binding.creaCampoDataScadenza,
            binding.creaCampoOra,
            binding.creaCampoPrezzo,
            binding.creaCampoNome,
            binding.creaCampoCategoria,
            binding.creaCampoDescrizione
        )
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity)[ModelAsta::class]

        requestPermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results: Map<String, Boolean> ->
                apriGalleria(results)
            }

        selectPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewModel.immagine.value = ImageHandler.encodeImage(uri, fragmentContext)
        }
    }

    private fun apriGalleria(results: Map<String, Boolean>) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                when {
                    results.getOrDefault(READ_MEDIA_IMAGES, false) ->
                        selectPhoto.launch("image/*")

                    else ->
                        Snackbar.make(fragmentView, R.string.noMediaAccess, Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.blu, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()
                }
            }

            else -> {
                when {
                    results.getOrDefault(READ_EXTERNAL_STORAGE, false) ->
                        selectPhoto.launch("image/*")

                    else ->
                        Snackbar.make(fragmentView, R.string.noMediaAccess, Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.blu, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()
                }
            }
        }
    }

    @UIBuilder
    override fun impostaOsservatori() {
        val dataFineObserver = Observer<LocalDate> { newData ->
            if (newData != LocalDate.MIN)
                binding.creaDataScadenza.setText(newData.toLocalStringShort())
            else
                binding.creaDataScadenza.setText("")
        }
        viewModel.dataFine.observe(viewLifecycleOwner, dataFineObserver)

        val oraFineObserver = Observer<LocalTime> { newOra ->
            binding.creaOra.setText(newOra.toString())
        }
        viewModel.oraFine.observe(viewLifecycleOwner, oraFineObserver)

        val prezzoObserver = Observer<Double> { newPrezzo ->
            binding.creaPrezzo.setText(newPrezzo.toString())
        }
        viewModel.prezzo.observe(viewLifecycleOwner, prezzoObserver)

        val immagineObserver = Observer<ByteArray> { newByteArray: ByteArray? ->
            when {
                newByteArray == null || newByteArray.isEmpty() -> {
                    binding.creaCampoFoto.setImageResource(R.drawable.icona_fotocamera)
                }

                else -> {
                    binding.creaCampoFoto.load(newByteArray) {
                        crossfade(true)
                    }
                }
            }
        }
        viewModel.immagine.observe(viewLifecycleOwner, immagineObserver)

        val nomeObserver = Observer<String> { newNome ->
            binding.creaNome.setText(newNome)
        }
        viewModel.nome.observe(viewLifecycleOwner, nomeObserver)

        val categoriaObserver = Observer<String> { newCategoria ->
            binding.creaCategoria.setText(newCategoria)
        }
        viewModel.categoria.observe(viewLifecycleOwner, categoriaObserver)

        val descrizioneObserver = Observer<String> { newDescrizione ->
            binding.creaDescrizione.setText(newDescrizione)
        }
        viewModel.descrizione.observe(viewLifecycleOwner, descrizioneObserver)
    }

    @EventHandler
    private fun clickCrea() {
        try {
            viewModel.validate()

            viewModel.idCreatore.value = CurrentUser.id

            val returned: Boolean? = eseguiChiamataREST("creaAsta", viewModel.toAsta())

            when (returned) {
                null -> Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                false -> Snackbar.make(
                    fragmentView,
                    R.string.crea_astaNonCreata,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                true -> {
                    viewModel.clear()

                    Snackbar.make(
                        fragmentView,
                        R.string.crea_astaCreataConSuccesso,
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(resources.getColor(R.color.blu, null))
                        .setTextColor(resources.getColor(R.color.grigio, null))
                        .show()

                    listenerGoToHome?.onFragmentGoToHome()
                }
            }
        } catch (_: EccezioneCampiNonCompilati) {
            erroreCampo(
                R.string.registrazione_erroreCampiObbligatoriNonCompilati,
                binding.creaCampoDataScadenza,
                binding.creaCampoOra,
                binding.creaCampoPrezzo,
                binding.creaCampoNome,
                binding.creaCampoCategoria,
                binding.creaCampoDescrizione
            )
        }
    }

    @EventHandler
    private fun clickDataScadenza() {
        val calendarConstraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .setFirstDayOfWeek(Calendar.MONDAY)

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.crea_titoloPopupData)
            .setCalendarConstraints(calendarConstraints.build())
            .setSelection(
                if (viewModel.dataFine.value == LocalDate.MIN)
                    MaterialDatePicker.todayInUtcMilliseconds()
                else
                    viewModel.dataFine.value?.toMillis()
            )
            .setTheme(R.style.DatePicker)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            if (datePicker.selection != null) {
                val localDate = datePicker.selection!!.toLocalDate()

                viewModel.dataFine.value = localDate

                binding.creaDataScadenza.setText(localDate.toLocalStringShort())
            }
        }

        datePicker.show(requireActivity().supportFragmentManager, "datePicker")
    }

    @EventHandler
    private fun clickOraScadenza() {
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText(R.string.crea_titoloPopupOra)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(
                if (viewModel.oraFine.value == null)
                    LocalTime.now().hour
                else
                    viewModel.oraFine.value!!.hour
            )
            .setMinute(
                if (viewModel.oraFine.value == null)
                    LocalTime.now().minute
                else
                    viewModel.oraFine.value!!.minute
            )
            .setTheme(R.style.DatePicker)
            .build()

        timePicker.addOnPositiveButtonClickListener {
            viewModel.oraFine.value = LocalTime.of(timePicker.hour, timePicker.minute)
        }

        timePicker.show(requireActivity().supportFragmentManager, "timePicker")
    }

    @EventHandler
    private fun clickFoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
        } else {
            requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
        }
    }
}

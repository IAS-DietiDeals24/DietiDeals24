package com.iasdietideals24.dietideals24.controller

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Context
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.ModificaastaBinding
import com.iasdietideals24.dietideals24.model.ModelAsta
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.ImageHandler
import com.iasdietideals24.dietideals24.utilities.classes.Logger
import com.iasdietideals24.dietideals24.utilities.classes.data.DettagliAsta
import com.iasdietideals24.dietideals24.utilities.classes.toLocalDate
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.classes.toMillis
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToDetails
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

class ControllerModificaAsta : Controller<ModificaastaBinding>() {

    private val args: ControllerModificaAstaArgs by navArgs()

    private lateinit var viewModel: ModelAsta

    private var listenerDetails: OnGoToDetails? = null

    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>
    private lateinit var selectPhoto: ActivityResultLauncher<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnGoToDetails) {
            listenerDetails = requireContext() as OnGoToDetails
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerDetails = null
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        when (viewModel.tipo.value) {
            "Inversa" -> {
                binding.modificaTipoAsta.text = getString(
                    R.string.crea_tipoAsta,
                    getString(R.string.tipoAsta_astaInversa)
                )
                binding.modificaEtichettaPrezzo.text = getString(
                    R.string.crea_prezzo,
                    getString(R.string.crea_prezzoPartenza)
                )
            }

            "Tempo fisso" -> {
                binding.modificaTipoAsta.text = getString(
                    R.string.crea_tipoAsta,
                    getString(R.string.tipoAsta_astaTempoFisso)
                )
                binding.modificaEtichettaPrezzo.text = getString(
                    R.string.crea_prezzo,
                    getString(R.string.crea_prezzoMinimo)
                )
            }

            "Silenziosa" -> {
                binding.modificaTipoAsta.text = getString(
                    R.string.crea_tipoAsta,
                    getString(R.string.tipoAsta_astaSilenziosa)
                )
                binding.modificaConstraintLayout5.visibility = ConstraintLayout.GONE
            }
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.modificaPulsanteIndietro.setOnClickListener { clickIndietro() }

        binding.modificaPulsanteModifica.setOnClickListener { clickModifica() }

        binding.modificaCampoDataScadenza.setEndIconOnClickListener { clickDataScadenza() }

        binding.modificaCampoOra.setEndIconOnClickListener { clickOraScadenza() }

        binding.modificaCampoFoto.setOnClickListener { clickFoto() }
    }

    @UIBuilder
    override fun impostaEventiDiCambiamentoCampi() {
        binding.modificaDataScadenza.addTextChangedListener {
            rimuoviErroreCampi()
        }

        binding.modificaOra.addTextChangedListener {
            rimuoviErroreCampi()
        }

        binding.modificaNome.addTextChangedListener {
            rimuoviErroreCampi()
        }

        binding.modificaCategoria.addTextChangedListener {
            rimuoviErroreCampi()
        }

        binding.modificaDescrizione.addTextChangedListener {
            rimuoviErroreCampi()
        }
    }

    @EventHandler
    private fun rimuoviErroreCampi() {
        rimuoviErroreCampo(
            binding.modificaCampoDataScadenza,
            binding.modificaCampoOra,
            binding.modificaCampoNome,
            binding.modificaCampoCategoria,
            binding.modificaCampoDescrizione
        )
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity)[ModelAsta::class]

        if (args.id != 0L) {
            val asta: DettagliAsta? = eseguiChiamataREST("caricaAsta", args.id)

            if (asta != null) {
                viewModel.idAsta.value = asta.anteprimaAsta.id
                viewModel.idCreatore.value = asta.idCreatore
                viewModel.nomeCreatore.value = asta.nomeCreatore
                viewModel.tipo.value = asta.anteprimaAsta.tipoAsta
                viewModel.dataFine.value = asta.anteprimaAsta.dataScadenza
                viewModel.oraFine.value = asta.anteprimaAsta.oraScadenza
                viewModel.prezzo.value = asta.anteprimaAsta.offerta
                viewModel.immagine.value = asta.anteprimaAsta.foto
                viewModel.nome.value = asta.anteprimaAsta.nome
                viewModel.categoria.value = asta.categoria
                viewModel.descrizione.value = asta.descrizione
            }
        }

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
                binding.modificaDataScadenza.setText(newData.toLocalStringShort())
            else
                binding.modificaDataScadenza.setText("")
        }
        viewModel.dataFine.observe(viewLifecycleOwner, dataFineObserver)

        val oraFineObserver = Observer<LocalTime> { newOra ->
            binding.modificaOra.setText(newOra.toString())
        }
        viewModel.oraFine.observe(viewLifecycleOwner, oraFineObserver)

        val prezzoObserver = Observer<BigDecimal> { newPrezzo ->
            binding.modificaPrezzo.text =
                getString(R.string.placeholder_prezzo, newPrezzo.toString())
        }
        viewModel.prezzo.observe(viewLifecycleOwner, prezzoObserver)

        val immagineObserver = Observer<ByteArray> { newByteArray: ByteArray? ->
            when {
                newByteArray == null || newByteArray.isEmpty() -> {
                    binding.modificaCampoFoto.setImageResource(R.drawable.icona_fotocamera)
                }

                else -> {
                    binding.modificaCampoFoto.load(newByteArray) {
                        crossfade(true)
                    }
                }
            }
        }
        viewModel.immagine.observe(viewLifecycleOwner, immagineObserver)

        val nomeObserver = Observer<String> { newNome ->
            binding.modificaNome.setText(newNome)
        }
        viewModel.nome.observe(viewLifecycleOwner, nomeObserver)

        val categoriaObserver = Observer<String> { newCategoria ->
            binding.modificaCategoria.setText(newCategoria)
        }
        viewModel.categoria.observe(viewLifecycleOwner, categoriaObserver)

        val descrizioneObserver = Observer<String> { newDescrizione ->
            binding.modificaDescrizione.setText(newDescrizione)
        }
        viewModel.descrizione.observe(viewLifecycleOwner, descrizioneObserver)
    }

    @EventHandler
    private fun clickIndietro() {
        MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(R.string.modifica_titoloConfermaIndietro)
            .setMessage(R.string.modifica_testoConfermaIndietro)
            .setPositiveButton(R.string.ok) { _, _ ->
                val idAsta = viewModel.idAsta.value!!
                viewModel.clear()
                listenerDetails?.onGoToDetails(idAsta, this::class)
            }
            .setNegativeButton(R.string.annulla) { _, _ -> }
            .show()
    }

    @EventHandler
    private fun clickModifica() {
        try {
            viewModel.validate()

            viewModel.idCreatore.value = CurrentUser.id

            val returned: Boolean? = eseguiChiamataREST("modificaAsta", viewModel.toAsta())

            when (returned) {
                null -> Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                false -> Snackbar.make(
                    fragmentView,
                    R.string.crea_astaNonModificata,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                true -> {
                    viewModel.clear()

                    Snackbar.make(
                        fragmentView,
                        R.string.crea_astaModificataConSuccesso,
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(resources.getColor(R.color.blu, null))
                        .setTextColor(resources.getColor(R.color.grigio, null))
                        .show()

                    Logger.log("Auction created successfully")

                    listenerDetails?.onGoToDetails(
                        viewModel.idAsta.value!!,
                        this::class
                    )
                }
            }
        } catch (_: EccezioneCampiNonCompilati) {
            erroreCampo(
                R.string.registrazione_erroreCampiObbligatoriNonCompilati,
                binding.modificaCampoDataScadenza,
                binding.modificaCampoOra,
                binding.modificaCampoNome,
                binding.modificaCampoCategoria,
                binding.modificaCampoDescrizione
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

                binding.modificaDataScadenza.setText(localDate.toLocalStringShort())
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
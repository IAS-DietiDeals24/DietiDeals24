package com.iasdietideals24.dietideals24.controller

import android.annotation.SuppressLint
import android.content.Context
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
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
import com.iasdietideals24.dietideals24.utilities.data.Asta
import com.iasdietideals24.dietideals24.utilities.dto.AstaDto
import com.iasdietideals24.dietideals24.utilities.enumerations.CategoriaAsta
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneDataPassata
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToHome
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalDate
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.kscripts.toMillis
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.repositories.CategoriaAstaRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import com.iasdietideals24.dietideals24.utilities.tools.ImageHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

class ControllerCreaAsta : Controller<CreaastaBinding>() {

    // ViewModel
    private val viewModel: ModelAsta by activityViewModel()

    // Repositories
    private val astaInversaRepository: AstaInversaRepository by inject()
    private val astaSilenziosaRepository: AstaSilenziosaRepository by inject()
    private val astaTempoFissoRepository: AstaTempoFissoRepository by inject()
    private val categoriaAstaRepository: CategoriaAstaRepository by inject()

    // Listeners
    private var listenerGoToHome: OnGoToHome? = null
    private lateinit var selectPhoto: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnGoToHome) {
            listenerGoToHome = requireContext() as OnGoToHome
        }
        viewModel.clear()
    }

    override fun onDetach() {
        super.onDetach()

        listenerGoToHome = null
        viewModel.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val asteCompratore = arrayOf(getString(R.string.tipoAsta_astaInversa))
        val asteVenditore = arrayOf(
            getString(R.string.tipoAsta_astaTempoFisso),
            getString(R.string.tipoAsta_astaSilenziosa)
        )

        apriMaterialDialog(asteCompratore, asteVenditore)
    }

    private fun apriMaterialDialog(
        asteCompratore: Array<String>,
        asteVenditore: Array<String>
    ) {
        var selezionato = -1

        MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(R.string.crea_titoloPopupTipoAsta)
            .setSingleChoiceItems(
                when (CurrentUser.tipoAccount) {
                    TipoAccount.COMPRATORE -> asteCompratore
                    TipoAccount.VENDITORE -> asteVenditore
                    else -> arrayOf()
                },
                selezionato
            ) { _, which ->
                selezionato = which
            }
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                if (selezionato == -1) {
                    apriMaterialDialog(asteCompratore, asteVenditore)
                } else {
                    when (CurrentUser.tipoAccount) {
                        TipoAccount.COMPRATORE -> viewModel.tipo.value = TipoAsta.INVERSA

                        TipoAccount.VENDITORE -> viewModel.tipo.value =
                            when (asteVenditore[selezionato]) {
                                getString(R.string.tipoAsta_astaTempoFisso) -> TipoAsta.TEMPO_FISSO
                                getString(R.string.tipoAsta_astaSilenziosa) -> TipoAsta.SILENZIOSA
                                else -> TipoAsta.TEMPO_FISSO
                            }

                        else -> {
                            // Non fare nulla
                        }
                    }
                }
            }
            .setNegativeButton(getString(R.string.annulla)) { _, _ ->
                listenerGoToHome?.onGoToHome()
            }
            .setCancelable(false)
            .show()
    }

    @SuppressLint("ClickableViewAccessibility")
    @UIBuilder
    override fun impostaEventiClick() {
        binding.creaPulsanteCrea.setOnClickListener {
            clickCrea()
        }

        binding.creaCampoDataScadenza.setStartIconOnClickListener {
            clickDataScadenza()
        }

        binding.creaCampoOra.setStartIconOnClickListener {
            clickOraScadenza()
        }

        binding.creaCampoFoto.setOnClickListener {
            clickFoto()
        }

        binding.creaDataScadenza.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.clearFocus()

                clickDataScadenza()

                return@OnTouchListener true
            }
            false
        })

        binding.creaOra.setOnTouchListener(View.OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                v.clearFocus()

                clickOraScadenza()

                return@OnTouchListener true
            }
            false
        })
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

    @EventHandler
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
        selectPhoto = registerForActivityResult(PickVisualMedia()) { uri: Uri? ->
            viewModel.immagine.value = ImageHandler.comprimiByteArray(uri, fragmentContext)
        }

        lifecycleScope.launch {
            val categorieAsta: MutableList<String> = mutableListOf()

            withContext(Dispatchers.IO) {
                categoriaAstaRepository.recuperaCategorieAsta().forEach {
                    categorieAsta.add(CategoriaAsta.fromEnumToString(CategoriaAsta.valueOf(it.nome)))
                }
            }

            val adapter: ArrayAdapter<String> = ArrayAdapter(
                fragmentContext,
                android.R.layout.simple_dropdown_item_1line,
                categorieAsta
            )

            binding.creaCategoria.setAdapter(adapter)
        }
    }

    @UIBuilder
    override fun impostaOsservatori() {
        val tipoAstaObserver = Observer<TipoAsta?> { newTipoAsta ->
            when (newTipoAsta) {
                TipoAsta.INVERSA -> {
                    binding.creaTipoAsta.text = getString(
                        R.string.crea_tipoAsta,
                        getString(R.string.tipoAsta_astaInversa)
                    )
                    binding.creaEtichettaPrezzo.text = getString(
                        R.string.crea_prezzo,
                        getString(R.string.crea_prezzoPartenza)
                    )
                }

                TipoAsta.TEMPO_FISSO -> {
                    binding.creaTipoAsta.text = getString(
                        R.string.crea_tipoAsta,
                        getString(R.string.tipoAsta_astaTempoFisso)
                    )
                    binding.creaEtichettaPrezzo.text = getString(
                        R.string.crea_prezzo,
                        getString(R.string.crea_prezzoMinimo)
                    )
                }

                TipoAsta.SILENZIOSA -> {
                    binding.creaTipoAsta.text = getString(
                        R.string.crea_tipoAsta,
                        getString(R.string.tipoAsta_astaSilenziosa)
                    )
                    binding.creaConstraintLayout3.visibility = ConstraintLayout.GONE
                }

                else -> {
                    // Non fare niente
                }
            }
        }
        viewModel.tipo.observe(viewLifecycleOwner, tipoAstaObserver)

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

        val prezzoObserver = Observer<BigDecimal> { newPrezzo ->
            binding.creaPrezzo.setText(
                String.format(
                    resources.configuration.getLocales().get(0),
                    "%.2f",
                    newPrezzo.toDouble()
                )
            )
        }
        viewModel.prezzo.observe(viewLifecycleOwner, prezzoObserver)

        val immagineObserver = Observer { newByteArray: ByteArray? ->
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

        val categoriaObserver = Observer<CategoriaAsta> { newCategoria ->
            binding.creaCategoria.setText(CategoriaAsta.fromEnumToString(newCategoria))
        }
        viewModel.categoria.observe(viewLifecycleOwner, categoriaObserver)

        val descrizioneObserver = Observer<String> { newDescrizione ->
            binding.creaDescrizione.setText(newDescrizione)
        }
        viewModel.descrizione.observe(viewLifecycleOwner, descrizioneObserver)
    }

    @EventHandler
    private fun clickCrea() {
        viewModel.prezzo.value =
            BigDecimal(estraiTestoDaElemento(binding.creaPrezzo).replace(",", "."))
        viewModel.nome.value = estraiTestoDaElemento(binding.creaNome)
        viewModel.categoria.value =
            CategoriaAsta.fromStringToEnum(estraiTestoDaElemento(binding.creaCategoria))
        viewModel.descrizione.value = estraiTestoDaElemento(binding.creaDescrizione)

        lifecycleScope.launch {
            try {
                viewModel.validate()

                viewModel.idCreatore.value = CurrentUser.id

                val returned: Asta = withContext(Dispatchers.IO) { creaAsta().toAsta() }

                when (returned.idAsta) {
                    0L -> Snackbar.make(
                        fragmentView,
                        R.string.crea_astaNonCreata,
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(resources.getColor(R.color.blu, null))
                        .setTextColor(resources.getColor(R.color.grigio, null))
                        .show()

                    else -> {
                        viewModel.clear()

                        Snackbar.make(
                            fragmentView,
                            R.string.crea_astaCreataConSuccesso,
                            Snackbar.LENGTH_SHORT
                        )
                            .setBackgroundTint(resources.getColor(R.color.blu, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()

                        withContext(Dispatchers.IO) {
                            logger.scriviLog("Auction created successfully")
                        }

                        listenerGoToHome?.onGoToHome()
                    }
                }
            } catch (_: EccezioneDataPassata) {
                erroreCampo(
                    R.string.registrazione_erroreDataPassata,
                    binding.creaCampoDataScadenza,
                    binding.creaCampoOra
                )
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
            } catch (_: Exception) {
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    private suspend fun creaAsta(): AstaDto {
        return when (viewModel.tipo.value!!) {
            TipoAsta.INVERSA -> astaInversaRepository.creaAstaInversa(viewModel.toAstaInversa())

            TipoAsta.TEMPO_FISSO -> astaTempoFissoRepository.creaAstaTempoFisso(viewModel.toAstaTempoFisso())

            TipoAsta.SILENZIOSA -> astaSilenziosaRepository.creaAstaSilenziosa(viewModel.toAstaSilenziosa())
        }
    }

    @EventHandler
    private fun clickDataScadenza() {
        val calendarConstraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
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
        selectPhoto.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }
}

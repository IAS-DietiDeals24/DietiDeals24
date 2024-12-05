package com.iasdietideals24.dietideals24.controller

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Context
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
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
import com.iasdietideals24.dietideals24.utilities.data.Asta
import com.iasdietideals24.dietideals24.utilities.data.Profilo
import com.iasdietideals24.dietideals24.utilities.dto.AccountDto
import com.iasdietideals24.dietideals24.utilities.dto.AstaDto
import com.iasdietideals24.dietideals24.utilities.dto.CompratoreDto
import com.iasdietideals24.dietideals24.utilities.dto.ProfiloDto
import com.iasdietideals24.dietideals24.utilities.enumerations.CategoriaAsta
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneDataPassata
import com.iasdietideals24.dietideals24.utilities.kscripts.OnBackButton
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalDate
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.kscripts.toMillis
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.repositories.CategoriaAstaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.CompratoreRepository
import com.iasdietideals24.dietideals24.utilities.repositories.ProfiloRepository
import com.iasdietideals24.dietideals24.utilities.repositories.VenditoreRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

class ControllerModificaAsta : Controller<ModificaastaBinding>() {

    // Navigation
    private val args: ControllerModificaAstaArgs by navArgs()

    // Repositories
    private val repositoryAstaInversa: AstaInversaRepository by inject()
    private val repositoryAstaSilenziosa: AstaSilenziosaRepository by inject()
    private val repositoryAstaTempoFisso: AstaTempoFissoRepository by inject()
    private val repositoryProfilo: ProfiloRepository by inject()
    private val compratoreRepository: CompratoreRepository by inject()
    private val venditoreRepository: VenditoreRepository by inject()
    private val categoriaAstaRepository: CategoriaAstaRepository by inject()

    // ViewModel
    private val viewModel: ModelAsta by activityViewModel()

    // Listeners
    private var listenerDetails: OnGoToDetails? = null
    private var listenerBackButton: OnBackButton? = null

    private lateinit var requestPermissions: ActivityResultLauncher<Array<String>>
    private lateinit var selectPhoto: ActivityResultLauncher<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnGoToDetails) {
            listenerDetails = requireContext() as OnGoToDetails
        }
        if (requireContext() is OnBackButton) {
            listenerBackButton = requireContext() as OnBackButton
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerDetails = null
        listenerBackButton = null
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        when (viewModel.tipo.value) {
            TipoAsta.INVERSA -> {
                binding.modificaTipoAsta.text = getString(
                    R.string.crea_tipoAsta,
                    getString(R.string.tipoAsta_astaInversa)
                )
                binding.modificaEtichettaPrezzo.text = getString(
                    R.string.crea_prezzo,
                    getString(R.string.crea_prezzoPartenza)
                )
            }

            TipoAsta.TEMPO_FISSO -> {
                binding.modificaTipoAsta.text = getString(
                    R.string.crea_tipoAsta,
                    getString(R.string.tipoAsta_astaTempoFisso)
                )
                binding.modificaEtichettaPrezzo.text = getString(
                    R.string.crea_prezzo,
                    getString(R.string.crea_prezzoMinimo)
                )
            }

            TipoAsta.SILENZIOSA -> {
                binding.modificaTipoAsta.text = getString(
                    R.string.crea_tipoAsta,
                    getString(R.string.tipoAsta_astaSilenziosa)
                )
                binding.modificaConstraintLayout5.visibility = ConstraintLayout.GONE
            }

            else -> {
                // Non fare nulla
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
        requestPermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results: Map<String, Boolean> ->
                apriGalleria(results)
            }

        selectPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewModel.immagine.value =
                com.iasdietideals24.dietideals24.utilities.tools.ImageHandler.encodeImage(
                    uri,
                    fragmentContext
                )
        }

        lifecycleScope.launch {
            val categorieAsta: MutableList<String> = mutableListOf()

            withContext(Dispatchers.IO) {
                categoriaAstaRepository.recuperaCategorieAsta().forEach {
                    categorieAsta.add(it.nome)
                }
            }

            val adapter: ArrayAdapter<String> = ArrayAdapter(
                fragmentContext,
                android.R.layout.simple_dropdown_item_1line,
                categorieAsta
            )

            binding.modificaCategoria.setAdapter(adapter)
        }

        if (args.id != 0L) {
            lifecycleScope.launch {
                try {
                    val asta: Asta = withContext(Dispatchers.IO) { caricaAsta().toAsta() }
                    var userName: String
                    withContext(Dispatchers.IO) {
                        val returned = caricaAccount(asta.idCreatore)
                        userName = returned.profiloShallow.nomeUtente
                    }
                    val creatoreAsta: Profilo =
                        withContext(Dispatchers.IO) { caricaProfilo(userName).toProfilo() }

                    if (asta.idAsta != 0L && creatoreAsta.nomeUtente != "") {
                        viewModel.idAsta.value = asta.idAsta
                        viewModel.idCreatore.value = asta.idCreatore
                        viewModel.nomeCreatore.value = creatoreAsta.nome
                        viewModel.tipo.value = asta.tipo
                        viewModel.dataFine.value = asta.dataFine
                        viewModel.oraFine.value = asta.oraFine
                        viewModel.prezzo.value = asta.prezzo
                        viewModel.immagine.value = asta.immagine
                        viewModel.nome.value = asta.nome
                        viewModel.categoria.value = asta.categoria
                        viewModel.descrizione.value = asta.descrizione
                    }
                } catch (e: Exception) {
                    Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(resources.getColor(R.color.blu, null))
                        .setTextColor(resources.getColor(R.color.grigio, null))
                        .show()
                }
            }
        }
    }

    private suspend fun caricaAsta(): AstaDto {
        return when (args.tipo) {
            TipoAsta.INVERSA -> repositoryAstaInversa.caricaAstaInversa(args.id)

            TipoAsta.SILENZIOSA -> repositoryAstaSilenziosa.caricaAstaSilenziosa(args.id)

            TipoAsta.TEMPO_FISSO -> repositoryAstaTempoFisso.caricaAstaTempoFisso(args.id)
        }
    }

    private suspend fun caricaAccount(idCreatore: Long): AccountDto {
        return when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> {
                compratoreRepository.caricaAccountCompratore(idCreatore)
            }

            TipoAccount.VENDITORE -> {
                venditoreRepository.caricaAccountVenditore(idCreatore)
            }

            else -> CompratoreDto()
        }
    }

    private suspend fun caricaProfilo(nomeUtente: String): ProfiloDto {
        return repositoryProfilo.caricaProfilo(nomeUtente)
    }

    private fun apriGalleria(results: Map<String, Boolean>) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                when {
                    results.getOrDefault(READ_MEDIA_IMAGES, false) ||
                            results.getOrDefault(READ_MEDIA_VISUAL_USER_SELECTED, false) ->
                        selectPhoto.launch("image/*")

                    else ->
                        Snackbar.make(fragmentView, R.string.noMediaAccess, Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.arancione, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()
                }
            }

            Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU -> {
                when {
                    results.getOrDefault(READ_MEDIA_IMAGES, false) ->
                        selectPhoto.launch("image/*")

                    else ->
                        Snackbar.make(fragmentView, R.string.noMediaAccess, Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.arancione, null))
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
                            .setBackgroundTint(resources.getColor(R.color.arancione, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()
                }
            }
        }
    }

    @UIBuilder
    override fun impostaOsservatori() {
        val tipoAstaObserver = Observer<TipoAsta?> { newTipoAsta ->
            when (newTipoAsta) {
                TipoAsta.INVERSA -> {
                    binding.modificaTipoAsta.text = getString(
                        R.string.crea_tipoAsta,
                        getString(R.string.tipoAsta_astaInversa)
                    )
                    binding.modificaEtichettaPrezzo.text = getString(
                        R.string.crea_prezzo,
                        getString(R.string.crea_prezzoPartenza)
                    )
                }

                TipoAsta.TEMPO_FISSO -> {
                    binding.modificaTipoAsta.text = getString(
                        R.string.crea_tipoAsta,
                        getString(R.string.tipoAsta_astaTempoFisso)
                    )
                    binding.modificaEtichettaPrezzo.text = getString(
                        R.string.crea_prezzo,
                        getString(R.string.crea_prezzoMinimo)
                    )
                }

                TipoAsta.SILENZIOSA -> {
                    binding.modificaTipoAsta.text = getString(
                        R.string.crea_tipoAsta,
                        getString(R.string.tipoAsta_astaSilenziosa)
                    )
                    binding.modificaConstraintLayout3.visibility = ConstraintLayout.GONE
                }

                else -> {
                    // Non fare niente
                }
            }
        }
        viewModel.tipo.observe(viewLifecycleOwner, tipoAstaObserver)

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

        val immagineObserver = Observer { newByteArray: ByteArray? ->
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

        val categoriaObserver = Observer<CategoriaAsta> { newCategoria ->
            binding.modificaCategoria.setText(CategoriaAsta.fromEnumToString(newCategoria))
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
                val tipoAsta = viewModel.tipo.value!!
                viewModel.clear()
                if (args.fromDetails) listenerDetails?.onGoToDetails(idAsta, tipoAsta, this::class)
                else listenerBackButton?.onBackButton()
            }
            .setNegativeButton(R.string.annulla) { _, _ -> }
            .show()
    }

    @EventHandler
    private fun clickModifica() {
        viewModel.nome.value = estraiTestoDaElemento(binding.modificaNome)
        viewModel.categoria.value =
            CategoriaAsta.fromStringToEnum(estraiTestoDaElemento(binding.modificaCategoria))
        viewModel.descrizione.value = estraiTestoDaElemento(binding.modificaDescrizione)

        lifecycleScope.launch {
            try {
                viewModel.validate()

                viewModel.idCreatore.value = CurrentUser.id

                val returned: Asta = withContext(Dispatchers.IO) { aggiornaAsta().toAsta() }

                when (returned.idAsta) {
                    0L -> Snackbar.make(
                        fragmentView,
                        R.string.crea_astaNonModificata,
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(resources.getColor(R.color.blu, null))
                        .setTextColor(resources.getColor(R.color.grigio, null))
                        .show()

                    else -> {
                        Snackbar.make(
                            fragmentView,
                            R.string.crea_astaModificataConSuccesso,
                            Snackbar.LENGTH_SHORT
                        )
                            .setBackgroundTint(resources.getColor(R.color.blu, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()

                        withContext(Dispatchers.IO) {
                            logger.scriviLog("Auction created successfully")
                        }

                        if (args.fromDetails)
                            listenerDetails?.onGoToDetails(
                                viewModel.idAsta.value!!,
                                viewModel.tipo.value!!,
                                ControllerModificaAsta::class
                            )
                        else listenerBackButton?.onBackButton()
                    }
                }
            } catch (_: EccezioneDataPassata) {
                erroreCampo(
                    R.string.registrazione_erroreDataPassata,
                    binding.modificaCampoDataScadenza,
                    binding.modificaCampoOra
                )
            } catch (_: EccezioneCampiNonCompilati) {
                erroreCampo(
                    R.string.registrazione_erroreCampiObbligatoriNonCompilati,
                    binding.modificaCampoDataScadenza,
                    binding.modificaCampoOra,
                    binding.modificaCampoNome,
                    binding.modificaCampoCategoria,
                    binding.modificaCampoDescrizione
                )
            } catch (e: Exception) {
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    private suspend fun aggiornaAsta(): AstaDto {
        return when (viewModel.tipo.value!!) {
            TipoAsta.INVERSA -> {
                repositoryAstaInversa.aggiornaAstaInversa(
                    viewModel.toAstaInversa(),
                    args.id
                )
            }

            TipoAsta.TEMPO_FISSO -> {
                repositoryAstaTempoFisso.aggiornaAstaTempoFisso(
                    viewModel.toAstaTempoFisso(),
                    args.id
                )
            }

            TipoAsta.SILENZIOSA -> {
                repositoryAstaSilenziosa.aggiornaAstaSilenziosa(
                    viewModel.toAstaSilenziosa(),
                    args.id
                )
            }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED))
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
        } else {
            requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
        }
    }
}
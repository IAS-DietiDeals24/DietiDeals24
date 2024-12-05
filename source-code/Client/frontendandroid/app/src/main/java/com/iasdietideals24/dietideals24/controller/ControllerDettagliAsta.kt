package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.DettagliastaBinding
import com.iasdietideals24.dietideals24.model.ModelAsta
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.data.Asta
import com.iasdietideals24.dietideals24.utilities.data.Offerta
import com.iasdietideals24.dietideals24.utilities.data.Profilo
import com.iasdietideals24.dietideals24.utilities.dto.AccountDto
import com.iasdietideals24.dietideals24.utilities.dto.AstaDto
import com.iasdietideals24.dietideals24.utilities.dto.OffertaDto
import com.iasdietideals24.dietideals24.utilities.dto.OffertaInversaDto
import com.iasdietideals24.dietideals24.utilities.dto.OffertaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.dto.OffertaTempoFissoDto
import com.iasdietideals24.dietideals24.utilities.dto.ProfiloDto
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto
import com.iasdietideals24.dietideals24.utilities.enumerations.CategoriaAsta
import com.iasdietideals24.dietideals24.utilities.enumerations.StatoAsta
import com.iasdietideals24.dietideals24.utilities.enumerations.StatoOfferta
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import com.iasdietideals24.dietideals24.utilities.kscripts.OnBackButton
import com.iasdietideals24.dietideals24.utilities.kscripts.OnEditButton
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToBids
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToProfile
import com.iasdietideals24.dietideals24.utilities.kscripts.OnHideMaterialDivider
import com.iasdietideals24.dietideals24.utilities.kscripts.OnRefresh
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.repositories.CompratoreRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaTempoFissoRepository
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
import java.time.ZoneOffset

class ControllerDettagliAsta : Controller<DettagliastaBinding>() {

    // Navigation
    private val args: ControllerDettagliAstaArgs by navArgs()

    // ViewModel
    private val viewModel: ModelAsta by activityViewModel()

    // Repositiories
    private val astaTempoFissoRepository: AstaTempoFissoRepository by inject()
    private val astaInversaRepository: AstaInversaRepository by inject()
    private val astaSilenziosaRepository: AstaSilenziosaRepository by inject()
    private val profiloRepository: ProfiloRepository by inject()
    private val offertaInversaRepository: OffertaInversaRepository by inject()
    private val offertaTempoFissoRepository: OffertaTempoFissoRepository by inject()
    private val offertaSilenziosaRepository: OffertaSilenziosaRepository by inject()
    private val compratoreRepository: CompratoreRepository by inject()
    private val venditoreRepository: VenditoreRepository by inject()

    // Listeners
    private var listenerBackButton: OnBackButton? = null
    private var listenerProfile: OnGoToProfile? = null
    private var listenerEditButton: OnEditButton? = null
    private var listenerBids: OnGoToBids? = null
    private var listenerRefresh: OnRefresh? = null
    private var listerMaterialDivider: OnHideMaterialDivider? = null

    override fun onPause() {
        super.onPause()

        listerMaterialDivider?.onHideMaterialDivider(false)
    }

    override fun onResume() {
        super.onResume()

        listerMaterialDivider?.onHideMaterialDivider(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnBackButton) {
            listenerBackButton = requireContext() as OnBackButton
        }
        if (requireContext() is OnGoToProfile) {
            listenerProfile = requireContext() as OnGoToProfile
        }
        if (requireContext() is OnEditButton) {
            listenerEditButton = requireContext() as OnEditButton
        }
        if (requireContext() is OnGoToBids) {
            listenerBids = requireContext() as OnGoToBids
        }
        if (requireContext() is OnRefresh) {
            listenerRefresh = requireContext() as OnRefresh
        }
        if (requireContext() is OnHideMaterialDivider) {
            listerMaterialDivider = requireContext() as OnHideMaterialDivider
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerBackButton = null
        listenerProfile = null
        listenerEditButton = null
        listenerBids = null
        listenerRefresh = null
        listerMaterialDivider = null
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.dettagliAstaPulsanteIndietro.setOnClickListener { clickIndietro() }

        binding.dettagliAstaPulsanteAiuto.setOnClickListener { clickAiuto() }

        binding.dettagliAstaImmagineUtente.setOnClickListener { clickImmagine() }

        binding.dettagliAstaNomeUtente.setOnClickListener { clickImmagine() }

        binding.dettagliAstaPulsanteOfferta.setOnClickListener { clickOfferta() }

        binding.dettagliAstaPulsanteModifica.setOnClickListener { clickModifica() }

        binding.dettagliAstaPulsanteElimina.setOnClickListener { clickElimina() }

        binding.dettagliAstaPulsanteElencoOfferte.setOnClickListener { clickOfferte() }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
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
                val offerta: Offerta =
                    withContext(Dispatchers.IO) {
                        recuperaOfferta(asta.idAsta, asta.tipo).toOfferta()
                    }

                if (asta.idAsta != 0L && creatoreAsta.nomeUtente != "") {
                    viewModel.idAsta.value = asta.idAsta
                    viewModel.stato.value = asta.stato
                    viewModel.idCreatore.value = asta.idCreatore
                    viewModel.nomeCreatore.value = creatoreAsta.nomeUtente
                    viewModel.tipo.value = asta.tipo
                    viewModel.dataFine.value = asta.dataFine
                    viewModel.oraFine.value = asta.oraFine
                    viewModel.immagine.value = asta.immagine
                    viewModel.nome.value = asta.nome
                    viewModel.categoria.value = asta.categoria
                    viewModel.descrizione.value = asta.descrizione
                    viewModel.prezzo.value = asta.prezzo

                    binding.dettagliAstaTestoOfferta.text =
                        if (viewModel.tipo.value == TipoAsta.INVERSA)
                            getString(R.string.dettagliAsta_testoOfferta2)
                        else
                            getString(R.string.dettagliAsta_testoOfferta1)

                    val offertaAsta = when (viewModel.tipo.value) {
                        TipoAsta.TEMPO_FISSO -> offerta.offerta.toString()

                        TipoAsta.INVERSA -> if (offerta.offerta == BigDecimal("0.00"))
                            asta.prezzo
                        else
                            offerta.offerta.toString()

                        else -> "???"
                    }

                    binding.dettagliAstaOfferta.text =
                        getString(R.string.placeholder_prezzo, offertaAsta)

                    if (creatoreAsta.immagineProfilo.isNotEmpty()) {
                        binding.dettagliAstaImmagineUtente.load(creatoreAsta.immagineProfilo) {
                            crossfade(true)
                        }
                        binding.dettagliAstaImmagineUtente.scaleType =
                            ImageView.ScaleType.CENTER_CROP
                    }

                    val idAltroAccount =
                        if (creatoreAsta.idAccountCollegati.first == CurrentUser.id)
                            creatoreAsta.idAccountCollegati.first
                        else
                            creatoreAsta.idAccountCollegati.second

                    if (CurrentUser.id == 0L || viewModel.stato.value!! == StatoAsta.CLOSED ||
                        CurrentUser.id == idAltroAccount
                    ) {
                        binding.dettagliAstaPulsanteOfferta.isEnabled = false
                    } else if (CurrentUser.id == asta.idCreatore) {
                        binding.dettagliAstaPulsanteOfferta.visibility = View.GONE
                        binding.dettagliAstaPulsanteModifica.visibility = View.VISIBLE
                        binding.dettagliAstaPulsanteElimina.visibility = View.VISIBLE
                        binding.dettagliAstaPulsanteElencoOfferte.visibility = View.VISIBLE
                    }
                }
            } catch (_: Exception) {
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    private suspend fun caricaAsta(): AstaDto {
        return when (args.tipo) {
            TipoAsta.INVERSA -> {
                astaInversaRepository.caricaAstaInversa(args.id)
            }

            TipoAsta.SILENZIOSA -> {
                astaSilenziosaRepository.caricaAstaSilenziosa(args.id)
            }

            TipoAsta.TEMPO_FISSO -> {
                astaTempoFissoRepository.caricaAstaTempoFisso(args.id)
            }
        }
    }

    private suspend fun caricaAccount(idCreatore: Long): AccountDto {
        var account: AccountDto = compratoreRepository.caricaAccountCompratore(idCreatore)

        if (account.email == "")
            account = venditoreRepository.caricaAccountVenditore(idCreatore)

        return account
    }

    private suspend fun caricaProfilo(nomeUtente: String): ProfiloDto {
        return profiloRepository.caricaProfilo(nomeUtente)
    }

    private suspend fun recuperaOfferta(idAsta: Long, tipo: TipoAsta): OffertaDto {
        return when (tipo) {
            TipoAsta.TEMPO_FISSO -> {
                offertaTempoFissoRepository.recuperaOffertaPiuAlta(idAsta)
            }

            TipoAsta.INVERSA -> {
                offertaInversaRepository.recuperaOffertaPiuBassa(idAsta)
            }

            TipoAsta.SILENZIOSA -> {
                OffertaSilenziosaDto()
            }
        }
    }

    @UIBuilder
    override fun impostaOsservatori() {
        val tipoObserver = Observer<TipoAsta?> { newTipoAsta ->
            binding.dettagliAstaTipoAsta.text = getString(
                R.string.crea_tipoAsta,
                when (newTipoAsta) {
                    TipoAsta.SILENZIOSA -> {
                        getString(R.string.tipoAsta_astaSilenziosa)
                    }

                    TipoAsta.TEMPO_FISSO -> {
                        getString(R.string.tipoAsta_astaTempoFisso)
                    }

                    TipoAsta.INVERSA -> {
                        getString(R.string.tipoAsta_astaInversa)
                    }

                    null -> {
                        // Non fare niente
                    }
                }
            )
        }
        viewModel.tipo.observe(viewLifecycleOwner, tipoObserver)

        val dataObserver = Observer<LocalDate> { newData ->
            binding.dettagliAstaDataScadenza.text = newData.toLocalStringShort()
        }
        viewModel.dataFine.observe(viewLifecycleOwner, dataObserver)

        val oraObserver = Observer<LocalTime> { newOra ->
            binding.dettagliAstaOraScadenza.text = newOra.toString()
        }
        viewModel.oraFine.observe(viewLifecycleOwner, oraObserver)

        val nomeObserver = Observer<String> { newNomeUtente ->
            binding.dettagliAstaNome.text = newNomeUtente
        }
        viewModel.nome.observe(viewLifecycleOwner, nomeObserver)

        val categoriaObserver = Observer<CategoriaAsta> { newCategoria ->
            binding.dettagliAstaCategoria.text = CategoriaAsta.fromEnumToString(newCategoria)
        }
        viewModel.categoria.observe(viewLifecycleOwner, categoriaObserver)

        val immagineObserver = Observer { newByteArray: ByteArray? ->
            when {
                newByteArray == null || newByteArray.isEmpty() -> {
                    binding.dettagliAstaCampoFoto.setImageResource(R.drawable.icona_fotocamera)
                }

                else -> {
                    binding.dettagliAstaCampoFoto.load(newByteArray) {
                        crossfade(true)
                    }
                }
            }
        }
        viewModel.immagine.observe(viewLifecycleOwner, immagineObserver)

        val nomeCreatoreObserver = Observer<String> { newNomeCreatore ->
            binding.dettagliAstaNomeUtente.text = newNomeCreatore
        }
        viewModel.nomeCreatore.observe(viewLifecycleOwner, nomeCreatoreObserver)

        val descrizioneObserver = Observer<String> { newDescrizione ->
            binding.dettagliAstaDescrizione.text = newDescrizione
        }
        viewModel.descrizione.observe(viewLifecycleOwner, descrizioneObserver)

        val prezzoObserver = Observer<BigDecimal> { newPrezzo ->
            binding.dettagliAstaOfferta.text = getString(
                R.string.placeholder_prezzo,
                if (viewModel.tipo.value != TipoAsta.SILENZIOSA)
                    newPrezzo.toString()
                else
                    "???"
            )
        }
        viewModel.prezzo.observe(viewLifecycleOwner, prezzoObserver)
    }

    @EventHandler
    private fun clickIndietro() {
        viewModel.clear()

        listenerBackButton?.onBackButton()
    }

    @EventHandler
    private fun clickAiuto() {
        MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(
                when (viewModel.tipo.value!!) {
                    TipoAsta.SILENZIOSA -> getString(R.string.aiuto_titoloAstaSilenziosa)
                    TipoAsta.TEMPO_FISSO -> getString(R.string.aiuto_titoloAstaTempoFisso)
                    TipoAsta.INVERSA -> getString(R.string.aiuto_titoloAstaInversa)
                }
            )
            .setIcon(R.drawable.icona_aiuto_arancione)
            .setMessage(
                when (viewModel.tipo.value!!) {
                    TipoAsta.SILENZIOSA -> getString(R.string.aiuto_astaSilenziosa)
                    TipoAsta.TEMPO_FISSO -> getString(R.string.aiuto_astaTempoFisso)
                    TipoAsta.INVERSA -> getString(R.string.aiuto_astaInversa)
                }
            )
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
            .show()
    }

    @EventHandler
    private fun clickImmagine() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                logger.scriviLog("Showing user profile")
            }
        }

        listenerProfile?.onGoToProfile(viewModel.idCreatore.value!!, this::class)
    }

    @EventHandler
    private fun clickOfferta() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                logger.scriviLog("Sending a bid")
            }
        }

        val viewInflated: View = LayoutInflater.from(context)
            .inflate(R.layout.popupofferta, view as ViewGroup?, false)
        val campoOfferta: TextInputLayout =
            viewInflated.findViewById(R.id.popupOfferta_campoOfferta)
        val valoreOfferta: TextInputEditText = viewInflated.findViewById(R.id.popupOfferta_offerta)
        val testoOfferta: MaterialTextView =
            viewInflated.findViewById(R.id.popupOfferta_testoOfferta)
        val testoValore: MaterialTextView =
            viewInflated.findViewById(R.id.popupOfferta_valoreOfferta)
        val pulsanteOfferta: MaterialButton =
            viewInflated.findViewById(R.id.popupOfferta_inviaOfferta)

        valoreOfferta.addTextChangedListener {
            rimuoviErroreCampo(campoOfferta)
        }

        testoOfferta.text = when (viewModel.tipo.value!!) {
            TipoAsta.SILENZIOSA -> {
                getString(R.string.dettagliAsta_testoOfferta1)
            }

            TipoAsta.TEMPO_FISSO -> {
                getString(R.string.dettagliAsta_testoOfferta1)
            }

            TipoAsta.INVERSA -> {
                getString(R.string.dettagliAsta_testoOfferta2)
            }
        }

        testoValore.text = getString(
            R.string.placeholder_prezzo,
            if (viewModel.tipo.value != TipoAsta.SILENZIOSA) {
                viewModel.prezzo.value.toString()
            } else
                "???"
        )

        val dialog = MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(R.string.dettagliAsta_titoloPopupOfferta)
            .setView(viewInflated)
            .setNegativeButton(R.string.annulla) { _, _ -> }
            .show()

        pulsanteOfferta.setOnClickListener {
            controlloOfferta(campoOfferta, dialog)
        }
    }

    private fun controlloOfferta(campoOfferta: TextInputLayout, dialog: AlertDialog) {
        if (isPriceInvalid(campoOfferta.editText?.text.toString())) {
            when (viewModel.tipo.value!!) {
                TipoAsta.TEMPO_FISSO -> campoOfferta.error =
                    getString(R.string.dettagliAsta_erroreOffertaTempoFisso)

                TipoAsta.INVERSA -> campoOfferta.error =
                    getString(R.string.dettagliAsta_erroreOffertaInversa)

                TipoAsta.SILENZIOSA -> campoOfferta.error =
                    getString(R.string.dettagliAsta_erroreOffertaSilenziosa)
            }
        } else {
            lifecycleScope.launch {
                try {
                    val returned: Offerta = withContext(Dispatchers.IO) {
                        inviaOfferta(
                            campoOfferta.editText?.text.toString().toBigDecimal()
                        ).toOfferta()
                    }

                    when (returned.idOfferta) {
                        0L -> Snackbar.make(
                            fragmentView,
                            R.string.dettagliAsta_fallimentoOfferta,
                            Snackbar.LENGTH_SHORT
                        )
                            .setBackgroundTint(resources.getColor(R.color.blu, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()

                        else -> {
                            Snackbar.make(
                                fragmentView,
                                R.string.dettagliAsta_successoOfferta,
                                Snackbar.LENGTH_SHORT
                            )
                                .setBackgroundTint(resources.getColor(R.color.blu, null))
                                .setTextColor(resources.getColor(R.color.grigio, null))
                                .show()

                            dialog.dismiss()

                            listenerRefresh?.onRefresh(
                                viewModel.idAsta.value!!,
                                viewModel.tipo.value!!,
                                ControllerDettagliAsta::class
                            )
                        }
                    }
                } catch (_: Exception) {
                    Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(resources.getColor(R.color.blu, null))
                        .setTextColor(resources.getColor(R.color.grigio, null))
                        .show()
                }
            }
        }
    }

    private suspend fun inviaOfferta(valoreOfferta: BigDecimal): OffertaDto {
        return when (viewModel.tipo.value!!) {
            TipoAsta.TEMPO_FISSO -> {
                offertaTempoFissoRepository.inviaOffertaTempoFisso(
                    OffertaTempoFissoDto(
                        0L,
                        LocalDate.now(ZoneOffset.UTC),
                        LocalTime.now(ZoneOffset.UTC),
                        valoreOfferta,
                        AccountShallowDto(
                            CurrentUser.id,
                            "Compratore"
                        ),
                        AstaShallowDto(
                            viewModel.idAsta.value!!,
                            "AstaDiVenditore",
                            "AstaTempoFisso"
                        )
                    )
                )
            }

            TipoAsta.INVERSA -> {
                offertaInversaRepository.inviaOffertaInversa(
                    OffertaInversaDto(
                        0L,
                        LocalDate.now(ZoneOffset.UTC),
                        LocalTime.now(ZoneOffset.UTC),
                        valoreOfferta,
                        AccountShallowDto(
                            CurrentUser.id,
                            "Venditore"
                        ),
                        AstaShallowDto(
                            viewModel.idAsta.value!!,
                            "AstaDiCompratore",
                            "AstaInversa"
                        )
                    )
                )
            }

            TipoAsta.SILENZIOSA -> {
                offertaSilenziosaRepository.inviaOffertaSilenziosa(
                    OffertaSilenziosaDto(
                        0L,
                        LocalDate.now(ZoneOffset.UTC),
                        LocalTime.now(ZoneOffset.UTC),
                        valoreOfferta,
                        AccountShallowDto(
                            CurrentUser.id,
                            "Compratore"
                        ),
                        StatoOfferta.PENDING.name,
                        AstaShallowDto(
                            viewModel.idAsta.value!!,
                            "AstaDiVenditore",
                            "AstaSilenziosa"
                        )
                    )
                )
            }
        }
    }

    private fun isPriceInvalid(price: String): Boolean {
        val bigDecimal = if (price.isEmpty()) BigDecimal("0.00") else price.toBigDecimal()
        val regex = Regex("^\\d+(\\.\\d{1,2})?\$")

        return when (viewModel.tipo.value!!) {
            TipoAsta.TEMPO_FISSO -> {
                bigDecimal <= viewModel.prezzo.value!! || bigDecimal <= BigDecimal("0.00") || !price.matches(
                    regex
                )
            }

            TipoAsta.INVERSA -> {
                bigDecimal >= viewModel.prezzo.value!! || bigDecimal <= BigDecimal("0.00") || !price.matches(
                    regex
                )
            }

            TipoAsta.SILENZIOSA -> {
                bigDecimal < BigDecimal("0.00") || !price.matches(regex)
            }
        }
    }

    @EventHandler
    private fun clickModifica() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                logger.scriviLog("Editing auction")
            }
        }

        listenerEditButton?.onEditButton(
            viewModel.idAsta.value!!,
            viewModel.tipo.value!!,
            this::class
        )
    }

    @EventHandler
    private fun clickElimina() {
        MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(R.string.elimina_titoloConfermaElimina)
            .setMessage(R.string.elimina_testoConfermaElimina)
            .setPositiveButton(R.string.ok) { _, _ ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        logger.scriviLog("Deleting auction")
                    }
                }

                clickConferma()
            }
            .setNegativeButton(R.string.annulla) { _, _ -> }
            .show()
    }

    private fun clickConferma() {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) { eliminaAsta() }

                Snackbar.make(
                    fragmentView,
                    R.string.dettagliAsta_successoEliminazione,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                listenerBackButton?.onBackButton()
            } catch (exception: Exception) {
                Snackbar.make(
                    fragmentView,
                    R.string.dettagliAsta_erroreEliminazione,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    private suspend fun eliminaAsta() {
        when (viewModel.tipo.value!!) {
            TipoAsta.INVERSA -> {
                astaInversaRepository.eliminaAstaInversa(viewModel.idAsta.value!!)
            }

            TipoAsta.TEMPO_FISSO -> {
                astaTempoFissoRepository.eliminaAstaTempoFisso(viewModel.idAsta.value!!)
            }

            TipoAsta.SILENZIOSA -> {
                astaSilenziosaRepository.eliminaAstaSilenziosa(viewModel.idAsta.value!!)
            }
        }
    }

    @EventHandler
    private fun clickOfferte() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                logger.scriviLog("Showing auction bids")
            }
        }

        listenerBids?.onGoToBids(viewModel.idAsta.value!!, viewModel.tipo.value!!, this::class)
    }
}
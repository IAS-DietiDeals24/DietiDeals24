package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.DettagliastaBinding
import com.iasdietideals24.dietideals24.model.ModelAsta
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.data.Asta
import com.iasdietideals24.dietideals24.utilities.data.Offerta
import com.iasdietideals24.dietideals24.utilities.data.Profilo
import com.iasdietideals24.dietideals24.utilities.dto.AstaDto
import com.iasdietideals24.dietideals24.utilities.dto.OffertaDto
import com.iasdietideals24.dietideals24.utilities.dto.OffertaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.dto.ProfiloDto
import com.iasdietideals24.dietideals24.utilities.enumerations.CategoriaAsta
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import com.iasdietideals24.dietideals24.utilities.kscripts.OnBackButton
import com.iasdietideals24.dietideals24.utilities.kscripts.OnEditButton
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToBids
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToProfile
import com.iasdietideals24.dietideals24.utilities.kscripts.OnRefresh
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.repositories.ProfiloRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import com.iasdietideals24.dietideals24.utilities.tools.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

class ControllerDettagliAsta : Controller<DettagliastaBinding>() {

    // Navigation
    private val args: ControllerDettagliAstaArgs by navArgs()

    // ViewModel
    private val viewModel: ModelAsta by activityViewModels()

    // Repositiories
    private val astaTempoFissoRepository: AstaTempoFissoRepository by inject()
    private val astaInversaRepository: AstaInversaRepository by inject()
    private val astaSilenziosaRepository: AstaSilenziosaRepository by inject()
    private val profiloRepository: ProfiloRepository by inject()
    private val offertaInversaRepository: OffertaInversaRepository by inject()
    private val offertaTempoFissoRepository: OffertaTempoFissoRepository by inject()
    private val offertaSilenziosaRepository: OffertaSilenziosaRepository by inject()

    // Listeners
    private var listenerBackButton: OnBackButton? = null
    private var listenerProfile: OnGoToProfile? = null
    private var listenerEditButton: OnEditButton? = null
    private var listenerBids: OnGoToBids? = null
    private var listenerRefresh: OnRefresh? = null

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
    }

    override fun onDetach() {
        super.onDetach()

        listenerBackButton = null
        listenerProfile = null
        listenerEditButton = null
        listenerBids = null
        listenerRefresh = null
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
                val creatoreAsta: Profilo =
                    withContext(Dispatchers.IO) { caricaProfilo(asta.idCreatore).toProfilo() }
                val offerta: Offerta =
                    withContext(Dispatchers.IO) {
                        recuperaOfferta(asta.idAsta, asta.tipo).toOfferta()
                    }

                if (asta.idAsta != 0L && creatoreAsta.nomeUtente != "" &&
                    (offerta.offerta != BigDecimal(0.0) && asta.tipo != TipoAsta.SILENZIOSA) ||
                    (asta.tipo == TipoAsta.SILENZIOSA)
                ) {
                    viewModel.idAsta.value = asta.idAsta
                    viewModel.idCreatore.value = asta.idCreatore
                    viewModel.nomeCreatore.value = creatoreAsta.nome
                    viewModel.tipo.value = asta.tipo
                    viewModel.dataFine.value = asta.dataFine
                    viewModel.oraFine.value = asta.oraFine
                    viewModel.immagine.value = asta.immagine
                    viewModel.nome.value = asta.nome
                    viewModel.categoria.value = asta.categoria
                    viewModel.descrizione.value = asta.descrizione
                    viewModel.prezzo.value = offerta.offerta

                    binding.dettagliAstaOfferta.text = getString(
                        R.string.placeholder_prezzo,
                        if (viewModel.tipo.value != TipoAsta.SILENZIOSA)
                            offerta.offerta.toString()
                        else
                            "???"
                    )

                    if (creatoreAsta.immagineProfilo.isNotEmpty()) {
                        binding.dettagliAstaCampoFoto.load(creatoreAsta.immagineProfilo) {
                            crossfade(true)
                        }
                    }

                    if (CurrentUser.id == "") {
                        binding.dettagliAstaPulsanteOfferta.isEnabled = false
                    } else if (CurrentUser.id == asta.idCreatore) {
                        binding.dettagliAstaPulsanteOfferta.visibility = View.GONE
                        binding.dettagliAstaPulsanteModifica.visibility = View.GONE
                        binding.dettagliAstaPulsanteElimina.visibility = View.GONE
                        binding.dettagliAstaPulsanteElencoOfferte.visibility = View.GONE
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

    private suspend fun caricaProfilo(idCreatore: String): ProfiloDto {
        return profiloRepository.caricaProfiloDaAccount(idCreatore)
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
        val tipoObserver = Observer<TipoAsta> { newTipoAsta ->
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
            binding.dettagliAstaCategoria.text = newCategoria.toString()
        }
        viewModel.categoria.observe(viewLifecycleOwner, categoriaObserver)

        val immagineObserver = Observer<ByteArray> { newByteArray ->
            if (newByteArray.isNotEmpty()) {
                binding.dettagliAstaCampoFoto.load(newByteArray) {
                    crossfade(true)
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
        Logger.log("Showing user profile")

        listenerProfile?.onGoToProfile(viewModel.idCreatore.value!!, this::class)
    }

    @EventHandler
    private fun clickOfferta() {
        Logger.log("Sending a bid")

        val viewInflated: View = LayoutInflater.from(context)
            .inflate(R.layout.popupofferta, view as ViewGroup?, false)
        val input: TextInputEditText = viewInflated.findViewById(R.id.popupOfferta_offerta)
        val testoOfferta: MaterialTextView =
            viewInflated.findViewById(R.id.popupOfferta_testoOfferta)
        val testoValore: MaterialTextView =
            viewInflated.findViewById(R.id.popupOfferta_valoreOfferta)
        input.addTextChangedListener {
            input.error = null
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

        MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(R.string.dettagliAsta_titoloPopupOfferta)
            .setView(viewInflated)
            .setPositiveButton(R.string.ok) { _, _ ->
                clickPositiveButton(input)
            }
            .setNegativeButton(R.string.annulla) { _, _ -> }
            .show()
    }

    private fun clickPositiveButton(input: TextInputEditText) {
        if (isPriceInvalid(input.text.toString())) {
            when (viewModel.tipo.value!!) {
                TipoAsta.TEMPO_FISSO -> input.error =
                    getString(R.string.dettagliAsta_erroreOffertaTempoFisso)

                TipoAsta.INVERSA -> input.error =
                    getString(R.string.dettagliAsta_erroreOffertaInversa)

                TipoAsta.SILENZIOSA -> input.error =
                    getString(R.string.dettagliAsta_erroreOffertaSilenziosa)
            }
        } else {
            lifecycleScope.launch {
                try {
                    val returned: Offerta = withContext(Dispatchers.IO) {
                        inviaOfferta(
                            Offerta(
                                0L,
                                0L,
                                CurrentUser.id,
                                input.text.toString().toBigDecimal(),
                                LocalDate.now(),
                                LocalTime.now()
                            )
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

                            listenerRefresh?.onRefresh(
                                viewModel.idAsta.value!!,
                                viewModel.tipo.value!!,
                                this::class
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

    private suspend fun inviaOfferta(offerta: Offerta): OffertaDto {
        return when (viewModel.tipo.value!!) {
            TipoAsta.TEMPO_FISSO -> {
                offertaTempoFissoRepository.inviaOffertaTempoFisso(
                    offerta.toOffertaTempoFisso(),
                    viewModel.idAsta.value!!
                )
            }

            TipoAsta.INVERSA -> {
                offertaInversaRepository.inviaOffertaInversa(
                    offerta.toOffertaInversa(),
                    viewModel.idAsta.value!!
                )
            }

            TipoAsta.SILENZIOSA -> {
                offertaSilenziosaRepository.inviaOffertaSilenziosa(
                    offerta.toOffertaSilenziosa(),
                    viewModel.idAsta.value!!
                )
            }
        }
    }

    private fun isPriceInvalid(price: String): Boolean {
        val bigDecimal = price.toBigDecimal()
        val regex = Regex("^\\d+\\.\\d{2}\$")

        return when (viewModel.tipo.value!!) {
            TipoAsta.TEMPO_FISSO -> {
                bigDecimal <= viewModel.prezzo.value!! || bigDecimal < BigDecimal(0.0) || !price.matches(
                    regex
                )
            }

            TipoAsta.INVERSA -> {
                bigDecimal >= viewModel.prezzo.value!! || bigDecimal < BigDecimal(0.0) || !price.matches(
                    regex
                )
            }

            TipoAsta.SILENZIOSA -> {
                bigDecimal < BigDecimal(0.0) || !price.matches(regex)
            }
        }
    }

    @EventHandler
    private fun clickModifica() {
        Logger.log("Editing auction")

        listenerEditButton?.onEditButton(sender = this::class)
    }

    @EventHandler
    private fun clickElimina() {
        MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(R.string.elimina_titoloConfermaElimina)
            .setMessage(R.string.elimina_testoConfermaElimina)
            .setPositiveButton(R.string.ok) { _, _ ->
                Logger.log("Deleting auction")

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
        Logger.log("Showing auction bids")

        listenerBids?.onGoToBids(viewModel.idAsta.value!!, viewModel.tipo.value!!, this::class)
    }
}
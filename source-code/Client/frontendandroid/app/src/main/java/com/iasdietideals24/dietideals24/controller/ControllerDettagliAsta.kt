package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.data.DettagliAsta
import com.iasdietideals24.dietideals24.utilities.classes.data.Offerta
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.interfaces.OnBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnEditButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToBids
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile
import com.iasdietideals24.dietideals24.utilities.interfaces.OnRefresh
import java.time.LocalDate
import java.time.LocalTime

class ControllerDettagliAsta : Controller<DettagliastaBinding>() {

    private val tempoFisso = "Tempo fisso"
    private val inversa = "Inversa"
    private val silenziosa = "Silenziosa"

    private val args: ControllerDettagliAstaArgs by navArgs()
    private lateinit var viewModel: ModelAsta

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

        binding.dettagliAstaNomeUtente.setOnClickListener { clickNomeUtente() }

        binding.dettagliAstaPulsanteOfferta.setOnClickListener { clickOfferta() }

        binding.dettagliAstaPulsanteModifica.setOnClickListener { clickModifica() }

        binding.dettagliAstaPulsanteElimina.setOnClickListener { clickElimina() }

        binding.dettagliAstaPulsanteElencoOfferte.setOnClickListener { clickOfferte() }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity)[ModelAsta::class]

        val asta: DettagliAsta? = eseguiChiamataREST(
            "caricaAsta",
            if (args.id == 0L) viewModel.idAsta.value!! else args.id
        )

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

            if (CurrentUser.id == 0L) {
                binding.dettagliAstaPulsanteOfferta.isEnabled = false
            } else if (CurrentUser.id == asta.idCreatore) {
                binding.dettagliAstaPulsanteOfferta.visibility = View.GONE
                binding.dettagliAstaPulsanteModifica.visibility = View.GONE
                binding.dettagliAstaPulsanteElimina.visibility = View.GONE
                binding.dettagliAstaPulsanteElencoOfferte.visibility = View.GONE
            }
        } else {
            Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.blu, null))
                .setTextColor(resources.getColor(R.color.grigio, null))
                .show()
        }
    }

    @UIBuilder
    override fun impostaOsservatori() {
        val tipoAstaObserver = Observer<String> { newTipoAsta ->
            binding.dettagliAstaTipoAsta.text = getString(
                R.string.crea_tipoAsta,
                when (newTipoAsta) {
                    silenziosa -> {
                        getString(R.string.tipoAsta_astaSilenziosa)
                    }

                    tempoFisso -> {
                        getString(R.string.tipoAsta_astaTempoFisso)
                    }

                    inversa -> {
                        getString(R.string.tipoAsta_astaInversa)
                    }

                    else -> ""
                }
            )
        }
        viewModel.tipo.observe(viewLifecycleOwner, tipoAstaObserver)

        val dataObserver = Observer<LocalDate> { newData ->
            binding.dettagliAstaDataScadenza.text = newData.toLocalStringShort()
        }
        viewModel.dataFine.observe(viewLifecycleOwner, dataObserver)

        val oraObserver = Observer<LocalTime> { newOra ->
            binding.dettagliAstaOraScadenza.text = newOra.toString()
        }
        viewModel.oraFine.observe(viewLifecycleOwner, oraObserver)

        val nomeObserver = Observer<String> { newNomeUtente ->
            binding.dettagliAstaNomeUtente.text = newNomeUtente
        }
        viewModel.nome.observe(viewLifecycleOwner, nomeObserver)

        val categoriaObserver = Observer<String> { newCategoria ->
            binding.dettagliAstaCategoria.text = newCategoria
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

        val prezzoObserver = Observer<Double> { newPrezzo ->
            binding.dettagliAstaOfferta.text = getString(
                R.string.placeholder_prezzo,
                if (viewModel.tipo.value != silenziosa)
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
                when (viewModel.tipo.value) {
                    silenziosa -> getString(R.string.aiuto_titoloAstaSilenziosa)
                    tempoFisso -> getString(R.string.aiuto_titoloAstaTempoFisso)
                    inversa -> getString(R.string.aiuto_titoloAstaInversa)
                    else -> ""
                }
            )
            .setIcon(R.drawable.icona_aiuto_arancione)
            .setMessage(
                when (viewModel.tipo.value) {
                    silenziosa -> getString(R.string.aiuto_astaSilenziosa)
                    tempoFisso -> getString(R.string.aiuto_astaTempoFisso)
                    inversa -> getString(R.string.aiuto_astaInversa)
                    else -> ""
                }
            )
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
            .show()
    }

    @EventHandler
    private fun clickImmagine() {
        listenerProfile?.onGoToProfile(viewModel.idCreatore.value!!, this::class)
    }

    @EventHandler
    private fun clickNomeUtente() {
        listenerProfile?.onGoToProfile(viewModel.idCreatore.value!!, this::class)
    }

    @EventHandler
    private fun clickOfferta() {
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
        testoOfferta.text = when (viewModel.tipo.value) {
            silenziosa -> {
                getString(R.string.dettagliAsta_testoOfferta1)
            }

            tempoFisso -> {
                getString(R.string.dettagliAsta_testoOfferta1)
            }

            inversa -> {
                getString(R.string.dettagliAsta_testoOfferta2)
            }

            else -> ""
        }
        testoValore.text = getString(
            R.string.placeholder_prezzo,
            if (viewModel.tipo.value != silenziosa)
                viewModel.prezzo.value.toString()
            else
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
            when (viewModel.tipo.value) {
                tempoFisso -> input.error = getString(R.string.dettagliAsta_erroreOffertaTempoFisso)
                inversa -> input.error = getString(R.string.dettagliAsta_erroreOffertaInversa)
                silenziosa -> input.error = getString(R.string.dettagliAsta_erroreOffertaSilenziosa)
            }
        } else {
            val returned: Boolean? = eseguiChiamataREST(
                "inviaOfferta",
                Offerta(
                    args.id,
                    CurrentUser.id,
                    input.text.toString().toDouble(),
                    LocalDate.now(),
                    LocalTime.now()
                )
            )

            when (returned) {
                null -> Snackbar.make(
                    fragmentView,
                    R.string.apiError,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                true -> {
                    Snackbar.make(
                        fragmentView,
                        R.string.dettagliAsta_successoOfferta,
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(resources.getColor(R.color.blu, null))
                        .setTextColor(resources.getColor(R.color.grigio, null))
                        .show()

                    listenerRefresh?.onRefresh(viewModel.idAsta.value!!, this::class)
                }

                else -> Snackbar.make(
                    fragmentView,
                    R.string.dettagliAsta_fallimentoOfferta,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    private fun isPriceInvalid(price: String): Boolean {
        val double = price.toDouble()
        val regex = Regex("^\\d+\\.\\d{2}\$")

        return when (viewModel.tipo.value) {
            tempoFisso -> {
                double <= viewModel.prezzo.value!! || double < 0.0 || !price.matches(regex)
            }

            inversa -> {
                double >= viewModel.prezzo.value!! || double < 0.0 || !price.matches(regex)
            }

            silenziosa -> {
                double < 0.0 || !price.matches(regex)
            }

            else -> true
        }
    }

    @EventHandler
    private fun clickModifica() {
        listenerEditButton?.onEditButton(this::class)
    }

    @EventHandler
    private fun clickElimina() {
        val returned: Boolean? = eseguiChiamataREST("eliminaAsta", viewModel.idAsta.value)

        when (returned) {
            null -> Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.blu, null))
                .setTextColor(resources.getColor(R.color.grigio, null))
                .show()

            true -> {
                Snackbar.make(
                    fragmentView,
                    R.string.dettagliAsta_successoEliminazione,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                listenerBackButton?.onBackButton()
            }

            false -> {
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

    @EventHandler
    private fun clickOfferte() {
        listenerBids?.onGoToBids(viewModel.idAsta.value!!, this::class)
    }
}
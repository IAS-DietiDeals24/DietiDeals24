package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.DettagliastaBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.data.DettagliAsta
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ControllerDettagliAsta : Controller<DettagliastaBinding>() {

    private val args: ControllerDettagliAstaArgs by navArgs()
    private var asta: DettagliAsta? = null

    private var listenerBackButton: OnFragmentBackButton? = null
    private var listenerCreatore: OnGoToProfile? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnFragmentBackButton) {
            listenerBackButton = requireContext() as OnFragmentBackButton
        }
        if (requireContext() is OnGoToProfile) {
            listenerCreatore = requireContext() as OnGoToProfile
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerBackButton = null
        listenerCreatore = null
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        asta = eseguiChiamataREST("caricaAsta", args.id)

        if (asta != null) {
            val stringaTipoAsta: String = when (asta!!._datiAnteprimaAsta._tipoAsta) {
                "Silenziosa" -> getString(R.string.tipoAsta_astaSilenziosa)
                "Tempo fisso" -> getString(R.string.tipoAsta_astaTempoFisso)
                "Inversa" -> getString(R.string.tipoAsta_astaInversa)
                else -> ""
            }
            binding.dettagliAstaTipoAsta.text = getString(R.string.crea_tipoAsta, stringaTipoAsta)
            binding.dettagliAstaDataScadenza.text = asta!!._datiAnteprimaAsta._dataScadenza.format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
            )
            binding.dettagliAstaOraScadenza.text = asta!!._datiAnteprimaAsta._oraScadenza.toString()
            binding.dettagliAstaCampoFoto.load(asta!!._datiAnteprimaAsta._foto) {
                crossfade(true)
            }
            binding.dettagliAstaNome.text = asta!!._datiAnteprimaAsta._nome
            binding.dettagliAstaCategoria.text = asta!!._categoria
            binding.dettagliAstaNomeUtente.text = asta!!._nomeCreatore
            binding.dettagliAstaDescrizione.text = asta!!._descrizione
            binding.dettagliAstaTestoOfferta.text = when (asta!!._datiAnteprimaAsta._tipoAsta) {
                "Silenziosa" -> getString(R.string.dettagliAsta_testoOfferta1)
                "Tempo fisso" -> getString(R.string.dettagliAsta_testoOfferta1)
                "Inversa" -> getString(R.string.dettagliAsta_testoOfferta2)
                else -> ""
            }
            binding.dettagliAstaOfferta.text = asta!!._datiAnteprimaAsta._offerta.toString()
        } else {
            Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.blu, null))
                .setTextColor(resources.getColor(R.color.grigio, null))
                .show()
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.dettagliAstaPulsanteIndietro.setOnClickListener {
            listenerBackButton?.onFragmentBackButton()
        }

        binding.dettagliAstaPulsanteAiuto.setOnClickListener {
            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(
                    when (asta?._datiAnteprimaAsta?._tipoAsta) {
                        "Silenziosa" -> getString(R.string.aiuto_titoloAstaSilenziosa)
                        "Tempo fisso" -> getString(R.string.aiuto_titoloAstaTempoFisso)
                        "Inversa" -> getString(R.string.aiuto_titoloAstaInversa)
                        else -> ""
                    }
                )
                .setIcon(R.drawable.icona_aiuto_arancione)
                .setMessage(
                    when (asta?._datiAnteprimaAsta?._tipoAsta) {
                        "Silenziosa" -> getString(R.string.aiuto_astaSilenziosa)
                        "Tempo fisso" -> getString(R.string.aiuto_astaTempoFisso)
                        "Inversa" -> getString(R.string.aiuto_astaInversa)
                        else -> ""
                    }
                )
                .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
                .show()
        }

        binding.dettagliAstaImmagineUtente.setOnClickListener {
            if (asta != null)
                listenerCreatore?.onGoToProfile(asta!!._idCreatore, this::class)
        }

        binding.dettagliAstaNomeUtente.setOnClickListener {
            if (asta != null)
                listenerCreatore?.onGoToProfile(asta!!._idCreatore, this::class)
        }
    }
}
package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.data.DettagliAsta
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ControllerDettagliAsta : Controller(R.layout.dettagliasta) {

    private val args: ControllerDettagliAstaArgs by navArgs()
    private var asta: DettagliAsta? = null

    private lateinit var pulsanteIndietro: ImageButton
    private lateinit var pulsanteAiuto: ImageButton
    private lateinit var tipoAsta: MaterialTextView
    private lateinit var dataScadenza: MaterialTextView
    private lateinit var oraScadenza: MaterialTextView
    private lateinit var immagine: ShapeableImageView
    private lateinit var nome: MaterialTextView
    private lateinit var categoria: MaterialTextView
    private lateinit var immagineCreatore: ShapeableImageView
    private lateinit var nomeCreatore: MaterialTextView
    private lateinit var descrizione: MaterialTextView
    private lateinit var testoOfferta: MaterialTextView
    private lateinit var offerta: MaterialTextView

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
    override fun trovaElementiInterfaccia() {
        pulsanteIndietro = fragmentView.findViewById(R.id.dettagliAsta_pulsanteIndietro)
        pulsanteAiuto = fragmentView.findViewById(R.id.dettagliAsta_pulsanteAiuto)
        tipoAsta = fragmentView.findViewById(R.id.dettagliAsta_tipoAsta)
        dataScadenza = fragmentView.findViewById(R.id.dettagliAsta_dataScadenza)
        oraScadenza = fragmentView.findViewById(R.id.dettagliAsta_oraScadenza)
        immagine = fragmentView.findViewById(R.id.dettagliAsta_campoFoto)
        nome = fragmentView.findViewById(R.id.dettagliAsta_nome)
        categoria = fragmentView.findViewById(R.id.dettagliAsta_categoria)
        immagineCreatore = fragmentView.findViewById(R.id.dettagliAsta_immagineUtente)
        nomeCreatore = fragmentView.findViewById(R.id.dettagliAsta_nomeUtente)
        descrizione = fragmentView.findViewById(R.id.dettagliAsta_descrizione)
        testoOfferta = fragmentView.findViewById(R.id.dettagliAsta_testoOfferta)
        offerta = fragmentView.findViewById(R.id.dettagliAsta_offerta)
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
            tipoAsta.text = getString(R.string.crea_tipoAsta, stringaTipoAsta)
            dataScadenza.text = asta!!._datiAnteprimaAsta._dataScadenza.format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
            )
            oraScadenza.text = asta!!._datiAnteprimaAsta._oraScadenza.toString()
            immagine.load(asta!!._datiAnteprimaAsta._foto) {
                crossfade(true)
            }
            nome.text = asta!!._datiAnteprimaAsta._nome
            categoria.text = asta!!._categoria
            nomeCreatore.text = asta!!._nomeCreatore
            descrizione.text = asta!!._descrizione
            testoOfferta.text = when (asta!!._datiAnteprimaAsta._tipoAsta) {
                "Silenziosa" -> getString(R.string.dettagliAsta_testoOfferta1)
                "Tempo fisso" -> getString(R.string.dettagliAsta_testoOfferta1)
                "Inversa" -> getString(R.string.dettagliAsta_testoOfferta2)
                else -> ""
            }
            offerta.text = asta!!._datiAnteprimaAsta._offerta.toString()
        } else {
            Toast.makeText(
                fragmentContext,
                getString(R.string.apiError),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        pulsanteIndietro.setOnClickListener {
            listenerBackButton?.onFragmentBackButton()
        }

        pulsanteAiuto.setOnClickListener {
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

        immagineCreatore.setOnClickListener {
            if (asta != null)
                listenerCreatore?.onGoToProfile(asta!!._idCreatore, this::class)
        }

        nomeCreatore.setOnClickListener {
            if (asta != null)
                listenerCreatore?.onGoToProfile(asta!!._idCreatore, this::class)
        }
    }
}
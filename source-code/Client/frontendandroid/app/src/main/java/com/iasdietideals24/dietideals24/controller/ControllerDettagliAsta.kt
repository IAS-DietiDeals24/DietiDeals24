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
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.data.DettagliAsta
import com.iasdietideals24.dietideals24.utilities.classes.data.Offerta
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentEditButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile
import java.time.LocalDate
import java.time.LocalTime

class ControllerDettagliAsta : Controller<DettagliastaBinding>() {

    private val args: ControllerDettagliAstaArgs by navArgs()
    private lateinit var viewModel: ModelAsta

    private var listenerBackButton: OnFragmentBackButton? = null
    private var listenerCreatore: OnGoToProfile? = null
    private var listenerModifica: OnFragmentEditButton? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnFragmentBackButton) {
            listenerBackButton = requireContext() as OnFragmentBackButton
        }
        if (requireContext() is OnGoToProfile) {
            listenerCreatore = requireContext() as OnGoToProfile
        }
        if (requireContext() is OnFragmentEditButton) {
            listenerModifica = requireContext() as OnFragmentEditButton
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerBackButton = null
        listenerCreatore = null
        listenerModifica = null
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.dettagliAstaPulsanteIndietro.setOnClickListener {
            viewModel.clear()

            listenerBackButton?.onFragmentBackButton()
        }

        binding.dettagliAstaPulsanteAiuto.setOnClickListener {
            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(
                    when (viewModel.tipo.value) {
                        "Silenziosa" -> getString(R.string.aiuto_titoloAstaSilenziosa)
                        "Tempo fisso" -> getString(R.string.aiuto_titoloAstaTempoFisso)
                        "Inversa" -> getString(R.string.aiuto_titoloAstaInversa)
                        else -> ""
                    }
                )
                .setIcon(R.drawable.icona_aiuto_arancione)
                .setMessage(
                    when (viewModel.tipo.value) {
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
            listenerCreatore?.onGoToProfile(viewModel.idCreatore.value!!, this::class)
        }

        binding.dettagliAstaNomeUtente.setOnClickListener {
            listenerCreatore?.onGoToProfile(viewModel.idCreatore.value!!, this::class)
        }

        binding.dettagliAstaPulsanteOfferta.setOnClickListener {
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
                "Silenziosa" -> {
                    getString(R.string.dettagliAsta_testoOfferta1)
                }

                "Tempo fisso" -> {
                    getString(R.string.dettagliAsta_testoOfferta1)
                }

                "Inversa" -> {
                    getString(R.string.dettagliAsta_testoOfferta2)
                }

                else -> ""
            }
            testoValore.text = if (viewModel.tipo.value != "Silenziosa")
                viewModel.prezzo.value.toString()
            else
                "???"

            MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
                .setTitle(R.string.dettagliAsta_titoloPopupOfferta)
                .setView(viewInflated)
                .setPositiveButton(R.string.ok) { _, _ ->
                    if (isPriceInvalid(input.text.toString()))
                        input.error = getString(R.string.dettagliAsta_erroreOffertaTempoFisso)
                    else if (isPriceInvalid(input.text.toString()))
                        input.error = getString(R.string.dettagliAsta_erroreOffertaInversa)
                    else if (isPriceInvalid(input.text.toString()))
                        input.error = getString(R.string.dettagliAsta_erroreOffertaSilenziosa)
                    else {
                        val returned: Boolean? = eseguiChiamataREST(
                            "inviaOfferta",
                            Offerta(args.id, CurrentUser.id, input.text.toString().toDouble())
                        )

                        if (returned == null)
                            Snackbar.make(
                                fragmentView,
                                R.string.apiError,
                                Snackbar.LENGTH_SHORT
                            )
                                .setBackgroundTint(resources.getColor(R.color.blu, null))
                                .setTextColor(resources.getColor(R.color.grigio, null))
                                .show()
                        else if (returned == true)
                            Snackbar.make(
                                fragmentView,
                                R.string.dettagliAsta_successoOfferta,
                                Snackbar.LENGTH_SHORT
                            )
                                .setBackgroundTint(resources.getColor(R.color.blu, null))
                                .setTextColor(resources.getColor(R.color.grigio, null))
                                .show()
                        else
                            Snackbar.make(
                                fragmentView,
                                R.string.dettagliAsta_fallimentoOfferta,
                                Snackbar.LENGTH_SHORT
                            )
                                .setBackgroundTint(resources.getColor(R.color.blu, null))
                                .setTextColor(resources.getColor(R.color.grigio, null))
                                .show()
                    }
                }
                .setNegativeButton(R.string.annulla) { _, _ -> }
                .show()
        }

        binding.dettagliAstaPulsanteModifica.setOnClickListener {
            listenerModifica?.onFragmentEditButton(this::class)
        }

        binding.dettagliAstaPulsanteElimina.setOnClickListener {
            val returned: Boolean? = eseguiChiamataREST("eliminaAsta", viewModel.idAsta.value)

            if (returned == null)
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            else if (returned == true) {
                Snackbar.make(
                    fragmentView,
                    R.string.dettagliAsta_successoEliminazione,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                listenerBackButton?.onFragmentBackButton()
            } else if (returned == false) {
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

        binding.dettagliAstaPulsanteElencoOfferte.setOnClickListener {
            //TODO
        }
    }

    private fun isPriceInvalid(price: String): Boolean {
        val double = price.toDouble()

        when (viewModel.tipo.value) {
            "Tempo fisso" -> {
                return double <= viewModel.prezzo.value!! || double < 0.0 || !price.matches(Regex("^\\d+\\.\\d{2}\$"))
            }

            "Inversa" -> {
                return double >= viewModel.prezzo.value!! || double < 0.0 || !price.matches(Regex("^\\d+\\.\\d{2}\$"))
            }

            "Silenziosa" -> {
                return double < 0.0 || !price.matches(Regex("^\\d+\\.\\d{2}\$"))
            }
        }

        return true
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity).get(ModelAsta::class)

        val asta: DettagliAsta? = eseguiChiamataREST("caricaAsta", args.id)

        if (asta != null) {
            viewModel.idAsta.value = asta._anteprimaAsta._id
            viewModel.tipo.value = asta._anteprimaAsta._tipoAsta
            viewModel.dataFine.value = asta._anteprimaAsta._dataScadenza
            viewModel.oraFine.value = asta._anteprimaAsta._oraScadenza
            viewModel.prezzo.value = asta._anteprimaAsta._offerta
            viewModel.immagine.value = asta._anteprimaAsta._foto
            viewModel.nome.value = asta._anteprimaAsta._nome
            viewModel.categoria.value = asta._categoria
            viewModel.descrizione.value = asta._descrizione

            if (CurrentUser.id == 0L) {
                binding.dettagliAstaPulsanteOfferta.isEnabled = false
            } else if (CurrentUser.id == asta._idCreatore) {
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
                    "Silenziosa" -> {
                        getString(R.string.tipoAsta_astaSilenziosa)
                    }

                    "Tempo fisso" -> {
                        getString(R.string.tipoAsta_astaTempoFisso)
                    }

                    "Inversa" -> {
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

        val immagineObserver = Observer<ByteArray> { newImmagine ->
            if (newImmagine.isNotEmpty())
                binding.dettagliAstaImmagineUtente.load(newImmagine) {
                    crossfade(true)
                }
        }
        viewModel.immagine.observe(viewLifecycleOwner, immagineObserver)

        val descrizioneObserver = Observer<String> { newDescrizione ->
            binding.dettagliAstaDescrizione.text = newDescrizione
        }
        viewModel.descrizione.observe(viewLifecycleOwner, descrizioneObserver)

        val prezzoObserver = Observer<Double> { newPrezzo ->
            binding.dettagliAstaOfferta.text = newPrezzo.toString()
        }
        viewModel.prezzo.observe(viewLifecycleOwner, prezzoObserver)
    }
}
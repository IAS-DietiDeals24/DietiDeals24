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
import com.iasdietideals24.dietideals24.utilities.annotations.Utility
import com.iasdietideals24.dietideals24.utilities.classes.APIController
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.Logger
import com.iasdietideals24.dietideals24.utilities.classes.TipoAccount
import com.iasdietideals24.dietideals24.utilities.classes.TipoAsta
import com.iasdietideals24.dietideals24.utilities.classes.data.Asta
import com.iasdietideals24.dietideals24.utilities.classes.data.Offerta
import com.iasdietideals24.dietideals24.utilities.classes.data.Profilo
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.interfaces.OnBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnEditButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToBids
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile
import com.iasdietideals24.dietideals24.utilities.interfaces.OnRefresh
import retrofit2.Call
import retrofit2.Response
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

class ControllerDettagliAsta : Controller<DettagliastaBinding>() {

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

        binding.dettagliAstaNomeUtente.setOnClickListener { clickImmagine() }

        binding.dettagliAstaPulsanteOfferta.setOnClickListener { clickOfferta() }

        binding.dettagliAstaPulsanteModifica.setOnClickListener { clickModifica() }

        binding.dettagliAstaPulsanteElimina.setOnClickListener { clickElimina() }

        binding.dettagliAstaPulsanteElencoOfferte.setOnClickListener { clickOfferte() }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity)[ModelAsta::class]

        val asta: Asta = caricaAsta()
        val creatoreAsta: Profilo = caricaProfilo(asta.idCreatore)
        val offerta: Offerta = recuperaOfferta(asta.idAsta, asta.tipo)

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
        } else
            Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.blu, null))
                .setTextColor(resources.getColor(R.color.grigio, null))
                .show()
    }

    private fun caricaAsta(): Asta {
        return when (args.tipo) {
            TipoAsta.INVERSA -> {
                val call = APIController.instance.caricaAstaInversa(args.id)
                chiamaAPI(call).toAsta()
            }

            TipoAsta.SILENZIOSA -> {
                val call = APIController.instance.caricaAstaSilenziosa(args.id)
                chiamaAPI(call).toAsta()
            }

            TipoAsta.TEMPO_FISSO -> {
                val call = APIController.instance.caricaAstaTempoFisso(args.id)
                chiamaAPI(call).toAsta()
            }
        }
    }

    private fun caricaProfilo(idCreatore: String): Profilo {
        val call = APIController.instance.caricaProfiloDaAccount(idCreatore)

        return chiamaAPI(call).toProfilo()
    }

    private fun recuperaOfferta(idAsta: Long, tipo: TipoAsta): Offerta {
        return when (tipo) {
            TipoAsta.TEMPO_FISSO -> {
                val call = APIController.instance.recuperaOffertaPiuAlta(idAsta)
                chiamaAPI(call).toOfferta()
            }

            TipoAsta.INVERSA -> {
                val call = APIController.instance.recuperaOffertaPiuBassa(idAsta)
                chiamaAPI(call).toOfferta()
            }

            TipoAsta.SILENZIOSA -> Offerta()
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
            val returned: Offerta = inviaOfferta(
                Offerta(
                    0L,
                    0L,
                    CurrentUser.id,
                    input.text.toString().toBigDecimal(),
                    LocalDate.now(),
                    LocalTime.now()
                )
            )

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

                    listenerRefresh?.onRefresh(viewModel.idAsta.value!!, this::class)
                }
            }
        }
    }

    private fun inviaOfferta(offerta: Offerta): Offerta {
        return when (viewModel.tipo.value!!) {
            TipoAsta.TEMPO_FISSO -> {
                val call = APIController.instance.inviaOffertaTempoFisso(
                    offerta.toOffertaTempoFisso(),
                    viewModel.idAsta.value!!
                )
                chiamaAPI(call).toOfferta()
            }

            TipoAsta.INVERSA -> {
                val call = APIController.instance.inviaOffertaInversa(
                    offerta.toOffertaInversa(),
                    viewModel.idAsta.value!!
                )
                chiamaAPI(call).toOfferta()
            }

            TipoAsta.SILENZIOSA -> {
                val call = APIController.instance.inviaOffertaSilenziosa(
                    offerta.toOffertaSilenziosa(),
                    viewModel.idAsta.value!!
                )
                chiamaAPI(call).toOfferta()
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
        eliminaAsta()
    }

    private fun eliminaAsta() {
        val call = when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> {
                APIController.instance.eliminaAstaInversa(viewModel.idAsta.value!!)
            }

            TipoAccount.VENDITORE -> {
                APIController.instance.eliminaAstaTempoFisso(viewModel.idAsta.value!!)
                APIController.instance.eliminaAstaSilenziosa(viewModel.idAsta.value!!)
            }

            else -> return
        }

        chiamaAPI(call)
    }

    @EventHandler
    private fun clickOfferte() {
        Logger.log("Showing auction bids")

        listenerBids?.onGoToBids(viewModel.idAsta.value!!, viewModel.tipo.value!!, this::class)
    }

    @Utility
    override fun <Model> onRESTSuccess(call: Call<Model>, response: Response<Model>) {
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

    @Utility
    override fun <Model> onRESTUnsuccess(call: Call<Model>, response: Response<Model>) {
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
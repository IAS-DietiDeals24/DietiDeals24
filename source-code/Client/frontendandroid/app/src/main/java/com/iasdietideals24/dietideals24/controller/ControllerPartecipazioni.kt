package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.PartecipazioniBinding
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.APIController
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.TipoAccount
import com.iasdietideals24.dietideals24.utilities.classes.adapters.AdapterPartecipazioni
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.toArrayOfAnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.interfaces.OnBackButton

class ControllerPartecipazioni : Controller<PartecipazioniBinding>() {

    private var listenerBackButton: OnBackButton? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnBackButton) {
            listenerBackButton = requireContext() as OnBackButton
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerBackButton = null
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        binding.partecipazioniRecyclerView.layoutManager = LinearLayoutManager(fragmentContext)
        val result: Array<AnteprimaAsta> = recuperaPartecipazioni()

        if (result.isNotEmpty())
            binding.partecipazioniRecyclerView.adapter = AdapterPartecipazioni(result, resources)
        else
            Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.blu, null))
                .setTextColor(resources.getColor(R.color.grigio, null))
                .show()

    }

    private fun recuperaPartecipazioni(): Array<AnteprimaAsta> {
        return if (CurrentUser.tipoAccount === TipoAccount.COMPRATORE) {
            val call1 = APIController.instance.recuperaPartecipazioniSilenziose(CurrentUser.id)
            val call2 = APIController.instance.recuperaPartecipazioniTempoFisso(CurrentUser.id)
            chiamaAPI(call1).toArrayOfAnteprimaAsta()
                .plus(chiamaAPI(call2).toArrayOfAnteprimaAsta())
        } else {
            val call = APIController.instance.recuperaPartecipazioniInverse(CurrentUser.id)
            chiamaAPI(call).toArrayOfAnteprimaAsta()
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.partecipazioniPulsanteIndietro.setOnClickListener { clickIndietro() }
    }

    @EventHandler
    private fun clickIndietro() {
        listenerBackButton?.onBackButton()
    }
}
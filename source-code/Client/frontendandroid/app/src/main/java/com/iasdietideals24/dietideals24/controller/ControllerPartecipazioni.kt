package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.PartecipazioniBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.adapters.AdapterPartecipazioni
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaAsta
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
        val result: Array<AnteprimaAsta>? =
            eseguiChiamataREST("recuperaPartecipazioni", CurrentUser.id)

        if (result != null)
            binding.partecipazioniRecyclerView.adapter = AdapterPartecipazioni(result, resources)
        else
            Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.blu, null))
                .setTextColor(resources.getColor(R.color.grigio, null))
                .show()

    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.partecipazioniPulsanteIndietro.setOnClickListener { clickIndietro() }
    }

    private fun clickIndietro() {
        listenerBackButton?.onBackButton()
    }
}
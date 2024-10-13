package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.PartecipazioniBinding
import com.iasdietideals24.dietideals24.model.ModelPartecipazioni
import com.iasdietideals24.dietideals24.utilities.adapters.AdapterPartecipazioni
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.kscripts.OnBackButton
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.parameter.parametersOf

class ControllerPartecipazioni : Controller<PartecipazioniBinding>() {

    // ViewModel
    private val viewModel: ModelPartecipazioni by activityViewModel()

    // PagingAdapter
    private val adapterPartecipazioni: AdapterPartecipazioni by inject { parametersOf(resources) }

    // Listeners
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
        recuperaPartecipazioni()
        binding.partecipazioniRecyclerView.adapter = adapterPartecipazioni
    }

    private fun recuperaPartecipazioni() {
        lifecycleScope.launch {
            try {
                if (CurrentUser.tipoAccount === TipoAccount.COMPRATORE) {
                    viewModel.getCompratoreFlows().collectLatest { pagingData ->
                        adapterPartecipazioni.submitData(pagingData)
                    }
                } else {
                    viewModel.getVenditoreFlows().collectLatest { pagingData ->
                        adapterPartecipazioni.submitData(pagingData)
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

    @UIBuilder
    override fun impostaEventiClick() {
        binding.partecipazioniPulsanteIndietro.setOnClickListener { clickIndietro() }
    }

    @EventHandler
    private fun clickIndietro() {
        listenerBackButton?.onBackButton()
    }
}
package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.iasdietideals24.dietideals24.databinding.PartecipazioniBinding
import com.iasdietideals24.dietideals24.model.ModelPartecipazioni
import com.iasdietideals24.dietideals24.utilities.adapters.AdapterPartecipazioni
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.kscripts.OnBackButton
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.merge
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

    private var jobRecupero: Job? = null

    override fun onPause() {
        super.onPause()

        jobRecupero?.cancel()
    }

    override fun onResume() {
        super.onResume()

        jobRecupero = lifecycleScope.launch {
            recuperaPartecipazioni()
        }
    }

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
        binding.partecipazioniRecyclerView.adapter = adapterPartecipazioni

        jobRecupero = lifecycleScope.launch {
            recuperaPartecipazioni()
        }
    }

    private suspend fun recuperaPartecipazioni() {
        when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> {
                merge(
                    viewModel.getAsteTempoFissoFlows(),
                    viewModel.getAsteSilenzioseFlows()
                ).collectLatest { pagingData ->
                    adapterPartecipazioni.submitData(pagingData)
                }
            }

            TipoAccount.VENDITORE -> {
                viewModel.getAsteInverseFlows().collectLatest { pagingData ->
                    adapterPartecipazioni.submitData(pagingData)
                }
            }

            else -> {
                // Non fare niente
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
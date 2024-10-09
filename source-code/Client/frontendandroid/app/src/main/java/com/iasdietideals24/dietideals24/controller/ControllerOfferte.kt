package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.OfferteBinding
import com.iasdietideals24.dietideals24.model.ModelAsta
import com.iasdietideals24.dietideals24.utilities.adapters.AdapterOfferte
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToDetails
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.parameter.parametersOf

class ControllerOfferte : Controller<OfferteBinding>() {

    // Navigation
    private val args: ControllerOfferteArgs by navArgs()

    // ViewModel
    private val viewModel: ModelAsta by activityViewModel()

    // PagingAdapters
    private val adapterOfferte: AdapterOfferte by inject { parametersOf(resources) }

    private var jobOfferte: Job? = null

    // Listeners
    private var listenerDetails: OnGoToDetails? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnGoToDetails) {
            listenerDetails = requireContext() as OnGoToDetails
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerDetails = null
    }

    override fun onPause() {
        super.onPause()

        jobOfferte?.cancel()
    }

    override fun onResume() {
        super.onResume()

        jobOfferte = lifecycleScope.launch {
            while (isActive) {
                aggiornaOfferte()

                delay(15000)
            }
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.offertePulsanteIndietro.setOnClickListener {
            clickIndietro()
        }
    }

    @EventHandler
    private fun clickIndietro() {
        listenerDetails?.onGoToDetails(args.id, viewModel.tipo.value!!, this::class)
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        binding.offerteRecyclerView.layoutManager = LinearLayoutManager(fragmentContext)
    }

    private suspend fun aggiornaOfferte() {
        when (viewModel.tipo.value!!) {
            TipoAsta.TEMPO_FISSO -> {
                viewModel.getTempoFissoFlows().collectLatest { pagingData ->
                    adapterOfferte.submitData(pagingData)
                }
            }

            TipoAsta.SILENZIOSA -> {
                viewModel.getSilenzioseFlows().collectLatest { pagingData ->
                    adapterOfferte.submitData(pagingData)
                }
            }

            TipoAsta.INVERSA -> {
                viewModel.getInverseFlows().collectLatest { pagingData ->
                    adapterOfferte.submitData(pagingData)
                }
            }
        }

        if (viewModel.getFlows().count() != 0)
            binding.offerteRecyclerView.adapter = adapterOfferte
        else
            Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.blu, null))
                .setTextColor(resources.getColor(R.color.grigio, null))
                .show()
    }
}
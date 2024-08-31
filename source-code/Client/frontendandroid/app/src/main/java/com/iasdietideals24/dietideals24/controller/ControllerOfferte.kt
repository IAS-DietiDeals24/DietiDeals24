package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.OfferteBinding
import com.iasdietideals24.dietideals24.model.ModelAsta
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.adapters.AdapterOfferte
import com.iasdietideals24.dietideals24.utilities.classes.data.OffertaRicevuta
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ControllerOfferte : Controller<OfferteBinding>() {

    private val args: ControllerOfferteArgs by navArgs()
    private lateinit var viewModel: ModelAsta

    private var mainDispatcher = Dispatchers.Main
    private var ioDispatcher = Dispatchers.IO
    private var jobOfferte: Job? = null

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
        listenerDetails?.onGoToDetails(args.id, this::class)
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity)[ModelAsta::class]

        binding.offerteRecyclerView.layoutManager = LinearLayoutManager(fragmentContext)
    }

    private suspend fun aggiornaOfferte() {
        val result: Array<OffertaRicevuta>? = withContext(ioDispatcher) {
            eseguiChiamataREST(
                "recuperaOfferte",
                if (args.id == 0L) viewModel.idAsta.value!! else args.id
            )
        }

        withContext(mainDispatcher) {
            if (result != null)
                binding.offerteRecyclerView.adapter = AdapterOfferte(result, resources)
            else
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
        }
    }
}
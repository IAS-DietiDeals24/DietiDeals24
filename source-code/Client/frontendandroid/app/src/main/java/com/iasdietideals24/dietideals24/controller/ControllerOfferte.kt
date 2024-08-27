package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.OfferteBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.adapters.AdapterNotifiche
import com.iasdietideals24.dietideals24.utilities.classes.data.Notifica
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentBackButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ControllerOfferte : Controller<OfferteBinding>() {

    private var mainDispatcher = Dispatchers.Main
    private var ioDispatcher = Dispatchers.IO
    private var jobOfferte: Job? = null

    private var listenerBackButton: OnFragmentBackButton? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnFragmentBackButton) {
            listenerBackButton = requireContext() as OnFragmentBackButton
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerBackButton = null
    }

    override fun onPause() {
        super.onPause()

        jobOfferte?.cancel()
    }

    override fun onResume() {
        super.onResume()

        jobOfferte = lifecycleScope.launch {
            while (isActive) {
                aggiornaNotifiche()

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

    private fun clickIndietro() {
        listenerBackButton?.onFragmentBackButton()
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        binding.offerteRecyclerView.layoutManager = LinearLayoutManager(fragmentContext)
    }

    private suspend fun aggiornaNotifiche() {
        val result: Array<Notifica>? = withContext(ioDispatcher) {
            eseguiChiamataREST(
                "recuperaNotifiche",
                CurrentUser.id,
            )
        }

        withContext(mainDispatcher) {
            if (result != null)
                binding.offerteRecyclerView.adapter = AdapterNotifiche(result, resources)
            else
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
        }
    }
}
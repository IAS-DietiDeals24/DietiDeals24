package com.iasdietideals24.dietideals24.controller

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.iasdietideals24.dietideals24.databinding.NotificheBinding
import com.iasdietideals24.dietideals24.model.ModelNotifiche
import com.iasdietideals24.dietideals24.utilities.adapters.AdapterNotifiche
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.parameter.parametersOf

class ControllerNotifiche : Controller<NotificheBinding>() {

    // ViewModel
    private val viewModel: ModelNotifiche by activityViewModel()

    // Adapter
    private val adapterNotifiche: AdapterNotifiche by inject { parametersOf(resources) }

    private var jobNotifiche: Job? = null

    override fun onPause() {
        super.onPause()

        jobNotifiche?.cancel()
    }

    override fun onResume() {
        super.onResume()

        jobNotifiche = lifecycleScope.launch {
            while (isActive) {
                viewModel.invalidate()

                aggiornaNotifiche()

                delay(15000)
            }
        }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        binding.notificheRecyclerView.layoutManager = LinearLayoutManager(fragmentContext)
        binding.notificheRecyclerView.adapter = adapterNotifiche
    }

    private suspend fun aggiornaNotifiche() {
        viewModel.getNotificheFlows().collect { pagingData ->
            adapterNotifiche.submitData(pagingData)
        }
    }
}
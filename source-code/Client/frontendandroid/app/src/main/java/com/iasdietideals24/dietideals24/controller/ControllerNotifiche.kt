package com.iasdietideals24.dietideals24.controller

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.NotificheBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.APIController
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.adapters.AdapterNotifiche
import com.iasdietideals24.dietideals24.utilities.classes.data.Notifica
import com.iasdietideals24.dietideals24.utilities.classes.toArrayOfNotifica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ControllerNotifiche : Controller<NotificheBinding>() {

    private var mainDispatcher = Dispatchers.Main
    private var ioDispatcher = Dispatchers.IO
    private var jobNotifiche: Job? = null

    override fun onPause() {
        super.onPause()

        jobNotifiche?.cancel()
    }

    override fun onResume() {
        super.onResume()

        jobNotifiche = lifecycleScope.launch {
            while (isActive) {
                aggiornaNotifiche()

                delay(15000)
            }
        }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        binding.notificheRecyclerView.layoutManager = LinearLayoutManager(fragmentContext)
    }

    private suspend fun aggiornaNotifiche() {
        val result: Array<Notifica> = withContext(ioDispatcher) {
            val call = APIController.instance.recuperaNotifiche(CurrentUser.id)

            chiamaAPI(call).toArrayOfNotifica()
        }

        withContext(mainDispatcher) {
            if (result.isNotEmpty())
                binding.notificheRecyclerView.adapter = AdapterNotifiche(result, resources)
            else
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
        }
    }
}
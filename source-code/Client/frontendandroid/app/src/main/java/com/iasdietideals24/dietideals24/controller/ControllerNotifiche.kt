package com.iasdietideals24.dietideals24.controller

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.adapters.AdapterNotifiche
import com.iasdietideals24.dietideals24.utilities.classes.data.DatiNotifica
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ControllerNotifiche : Controller(R.layout.notifiche) {

    private lateinit var recyclerView: RecyclerView

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
    override fun trovaElementiInterfaccia() {
        recyclerView = fragmentView.findViewById(R.id.notifiche_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(fragmentContext)
    }

    private suspend fun aggiornaNotifiche() {
        val result: Array<DatiNotifica>? = withContext(Dispatchers.IO) {
            eseguiChiamataREST(
                "recuperaNotifiche",
                CurrentUser.id,
            )
        }

        withContext(Dispatchers.Main) {
            if (result != null)
                recyclerView.adapter = AdapterNotifiche(result, resources)
            else
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
        }
    }
}
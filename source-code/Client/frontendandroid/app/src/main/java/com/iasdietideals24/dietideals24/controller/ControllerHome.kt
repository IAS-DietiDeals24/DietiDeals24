package com.iasdietideals24.dietideals24.controller

import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.HomeBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.adapters.AdapterHome
import com.iasdietideals24.dietideals24.utilities.classes.data.DatiAnteprimaAsta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ControllerHome : Controller<HomeBinding>() {

    private var jobRecupero: Job? = null

    override fun onPause() {
        super.onPause()

        jobRecupero?.cancel()
    }

    override fun onResume() {
        super.onResume()

        if (binding.homeRicerca.text.isNullOrEmpty()) {
            jobRecupero = lifecycleScope.launch {
                while (isActive) {
                    recuperaAste()

                    delay(10000)
                }
            }
        }
    }

    @UIBuilder
    override fun impostaEventiDiCambiamentoCampi() {
        binding.homeRicerca.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Non fare niente
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Non fare niente
            }

            override fun afterTextChanged(s: Editable?) {
                jobRecupero?.cancel()

                if (!s.isNullOrEmpty()) {
                    jobRecupero = lifecycleScope.launch {
                        delay(1000)

                        while (isActive) {
                            recuperaAste()

                            delay(10000)
                        }
                    }
                } else if (s.isNullOrEmpty() && lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    jobRecupero = lifecycleScope.launch {
                        while (isActive) {
                            recuperaAste()

                            delay(10000)
                        }
                    }
                }
            }
        })
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(fragmentContext)
        binding.homeFiltro.setText(resources.getString(R.string.category_no_category))
        binding.homeFiltro.setSimpleItems(resources.getStringArray(R.array.home_categorieAsta))
        binding.homeFiltro.setOnItemClickListener { _, _, _, _ ->
            jobRecupero?.cancel()

            jobRecupero = lifecycleScope.launch {
                while (isActive) {
                    recuperaAste()

                    delay(10000)
                }
            }
        }
    }

    private suspend fun recuperaAste() {
        val result: Array<DatiAnteprimaAsta>? = withContext(Dispatchers.IO) {
            if (binding.homeRicerca.text.isNullOrEmpty() && binding.homeFiltro.text.toString() == getString(
                    R.string.category_no_category
                )
            )
                eseguiChiamataREST(
                    "recuperaAste",
                    CurrentUser.id
                )
            else
                eseguiChiamataREST(
                    "ricercaAste",
                    CurrentUser.id,
                    binding.homeRicerca.text.toString(),
                    binding.homeFiltro.text.toString()
                )
        }

        withContext(Dispatchers.Main) {
            if (result != null)
                binding.homeRecyclerView.adapter = AdapterHome(result, resources)
            else
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
        }
    }
}
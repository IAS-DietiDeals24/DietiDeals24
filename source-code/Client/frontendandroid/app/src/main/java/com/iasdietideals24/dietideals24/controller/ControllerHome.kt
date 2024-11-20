package com.iasdietideals24.dietideals24.controller

import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.HomeBinding
import com.iasdietideals24.dietideals24.model.ModelHome
import com.iasdietideals24.dietideals24.utilities.adapters.AdapterHome
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.repositories.CategoriaAstaRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import com.iasdietideals24.dietideals24.utilities.tools.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.parameter.parametersOf

class ControllerHome : Controller<HomeBinding>() {

    // ViewModel
    private val viewModel: ModelHome by activityViewModel()

    // Repositories
    private val categoriaAstaRepository: CategoriaAstaRepository by inject()

    // PagingAdapter
    private val adapterHome: AdapterHome by inject { parametersOf(resources) }

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
    override fun impostaEventiClick() {
        binding.homeFiltro.setOnItemClickListener { _, _, _, _ ->
            viewModel.filter.value = binding.homeFiltro.text.toString()
        }
    }

    @UIBuilder
    override fun impostaEventiDiCambiamentoCampi() {
        binding.homeRicerca.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.searchText.value = s.toString()

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

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    @UIBuilder
    override fun impostaOsservatori() {
        lifecycleScope.launch {
            viewModel.searchText.collect {
                viewModel.searchText.value = it
            }
        }

        lifecycleScope.launch {
            viewModel.filter.collect {
                viewModel.filter.value = it
            }
        }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(fragmentContext)

        lifecycleScope.launch {
            val categorieAsta: MutableList<String> = mutableListOf()

            withContext(Dispatchers.IO) {
                categoriaAstaRepository.recuperaCategorieAsta().forEach {
                    categorieAsta.add(it.nome)
                }
            }

            val adapter: ArrayAdapter<String> = ArrayAdapter(
                fragmentContext,
                android.R.layout.simple_dropdown_item_1line,
                categorieAsta
            )

            binding.homeFiltro.setAdapter(adapter)
        }
    }

    private suspend fun recuperaAste() {
        when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> {
                viewModel.getCompratoreFlows().collectLatest { pagingData ->
                    adapterHome.submitData(pagingData)
                }
            }

            TipoAccount.VENDITORE -> {
                viewModel.getVenditoreFlows().collectLatest { pagingData ->
                    adapterHome.submitData(pagingData)
                }
            }

            else -> {
                viewModel.getFlows().collectLatest { pagingData ->
                    adapterHome.submitData(pagingData)
                }
            }
        }

        Logger.log("Performing auction research")

        if (viewModel.getFlows().count() != 0)
            binding.homeRecyclerView.adapter = adapterHome
        else
            Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.blu, null))
                .setTextColor(resources.getColor(R.color.grigio, null))
                .show()
    }
}
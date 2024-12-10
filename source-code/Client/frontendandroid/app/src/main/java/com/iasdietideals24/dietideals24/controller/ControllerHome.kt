package com.iasdietideals24.dietideals24.controller

import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.HomeBinding
import com.iasdietideals24.dietideals24.model.ModelHome
import com.iasdietideals24.dietideals24.utilities.adapters.AdapterHome
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.enumerations.CategoriaAsta
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.repositories.CategoriaAstaRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
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

        jobRecupero = lifecycleScope.launch {
            while (isActive) {
                viewModel.invalidate()

                recuperaAste()

                delay(10000)
            }
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.homeFiltro.setOnItemClickListener { _, _, _, _ ->
            viewModel.filter.value =
                if (binding.homeFiltro.text.toString() != getString(R.string.category_all))
                    binding.homeFiltro.text.toString() else ""

            jobRecupero = lifecycleScope.launch {
                jobRecupero?.cancel()

                viewModel.invalidate()

                recuperaAste()

                delay(10000)
            }
        }

        binding.homeCampoRicerca.setEndIconOnClickListener {
            viewModel.searchText.value = binding.homeRicerca.text.toString()

            jobRecupero = lifecycleScope.launch {
                jobRecupero?.cancel()

                viewModel.invalidate()

                recuperaAste()

                delay(10000)
            }
        }

        binding.homeTipo.setOnItemClickListener { _, _, position, _ ->
            viewModel.tipo = position

            jobRecupero = lifecycleScope.launch {
                jobRecupero?.cancel()

                viewModel.invalidate()

                recuperaAste()

                delay(10000)
            }
        }
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
        binding.homeRecyclerView.adapter = adapterHome

        when (CurrentUser.tipoAccount) {
            TipoAccount.VENDITORE -> {
                binding.homeCampoTipo.visibility = View.GONE
            }

            TipoAccount.COMPRATORE -> {
                val tipoAsta: MutableList<String> = mutableListOf(
                    getString(R.string.tipoAsta_astaSilenziosa),
                    getString(R.string.tipoAsta_astaTempoFisso)
                )

                val adapter: ArrayAdapter<String> = ArrayAdapter(
                    fragmentContext,
                    android.R.layout.simple_dropdown_item_1line,
                    tipoAsta
                )

                binding.homeTipo.setAdapter(adapter)
                binding.homeTipo.setText(tipoAsta[viewModel.tipo], false)

                binding.homeTipo.setOnItemClickListener { _, _, position, _ ->
                    viewModel.tipo = position

                    jobRecupero = lifecycleScope.launch {
                        jobRecupero?.cancel()

                        viewModel.invalidate()

                        recuperaAste()
                    }
                }
            }

            else -> {
                val tipoAsta: MutableList<String> = mutableListOf(
                    getString(R.string.tipoAsta_astaSilenziosa),
                    getString(R.string.tipoAsta_astaTempoFisso),
                    getString(R.string.tipoAsta_astaInversa)
                )

                val adapter: ArrayAdapter<String> = ArrayAdapter(
                    fragmentContext,
                    android.R.layout.simple_dropdown_item_1line,
                    tipoAsta
                )

                binding.homeTipo.setAdapter(adapter)
                binding.homeTipo.setText(tipoAsta[viewModel.tipo], false)

                binding.homeTipo.setOnItemClickListener { _, _, position, _ ->
                    viewModel.tipo = position

                    jobRecupero = lifecycleScope.launch {
                        jobRecupero?.cancel()

                        viewModel.invalidate()

                        recuperaAste()
                    }
                }
            }
        }

        lifecycleScope.launch {
            val categorieAsta: MutableList<String> = mutableListOf(getString(R.string.category_all))

            withContext(Dispatchers.IO) {
                categoriaAstaRepository.recuperaCategorieAsta().forEach {
                    categorieAsta.add(CategoriaAsta.fromEnumToString(CategoriaAsta.valueOf(it.nome)))
                }
            }

            val adapter: ArrayAdapter<String> = ArrayAdapter(
                fragmentContext,
                android.R.layout.simple_dropdown_item_1line,
                categorieAsta
            )

            binding.homeFiltro.setAdapter(adapter)

            if (viewModel.filter.value == "")
                binding.homeFiltro.setText(getString(R.string.category_all), false)
            else
                binding.homeFiltro.setText(viewModel.filter.value, false)
        }
    }

    private suspend fun recuperaAste() {
        when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> {
                if (viewModel.tipo == 0) {
                    viewModel.getAsteSilenzioseFlows().collectLatest { pagingData ->
                        adapterHome.submitData(pagingData)
                    }
                } else {
                    viewModel.getAsteTempoFissoFlows().collectLatest { pagingData ->
                        adapterHome.submitData(pagingData)
                    }
                }
            }

            TipoAccount.VENDITORE -> {
                viewModel.getAsteInverseFlows().collectLatest { pagingData ->
                    adapterHome.submitData(pagingData)
                }
            }

            else -> {
                when (viewModel.tipo) {
                    0 -> {
                        viewModel.getAsteSilenzioseFlows().collectLatest { pagingData ->
                            adapterHome.submitData(pagingData)
                        }
                    }

                    1 -> {
                        viewModel.getAsteTempoFissoFlows().collectLatest { pagingData ->
                            adapterHome.submitData(pagingData)
                        }
                    }

                    else -> {
                        viewModel.getAsteInverseFlows().collectLatest { pagingData ->
                            adapterHome.submitData(pagingData)
                        }
                    }
                }
            }
        }

        withContext(Dispatchers.IO) {
            logger.scriviLog("Performing auction research")
        }
    }
}
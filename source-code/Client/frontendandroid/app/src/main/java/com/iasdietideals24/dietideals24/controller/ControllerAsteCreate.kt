package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.AstecreateBinding
import com.iasdietideals24.dietideals24.model.ModelAsteCreate
import com.iasdietideals24.dietideals24.utilities.adapters.AdapterAsteCreate
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.kscripts.OnBackButton
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.parameter.parametersOf

class ControllerAsteCreate : Controller<AstecreateBinding>() {

    // ViewModel
    private val viewModel: ModelAsteCreate by activityViewModel()

    // PagingAdapter
    private val adapterAsteCreate: AdapterAsteCreate by inject { parametersOf(resources) }

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
            viewModel.invalidate()

            recuperaAsteCreate()
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
        binding.astecreateRecyclerView.layoutManager = LinearLayoutManager(fragmentContext)
        binding.astecreateRecyclerView.adapter = adapterAsteCreate

        if (CurrentUser.tipoAccount == TipoAccount.COMPRATORE) {
            binding.astecreateCampoFiltro.visibility = View.GONE
        } else {
            val categorieAsta: MutableList<String> = mutableListOf(
                getString(R.string.tipoAsta_astaSilenziosa),
                getString(R.string.tipoAsta_astaTempoFisso)
            )

            val adapter: ArrayAdapter<String> = ArrayAdapter(
                fragmentContext,
                android.R.layout.simple_dropdown_item_1line,
                categorieAsta
            )

            binding.astecreateFiltro.setAdapter(adapter)

            binding.astecreateFiltro.setOnItemClickListener { _, _, position, _ ->
                viewModel.filtro = position

                jobRecupero = lifecycleScope.launch {
                    jobRecupero?.cancel()

                    viewModel.invalidate()

                    recuperaAsteCreate()
                }
            }
        }

        jobRecupero = lifecycleScope.launch {
            recuperaAsteCreate()
        }
    }

    private suspend fun recuperaAsteCreate() {
        when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> {
                viewModel.getAsteInverseFlows().collectLatest { pagingData ->
                    adapterAsteCreate.submitData(pagingData)
                }
            }

            TipoAccount.VENDITORE -> {
                if (viewModel.filtro == 0) {
                    viewModel.getAsteSilenzioseFlows().collectLatest { pagingData ->
                        adapterAsteCreate.submitData(pagingData)
                    }
                } else {
                    viewModel.getAsteTempoFissoFlows().collectLatest { pagingData ->
                        adapterAsteCreate.submitData(pagingData)
                    }
                }
            }

            else -> {
                // Non fare niente
            }
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.astecreatePulsanteIndietro.setOnClickListener { clickIndietro() }
    }

    @EventHandler
    private fun clickIndietro() {
        listenerBackButton?.onBackButton()
    }
}
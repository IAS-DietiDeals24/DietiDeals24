package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.AstecreateBinding
import com.iasdietideals24.dietideals24.model.ModelAsteCreate
import com.iasdietideals24.dietideals24.utilities.adapters.AdapterAsteCreate
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.kscripts.OnBackButton
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
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
        recuperaAsteCreate()
        binding.astecreateRecyclerView.adapter = adapterAsteCreate
    }

    private fun recuperaAsteCreate() {
        lifecycleScope.launch {
            try {
                when (CurrentUser.tipoAccount) {
                    TipoAccount.COMPRATORE -> {
                        viewModel.getCompratoreFlows().collectLatest { pagingData ->
                            adapterAsteCreate.submitData(pagingData)
                        }
                    }

                    TipoAccount.VENDITORE -> {
                        viewModel.getVenditoreFlows().collectLatest { pagingData ->
                            adapterAsteCreate.submitData(pagingData)
                        }
                    }

                    else -> {
                        //Non fare nulla
                    }
                }
            } catch (_: Exception) {
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
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
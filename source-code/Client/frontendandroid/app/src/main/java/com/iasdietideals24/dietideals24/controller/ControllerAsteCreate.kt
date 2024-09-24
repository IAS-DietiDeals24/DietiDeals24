package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.iasdietideals24.dietideals24.databinding.AstecreateBinding
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.APIController
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.TipoAccount
import com.iasdietideals24.dietideals24.utilities.classes.adapters.AdapterAsteCreate
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.toArrayOfAnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.interfaces.OnBackButton

class ControllerAsteCreate : Controller<AstecreateBinding>() {

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
        val result: Array<AnteprimaAsta> = recuperaAsteCreate()

        if (result.isNotEmpty())
            binding.astecreateRecyclerView.adapter =
                AdapterAsteCreate(result, resources)
    }

    private fun recuperaAsteCreate(): Array<AnteprimaAsta> {
        return if (CurrentUser.tipoAccount == TipoAccount.COMPRATORE) {
            val call = APIController.instance.recuperaAsteCreateInverse(CurrentUser.id)
            chiamaAPI(call).toArrayOfAnteprimaAsta()
        } else {
            val call1 = APIController.instance.recuperaAsteCreateSilenziose(CurrentUser.id)
            val call2 = APIController.instance.recuperaAsteCreateTempoFisso(CurrentUser.id)
            chiamaAPI(call1).toArrayOfAnteprimaAsta()
                .plus(chiamaAPI(call2).toArrayOfAnteprimaAsta())
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
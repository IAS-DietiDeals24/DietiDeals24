package com.iasdietideals24.dietideals24.controller

import androidx.lifecycle.ViewModelProvider
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.model.ModelAsta
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder

class ControllerCreaAsta : Controller(R.layout.creaasta) {

    private lateinit var viewModel: ModelAsta

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity).get(ModelAsta::class)
    }
}

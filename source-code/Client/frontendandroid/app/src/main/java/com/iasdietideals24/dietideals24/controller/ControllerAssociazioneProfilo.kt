package com.iasdietideals24.dietideals24.controller

import com.google.android.material.button.MaterialButton
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder

class ControllerAssociazioneProfilo : Controller(R.layout.associaprofilo) {
    private lateinit var pulsanteFine: MaterialButton

    @UIBuilder
    override fun trovaElementiInterfaccia() {
        pulsanteFine = findViewById(R.id.associaProfilo_pulsanteFine)
    }

    @UIBuilder
    override fun impostaEventiClick() {
        pulsanteFine.setOnClickListener { clickFine() }
    }

    @EventHandler
    private fun clickFine() {
        TODO("Vai alla home")
    }
}
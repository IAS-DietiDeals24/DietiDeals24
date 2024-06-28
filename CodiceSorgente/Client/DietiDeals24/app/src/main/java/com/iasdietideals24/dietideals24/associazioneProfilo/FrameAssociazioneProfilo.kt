package com.iasdietideals24.dietideals24.associazioneProfilo

import com.google.android.material.button.MaterialButton
import com.iasdietideals24.dietideals24.R

class FrameAssociazioneProfilo(private val controller: ControllerAssociazioneProfilo) {
    private lateinit var pulsanteFine: MaterialButton
    init {
        controller.setContentView(R.layout.associaprofilo)

        trovaElementiInterfaccia()

        impostaEventiClick()
    }

    private fun trovaElementiInterfaccia() {
        pulsanteFine = controller.findViewById(R.id.associaProfilo_pulsanteFine)
    }

    private fun impostaEventiClick() {
        pulsanteFine.setOnClickListener { clickFine() }
    }

    private fun clickFine() {
        controller.apriHome()
    }
}
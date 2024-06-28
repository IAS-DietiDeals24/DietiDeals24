package com.iasdietideals24.dietideals24.associazioneProfilo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ControllerAssociazioneProfilo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mostraAssociaProfilo()
    }

    private fun mostraAssociaProfilo() {
        FrameAssociazioneProfilo(this)
    }

    fun apriHome() {
        TODO()
    }
}
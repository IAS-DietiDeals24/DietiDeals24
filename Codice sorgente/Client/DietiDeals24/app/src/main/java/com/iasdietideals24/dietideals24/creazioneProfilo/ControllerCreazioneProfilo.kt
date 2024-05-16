package com.iasdietideals24.dietideals24.creazioneProfilo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ControllerCreazioneProfilo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mostraCreazioneProfiloFase1()
    }

    private fun mostraCreazioneProfiloFase1() {
        FrameCreazioneProfiloFase1(this)
    }
}
package com.iasdietideals24.dietideals24.controller

import android.widget.ImageButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.model.ModelControllerCreazioneProfilo
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder

class ControllerCreazioneProfiloFase3 : Controller(R.layout.creazioneprofilofase3) {
    private var viewModel: ModelControllerCreazioneProfilo? = null

    private lateinit var pulsanteIndietro: ImageButton
    private lateinit var linkPersonale: TextInputEditText
    private lateinit var linkInstagram: TextInputEditText
    private lateinit var linkFacebook: TextInputEditText
    private lateinit var linkGitHub: TextInputEditText
    private lateinit var linkX: TextInputEditText
    private lateinit var pulsanteFine: MaterialButton

    @UIBuilder
    override fun trovaElementiInterfaccia() {
        pulsanteIndietro = findViewById(R.id.creazioneProfiloFase3_pulsanteIndietro)
        linkPersonale = findViewById(R.id.creazioneProfiloFase3_linkPersonale)
        linkInstagram = findViewById(R.id.creazioneProfiloFase3_linkInstagram)
        linkFacebook = findViewById(R.id.creazioneProfiloFase3_linkFacebook)
        linkGitHub = findViewById(R.id.creazioneProfiloFase3_linkGitHub)
        linkX = findViewById(R.id.creazioneProfiloFase3_linkX)
        pulsanteFine = findViewById(R.id.creazioneProfiloFase3_pulsanteFine)
    }

    @UIBuilder
    override fun impostaEventiClick() {
        pulsanteIndietro.setOnClickListener { clickIndietro() }
        pulsanteFine.setOnClickListener { clickFine() }
    }

    @EventHandler
    private fun clickIndietro() {
        cambiaAttivita(ControllerCreazioneProfiloFase2::class.java, viewModel!!)
    }

    @EventHandler
    private fun clickFine() {
        TODO("Registra e vai alla home")
    }
}
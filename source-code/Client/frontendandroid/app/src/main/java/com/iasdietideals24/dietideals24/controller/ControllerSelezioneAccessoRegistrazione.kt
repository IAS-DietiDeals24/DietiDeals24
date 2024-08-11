package com.iasdietideals24.dietideals24.controller

import android.widget.ImageButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder

class ControllerSelezioneAccessoRegistrazione : Controller(R.layout.selezioneaccessoregistrazione) {
    private lateinit var pulsanteAccedi: MaterialButton
    private lateinit var pulsanteRegistrati: MaterialButton
    private lateinit var pulsanteIndietro: ImageButton
    private lateinit var saluto: MaterialTextView


    @UIBuilder
    override fun trovaElementiInterfaccia() {
        pulsanteAccedi = findViewById(R.id.selezioneAccessoRegistrazione_pulsanteAccedi)
        pulsanteRegistrati = findViewById(R.id.selezioneAccessoRegistrazione_pulsanteRegistrati)
        pulsanteIndietro = findViewById(R.id.selezioneAccessoRegistrazione_pulsanteIndietro)
        saluto = findViewById(R.id.selezioneAccessoRegistrazione_saluto)
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        when (caricaPreferenzaStringa("tipoAccount")) {
            "compratore" -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_compratore)
                saluto.text = getString(
                    R.string.selezioneAccessoRegistrazione_saluto,
                    stringaTipoAccount
                )
            }

            "venditore" -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_venditore)
                saluto.text = getString(
                    R.string.selezioneAccessoRegistrazione_saluto,
                    stringaTipoAccount
                )
            }
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        pulsanteAccedi.setOnClickListener {
            clickAccedi()
        }

        pulsanteRegistrati.setOnClickListener {
            clickRegistrati()
        }

        pulsanteIndietro.setOnClickListener {
            clickIndietro()
        }
    }


    @EventHandler
    private fun clickAccedi() {
        cambiaAttivita(ControllerAccesso::class.java)
    }

    @EventHandler
    private fun clickRegistrati() {
        cambiaAttivita(ControllerRegistrazione::class.java)
    }

    @EventHandler
    private fun clickIndietro() {
        cambiaAttivita(ControllerSelezioneTipoAccount::class.java)
    }
}
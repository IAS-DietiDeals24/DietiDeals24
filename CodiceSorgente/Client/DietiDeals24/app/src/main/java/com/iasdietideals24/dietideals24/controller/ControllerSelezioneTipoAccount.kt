package com.iasdietideals24.dietideals24.controller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.utilities.EventHandler
import com.iasdietideals24.dietideals24.utilities.UIBuilder


class ControllerSelezioneTipoAccount : AppCompatActivity() {

    private lateinit var pulsanteCompratore: MaterialButton
    private lateinit var pulsanteVenditore: MaterialButton
    private lateinit var pulsanteOspite: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.selezionetipoaccount)

        trovaElementiInterfaccia()

        impostaEventiClick()
    }


    @UIBuilder
    private fun trovaElementiInterfaccia() {
        pulsanteCompratore = findViewById(R.id.selezioneTipoAccount_pulsanteCompratore)
        pulsanteVenditore = findViewById(R.id.selezioneTipoAccount_pulsanteVenditore)
        pulsanteOspite = findViewById(R.id.selezioneTipoAccount_pulsanteOspite)
    }

    @UIBuilder
    private fun impostaEventiClick() {
        pulsanteCompratore.setOnClickListener {
            clickCompratore()
        }

        pulsanteVenditore.setOnClickListener {
            clickVenditore()
        }

        pulsanteOspite.setOnClickListener {
            clickOspite()
        }
    }


    @EventHandler
    private fun clickCompratore() {
        val nuovaAttivita = Intent(this, ControllerSelezioneAccessoRegistrazione::class.java)
        nuovaAttivita.putExtra("tipoAccount", "compratore")
        startActivity(nuovaAttivita)
    }

    @EventHandler
    private fun clickVenditore() {
        val nuovaAttivita = Intent(this, ControllerSelezioneAccessoRegistrazione::class.java)
        nuovaAttivita.putExtra("tipoAccount", "venditore")
        startActivity(nuovaAttivita)
    }

    @EventHandler
    private fun clickOspite() {
        TODO()
    }
}
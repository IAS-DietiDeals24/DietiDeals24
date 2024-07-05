package com.iasdietideals24.dietideals24.controller

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.utilities.EventHandler
import com.iasdietideals24.dietideals24.utilities.UIBuilder

class ControllerSelezioneAccessoRegistrazione : AppCompatActivity() {

    private lateinit var pulsanteAccedi: MaterialButton
    private lateinit var pulsanteRegistrati: MaterialButton
    private lateinit var pulsanteIndietro: ImageButton
    private lateinit var saluto: MaterialTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.selezioneaccessoregistrazione)

        trovaElementiInterfaccia()

        impostaMessaggioCorpo()

        impostaEventiClick()
    }


    @UIBuilder
    private fun trovaElementiInterfaccia() {
        pulsanteAccedi = findViewById(R.id.selezioneAccessoRegistrazione_pulsanteAccedi)
        pulsanteRegistrati = findViewById(R.id.selezioneAccessoRegistrazione_pulsanteRegistrati)
        pulsanteIndietro = findViewById(R.id.selezioneAccessoRegistrazione_pulsanteIndietro)
        saluto = findViewById(R.id.selezioneAccessoRegistrazione_saluto)
    }

    @UIBuilder
    private fun impostaMessaggioCorpo() {
        when (intent.extras?.getString("tipoAccount")) {
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
    private fun impostaEventiClick() {
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
        val nuovaAttivita = Intent(this, ControllerAccesso::class.java)
        nuovaAttivita.putExtra("tipoAccount", intent.extras?.getString("tipoAccount"))
        startActivity(nuovaAttivita)
    }

    @EventHandler
    private fun clickRegistrati() {
        val nuovaAttivita = Intent(this, ControllerRegistrazione::class.java)
        nuovaAttivita.putExtra("tipoAccount", intent.extras?.getString("tipoAccount"))
        startActivity(nuovaAttivita)
    }

    @EventHandler
    private fun clickIndietro() {
        val nuovaAttivita = Intent(this, ControllerSelezioneTipoAccount::class.java)
        startActivity(nuovaAttivita)
    }
}
package com.iasdietideals24.dietideals24.controller

import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.LoginStatusCallback
import com.facebook.login.LoginManager.Companion.getInstance
import com.google.android.material.button.MaterialButton
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.model.ModelControllerAccesso
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder

class ControllerSelezioneTipoAccount : Controller(R.layout.selezionetipoaccount) {
    private var viewModel: ModelControllerAccesso = ModelControllerAccesso()

    private lateinit var pulsanteCompratore: MaterialButton
    private lateinit var pulsanteVenditore: MaterialButton
    private lateinit var pulsanteOspite: MaterialButton

    override fun elaborazioneAggiuntiva() {
        // Controlla l'accesso automatico con Facebook
        getInstance()
            .retrieveLoginStatus(
                this,
                object : LoginStatusCallback {
                    override fun onCompleted(accessToken: AccessToken) {
                        // Non fare nulla
                    }

                    override fun onFailure() {
                        // Non fare nulla
                    }

                    override fun onError(exception: Exception) {
                        Toast.makeText(
                            applicationContext,
                            "Errore durante l'accesso con Facebook",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }


    @UIBuilder
    override fun trovaElementiInterfaccia() {
        pulsanteCompratore = findViewById(R.id.selezioneTipoAccount_pulsanteCompratore)
        pulsanteVenditore = findViewById(R.id.selezioneTipoAccount_pulsanteVenditore)
        pulsanteOspite = findViewById(R.id.selezioneTipoAccount_pulsanteOspite)
    }

    @UIBuilder
    override fun impostaEventiClick() {
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
        cambiaAttivita(
            ControllerSelezioneAccessoRegistrazione::class.java,
            Pair("tipoAccount", "compratore")
        )
    }

    @EventHandler
    private fun clickVenditore() {
        cambiaAttivita(
            ControllerSelezioneAccessoRegistrazione::class.java,
            Pair("tipoAccount", "venditore")
        )
    }

    @EventHandler
    private fun clickOspite() {
        TODO("Vai alla home")
    }
}
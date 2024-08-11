package com.iasdietideals24.dietideals24.controller

import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.model.ModelControllerAccesso
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneAPI
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneAccountNonEsistente

class ControllerAccesso : Controller(R.layout.accesso) {
    private var viewModel: ModelControllerAccesso = ModelControllerAccesso()

    private var callbackManager = create()

    private lateinit var tipoAccount: MaterialTextView
    private lateinit var campoEmail: TextInputLayout
    private lateinit var email: TextInputEditText
    private lateinit var campoPassword: TextInputLayout
    private lateinit var password: TextInputEditText
    private lateinit var pulsanteIndietro: ImageButton
    private lateinit var pulsanteAccedi: MaterialButton
    private lateinit var pulsanteFacebook: LoginButton
    private lateinit var layout: LinearLayout


    @UIBuilder
    override fun trovaElementiInterfaccia() {
        tipoAccount = findViewById(R.id.accesso_tipoAccount)
        campoEmail = findViewById(R.id.accesso_campoEmail)
        email = findViewById(R.id.accesso_email)
        campoPassword = findViewById(R.id.accesso_campoPassword)
        password = findViewById(R.id.accesso_password)
        pulsanteIndietro = findViewById(R.id.accesso_pulsanteIndietro)
        pulsanteAccedi = findViewById(R.id.accesso_pulsanteAccedi)
        pulsanteFacebook = findViewById(R.id.accesso_pulsanteFacebook)
        layout = findViewById(R.id.accesso_linearLayout)
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        when (caricaPreferenzaStringa("tipoAccount")) {
            "compratore" -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_compratore)
                tipoAccount.text = getString(
                    R.string.selezioneAccessoRegistrazione_tipoAccount,
                    stringaTipoAccount
                )
            }

            "venditore" -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_venditore)
                tipoAccount.text = getString(
                    R.string.selezioneAccessoRegistrazione_tipoAccount,
                    stringaTipoAccount
                )
            }
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        pulsanteIndietro.setOnClickListener { clickIndietro() }
        pulsanteAccedi.setOnClickListener { clickAccedi() }

        pulsanteFacebook.permissions = listOf(
            "email", "public_profile", "user_birthday",
            "user_gender", "user_link", "user_location"
        )
        pulsanteFacebook.authType = "rerequest"
        pulsanteFacebook.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Toast.makeText(
                        applicationContext,
                        "Login effettuato con successo.",
                        Toast.LENGTH_SHORT
                    ).show()

                    TODO("Se l'account Facebook non è associato a nessun account DietiDeals24, crea un nuovo account, altrimenti vai alla home")
                }

                override fun onCancel() {
                    // Non fare nulla
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(
                        applicationContext,
                        "Errore durante l'accesso con Facebook.",
                        Toast.LENGTH_SHORT
                    ).show()

                    rimuoviMessaggioErrore(layout, 2)
                    erroreCampo(
                        layout,
                        getString(R.string.accesso_erroreSocial),
                        2
                    )
                }
            })
    }

    @UIBuilder
    override fun impostaEventiDiCambiamentoCampi() {
        email.addTextChangedListener {
            rimuoviErroreCampo(campoEmail)
        }

        password.addTextChangedListener {
            rimuoviErroreCampo(campoPassword)
        }
    }


    @EventHandler
    private fun clickIndietro() {
        cambiaAttivita(ControllerSelezioneAccessoRegistrazione::class.java)
    }

    @EventHandler
    private fun clickAccedi() {
        viewModel.email = estraiTestoDaElemento(email)
        viewModel.password = estraiTestoDaElemento(password)
        viewModel.tipoAccount = estraiTestoDaElemento(tipoAccount)

        try {
            viewModel.validate()

            val returned: Int? = eseguiChiamataREST<Int>(
                "accedi",
                viewModel.email,
                viewModel.password,
                viewModel.tipoAccount
            )
            if (returned == null) throw EccezioneAPI("Errore di comunicazione con il server.")
            else if (returned == 0) throw EccezioneAccountNonEsistente("Email non associata.")
            else TODO("Se è stata completata la registrazione, vai alla home, se non è stata completata, vai alla registrazione")
        } catch (eccezione: EccezioneAccountNonEsistente) {
            rimuoviMessaggioErrore(layout, 2)
            erroreCampo(
                layout,
                getString(R.string.accesso_erroreCredenzialiNonCorrette),
                2,
                campoEmail, campoPassword
            )
        } catch (eccezione: EccezioneAPI) {
            Toast.makeText(
                applicationContext,
                "Errore di comunicazione con il server.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
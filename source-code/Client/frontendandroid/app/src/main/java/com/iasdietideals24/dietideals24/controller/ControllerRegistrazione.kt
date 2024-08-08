package com.iasdietideals24.dietideals24.controller

import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.iasdietideals24.dietideals24.model.ModelControllerRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneAPI
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneEmailNonValida
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneEmailUsata
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezionePasswordNonSicura

class ControllerRegistrazione : Controller(R.layout.registrazione) {
    private var viewModel: ModelControllerRegistrazione = ModelControllerRegistrazione()

    private var callbackManager = create()

    private lateinit var tipoAccount: MaterialTextView
    private lateinit var campoEmail: TextInputLayout
    private lateinit var email: TextInputEditText
    private lateinit var campoPassword: TextInputLayout
    private lateinit var password: TextInputEditText
    private lateinit var pulsanteIndietro: ImageButton
    private lateinit var pulsanteAvanti: MaterialButton
    private lateinit var pulsanteFacebook: LoginButton
    private lateinit var linearLayout: LinearLayout
    private lateinit var constraintLayout: ConstraintLayout

    override fun elaborazioneAggiuntiva() {
        /* Al fine di rendere il popup di info password più visibile, il frame di registrazione ha
        una cortina nera come foreground. Viene resa invisibile quando la pagina di registrazione è
        creata e quando è chiuso il popup. Viene resa visibile quando il popup è aperto. */
        constraintLayout.foreground.alpha = 0
    }


    @UIBuilder
    override fun trovaElementiInterfaccia() {
        tipoAccount = findViewById(R.id.registrazione_tipoAccount)
        campoEmail = findViewById(R.id.registrazione_campoEmail)
        email = findViewById(R.id.registrazione_email)
        campoPassword = findViewById(R.id.registrazione_campoPassword)
        password = findViewById(R.id.registrazione_password)
        pulsanteIndietro = findViewById(R.id.registrazione_pulsanteIndietro)
        pulsanteAvanti = findViewById(R.id.registrazione_pulsanteAvanti)
        pulsanteFacebook = findViewById(R.id.registrazione_pulsanteFacebook)
        linearLayout = findViewById(R.id.registrazione_linearLayout)
        constraintLayout = findViewById(R.id.registrazione_constraintLayout)
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        when (intent.extras?.getString("tipoAccount")) {
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
        campoPassword.setEndIconOnClickListener { clickInfoPassword() }
        pulsanteIndietro.setOnClickListener { clickIndietro() }
        pulsanteAvanti.setOnClickListener { clickAvanti() }

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

                    scegliAssociaCreaProfilo(
                        viewModel.email,
                        viewModel.password,
                        viewModel.tipoAccount
                    )
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

                    rimuoviMessaggioErrore(linearLayout, 2)
                    erroreCampo(
                        linearLayout,
                        getString(R.string.registrazione_erroreRegistrazioneSocial),
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
    private fun clickInfoPassword() {
        mostraPopupInfoPassword()
    }

    @EventHandler
    private fun clickIndietro() {
        cambiaAttivita(
            ControllerSelezioneAccessoRegistrazione::class.java,
            Pair("tipoAccount", intent.extras?.getString("tipoAccount")!!)
        )
    }

    @EventHandler
    private fun clickAvanti() {
        viewModel.email = estraiTestoDaElemento(email)
        viewModel.password = estraiTestoDaElemento(password)
        viewModel.tipoAccount = estraiTestoDaElemento(tipoAccount)

        try {
            viewModel.validate()

            scegliAssociaCreaProfilo(viewModel.email, viewModel.password, viewModel.tipoAccount)
        } catch (eccezione: EccezioneCampiNonCompilati) {
            rimuoviMessaggioErrore(linearLayout, 2)
            erroreCampo(
                linearLayout,
                getString(R.string.registrazione_erroreCampiObbligatoriNonCompilati),
                2,
                campoEmail, campoPassword
            )
        } catch (eccezione: EccezioneEmailNonValida) {
            rimuoviMessaggioErrore(linearLayout, 2)
            erroreCampo(
                linearLayout,
                getString(R.string.registrazione_erroreFormatoEmail),
                2,
                campoEmail
            )
        } catch (eccezione: EccezioneEmailUsata) {
            rimuoviMessaggioErrore(linearLayout, 2)
            erroreCampo(
                linearLayout,
                getString(R.string.registrazione_erroreEmailGiàUsata),
                2,
                campoEmail
            )
        } catch (eccezione: EccezionePasswordNonSicura) {
            rimuoviMessaggioErrore(linearLayout, 2)
            erroreCampo(
                linearLayout,
                getString(R.string.registrazione_errorePasswordNonSicura),
                2,
                campoPassword
            )
        } catch (eccezione: EccezioneAPI) {
            Toast.makeText(
                applicationContext,
                "Errore di comunicazione con il server.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun scegliAssociaCreaProfilo(email: String, password: String, tipoAccount: String) {
        val returned: Pair<Boolean, Int>? =
            eseguiChiamataREST<Pair<Boolean, Int>>("registra", email, password, tipoAccount)

        if (returned == null) throw EccezioneAPI("Errore di comunicazione con il server.")
        // Un account di altro tipo con la stessa email è stato trovato
        else if (returned.first == true) {
            val nuovaAttivita = Intent(this, ControllerAssociazioneProfilo::class.java)
            startActivity(nuovaAttivita)
        }
        // Un account di altro tipo con la stessa email non è stato trovato
        else TODO("Crea account")
    }
    //endregion


    //region PopupInfoPassword
    private lateinit var constraintLayoutPopup: ConstraintLayout
    private lateinit var pulsanteChiudi: ImageButton

    @UIBuilder
    private fun mostraPopupInfoPassword() {
        val layoutPopup = gonfiaLayout()

        val finestraPopup = creaFinestraPopup(layoutPopup)

        trovaElementiInterfaccia(layoutPopup)

        /* Al fine di rendere il popup di info password più visibile, il frame di registrazione ha
        una cortina nera come foreground. Viene resa visibile quando la pagina di registrazione è
        creata e quando è aperto il popup. Viene resa invisibile quando il popup è chiuso. */
        impostaTrasparenzaCortina(150)

        impostaEventiClick(finestraPopup)

        finestraPopup.showAtLocation(layoutPopup, Gravity.CENTER, 0, 0)
    }

    @UIBuilder
    private fun gonfiaLayout(): View {
        val servizioInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val radiceLayout = findViewById<ConstraintLayout>(R.id.infoPassword_constraintLayout)

        return servizioInflater.inflate(R.layout.infopassword, radiceLayout)
    }

    @UIBuilder
    private fun creaFinestraPopup(popupDaCreare: View): PopupWindow {
        val larghezzaFinestra = LinearLayout.LayoutParams.WRAP_CONTENT
        val altezzaFinestra = LinearLayout.LayoutParams.WRAP_CONTENT
        val chiusuraConTapEsterno = false

        return PopupWindow(popupDaCreare, larghezzaFinestra, altezzaFinestra, chiusuraConTapEsterno)
    }

    @UIBuilder
    private fun trovaElementiInterfaccia(layoutPopupCreato: View) {
        constraintLayoutPopup = findViewById(R.id.registrazione_constraintLayout)
        pulsanteChiudi = layoutPopupCreato.findViewById(R.id.infoPassword_pulsanteChiudi)
    }

    @UIBuilder
    private fun impostaTrasparenzaCortina(alpha: Int) {
        constraintLayoutPopup.foreground.alpha = alpha
    }

    @UIBuilder
    private fun impostaEventiClick(finestraPopupCreata: PopupWindow) {
        pulsanteChiudi.setOnClickListener { clickChiudi(finestraPopupCreata) }
    }


    @EventHandler
    private fun clickChiudi(finestraPopupCreata: PopupWindow) {
        finestraPopupCreata.dismiss()

        impostaTrasparenzaCortina(0)
    }
    //endregion
}

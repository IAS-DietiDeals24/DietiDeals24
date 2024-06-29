package com.iasdietideals24.dietideals24.controller

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.model.ModelControllerRegistrazione
import com.iasdietideals24.dietideals24.utilities.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.EccezioneCollegamentoSocialNonRiuscito
import com.iasdietideals24.dietideals24.utilities.EccezioneEmailNonValida
import com.iasdietideals24.dietideals24.utilities.EccezioneEmailUsata
import com.iasdietideals24.dietideals24.utilities.EccezionePasswordNonSicura
import com.iasdietideals24.dietideals24.utilities.ErrorHandler
import com.iasdietideals24.dietideals24.utilities.EventHandler
import com.iasdietideals24.dietideals24.utilities.UIBuilder
import com.iasdietideals24.dietideals24.utilities.Utility
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


class ControllerRegistrazione : AppCompatActivity() {
    private var viewModel: ModelControllerRegistrazione = ModelControllerRegistrazione()

    private lateinit var tipoAccount: MaterialTextView
    private lateinit var campoEmail: TextInputLayout
    private lateinit var email: TextInputEditText
    private lateinit var campoPassword: TextInputLayout
    private lateinit var password: TextInputEditText
    private lateinit var pulsanteIndietro: ImageButton
    private lateinit var pulsanteAvanti: MaterialButton
    private lateinit var pulsanteGoogle: ShapeableImageView
    private lateinit var pulsanteFacebook: ShapeableImageView
    private lateinit var pulsanteGitHub: ShapeableImageView
    private lateinit var pulsanteX: ShapeableImageView
    private lateinit var linearLayout: LinearLayout
    private lateinit var constraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.registrazione)

        trovaElementiInterfaccia()

        /* Al fine di rendere il popup di info password più visibile, il frame di registrazione ha
        una cortina nera come foreground. Viene resa invisibile quando la pagina di registrazione è
        creata e quando è chiuso il popup. Viene resa visibile quando il popup è aperto. */
        constraintLayout.foreground.alpha = 0

        impostaMessaggioCorpo()

        impostaEventiClick()

        impostaEventiDiCambiamentoCampi()
    }


    @UIBuilder
    private fun trovaElementiInterfaccia() {
        tipoAccount = findViewById(R.id.registrazione_tipoAccount)
        campoEmail = findViewById(R.id.registrazione_campoEmail)
        email = findViewById(R.id.registrazione_email)
        campoPassword = findViewById(R.id.registrazione_campoPassword)
        password = findViewById(R.id.registrazione_password)
        pulsanteIndietro = findViewById(R.id.registrazione_pulsanteIndietro)
        pulsanteAvanti = findViewById(R.id.registrazione_pulsanteAvanti)
        pulsanteGoogle = findViewById(R.id.registrazione_pulsanteGoogle)
        pulsanteFacebook = findViewById(R.id.registrazione_pulsanteFacebook)
        pulsanteGitHub = findViewById(R.id.registrazione_pulsanteGitHub)
        pulsanteX = findViewById(R.id.registrazione_pulsanteX)
        linearLayout = findViewById(R.id.registrazione_linearLayout)
        constraintLayout = findViewById(R.id.registrazione_constraintLayout)
    }

    @UIBuilder
    private fun impostaMessaggioCorpo() {
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
    private fun impostaEventiClick() {
        campoPassword.setEndIconOnClickListener { clickInfoPassword() }
        pulsanteIndietro.setOnClickListener { clickIndietro() }
        pulsanteAvanti.setOnClickListener { clickAvanti() }
        pulsanteGoogle.setOnClickListener { clickGoogle() }
        pulsanteFacebook.setOnClickListener { clickFacebook() }
        pulsanteGitHub.setOnClickListener { clickGitHub() }
        pulsanteX.setOnClickListener { clickX() }
    }

    @UIBuilder
    private fun impostaEventiDiCambiamentoCampi() {
        email.addTextChangedListener {
            rimuoviErroreCampo(campoEmail)
        }

        password.addTextChangedListener {
            rimuoviErroreCampo(campoPassword)
        }
    }


    @ErrorHandler
    private fun clickInfoPassword() {
        TODO()
    }

    @EventHandler
    private fun clickIndietro() {
        val nuovaAttivita = Intent(this, ControllerSelezioneAccessoRegistrazione::class.java)
        nuovaAttivita.putExtra("tipoAccount", intent.extras?.getString("tipoAccount"))
        startActivity(nuovaAttivita)
    }

    @EventHandler
    private fun clickAvanti() {
        viewModel.email = estraiTestoDaElemento(email)
        viewModel.password = estraiTestoDaElemento(password)

        try {
            viewModel.validate()

            scegliAssociaCreaProfilo(viewModel.email, viewModel.password)
        } catch (eccezione: EccezioneCampiNonCompilati) {
            rimuoviMessaggioErrore(linearLayout)
            erroreCampiObbligatoriNonCompilati()
        } catch (eccezione: EccezioneEmailNonValida) {
            rimuoviMessaggioErrore(linearLayout)
            erroreFormatoEmail()
        } catch (eccezione: EccezioneEmailUsata) {
            rimuoviMessaggioErrore(linearLayout)
            erroreEmailGiaUsata()
        } catch (eccezione: EccezionePasswordNonSicura) {
            rimuoviMessaggioErrore(linearLayout)
            errorePasswordNonSicura()
        }
    }

    @EventHandler
    private fun clickGoogle() {
        try {
            registrazioneGoogle()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            rimuoviMessaggioErrore(linearLayout)
            erroreRegistrazioneSocial()
        }
    }

    @EventHandler
    private fun clickFacebook() {
        try {
            registrazioneFacebook()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            rimuoviMessaggioErrore(linearLayout)
            erroreRegistrazioneSocial()
        }
    }

    @EventHandler
    private fun clickGitHub() {
        try {
            registrazioneGitHub()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            rimuoviMessaggioErrore(linearLayout)
            erroreRegistrazioneSocial()
        }
    }

    @EventHandler
    private fun clickX() {
        try {
            registrazioneX()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            rimuoviMessaggioErrore(linearLayout)
            erroreRegistrazioneSocial()
        }
    }


    private fun scegliAssociaCreaProfilo(email: String, password: String) {
        val accountGiaRegistrato = recuperaAccountRegistrato(email)

        // Un account di altro tipo con la stessa email è stato trovato
        if (accountGiaRegistrato != null) {
            associaAccount(accountGiaRegistrato, email, password)
        }
        // Un account di altro tipo con la stessa email non è stato trovato
        else {
            creaAccount(email, password)
        }
    }

    @GET("account?email={email}")
    private fun recuperaAccountRegistrato(@Path("email") email: String): Call<Any>? {
        TODO()
    }

    @POST("account/update")
    private fun associaAccount(
        accountGiaRegistrato: Call<Any>?,
        emailRegistrazione: String,
        passwordRegistrazione: String
    ) {
        TODO()
    }

    @POST("account/new")
    private fun creaAccount(email: String, password: String) {
        TODO()
    }

    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    private fun registrazioneGoogle(): Boolean {
        TODO()
    }

    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    private fun registrazioneFacebook(): Boolean {
        TODO()
    }

    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    private fun registrazioneGitHub(): Boolean {
        TODO()
    }

    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    private fun registrazioneX(): Boolean {
        TODO()
    }


    @Utility
    private fun estraiTestoDaElemento(elemento: TextInputEditText): String {
        val testoElemento = elemento.text
        return testoElemento.toString()
    }

    @Utility
    private fun rimuoviErroreCampo(vararg campiEvidenziati: TextInputLayout) {
        for (campo in campiEvidenziati)
            campo.error = null
    }

    @Utility
    private fun rimuoviMessaggioErrore(layoutDoveEliminareMessaggio: LinearLayout) {
        layoutDoveEliminareMessaggio.removeViewAt(2)
    }

    @Utility
    private fun evidenziaCampiErrore(vararg campiDaEvidenziare: TextInputLayout) {
        for (campo in campiDaEvidenziare)
            campo.error = getString(R.string.accesso_erroreCampi)
    }

    @Utility
    private fun creaMessaggioErrore(testoMessaggio: String): MaterialTextView {
        val messaggio = MaterialTextView(this)

        messaggio.text = testoMessaggio

        messaggio.textSize = 20f

        val grigio = resources.getColor(R.color.grigio, theme)
        messaggio.setBackgroundColor(grigio)

        val rosso = resources.getColor(R.color.rosso, theme)
        messaggio.setTextColor(rosso)

        val parametriLayoutTextView =
            LayoutParams(420 * resources.displayMetrics.density.toInt(), MATCH_PARENT)
        messaggio.layoutParams = parametriLayoutTextView

        messaggio.setBackgroundResource(R.drawable.errore)

        messaggio.setPadding(60, 0, 0, 0)

        return messaggio
    }


    @ErrorHandler
    private fun erroreEmailGiaUsata() {
        creaMessaggioErroreEmailGiaUsata(linearLayout)
        evidenziaCampiErrore(campoEmail)
    }

    @ErrorHandler
    private fun errorePasswordNonSicura() {
        creaMessaggioErrorePasswordNonSicura(linearLayout)
        evidenziaCampiErrore(campoPassword)
    }

    @ErrorHandler
    private fun erroreCampiObbligatoriNonCompilati() {
        creaMessaggioErroreCampiObbligatoriNonCompilati(linearLayout)
        evidenziaCampiErrore(campoEmail, campoPassword)
    }

    @ErrorHandler
    private fun erroreRegistrazioneSocial() {
        creaMessaggioErroreRegistrazioneSocial(linearLayout)
    }

    @ErrorHandler
    private fun erroreFormatoEmail() {
        creaMessaggioErroreFormatoEmail(linearLayout)
        evidenziaCampiErrore(campoEmail)
    }

    @ErrorHandler
    private fun creaMessaggioErroreEmailGiaUsata(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.registrazione_erroreEmailGiàUsata)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore, 2)
    }

    @ErrorHandler
    private fun creaMessaggioErrorePasswordNonSicura(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.registrazione_errorePasswordNonSicura)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore, 2)
    }

    @ErrorHandler
    private fun creaMessaggioErroreCampiObbligatoriNonCompilati(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio =
            getString(R.string.registrazione_erroreCampiObbligatoriNonCompilati)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore, 2)
    }

    @ErrorHandler
    private fun creaMessaggioErroreRegistrazioneSocial(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.registrazione_erroreRegistrazioneSocial)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore, 2)
    }

    @ErrorHandler
    private fun creaMessaggioErroreFormatoEmail(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.registrazione_erroreFormatoEmail)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore, 2)
    }
}

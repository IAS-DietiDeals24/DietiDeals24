package com.iasdietideals24.dietideals24.controller

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.model.ModelControllerAccesso
import com.iasdietideals24.dietideals24.utilities.EccezioneAccountNonEsistente
import com.iasdietideals24.dietideals24.utilities.EccezioneCollegamentoSocialNonRiuscito
import com.iasdietideals24.dietideals24.utilities.ErrorHandler
import com.iasdietideals24.dietideals24.utilities.EventHandler
import com.iasdietideals24.dietideals24.utilities.UIBuilder
import com.iasdietideals24.dietideals24.utilities.Utility
import retrofit2.Call
import retrofit2.http.GET


class ControllerAccesso : AppCompatActivity() {
    private var viewModel: ModelControllerAccesso = ModelControllerAccesso()

    private lateinit var tipoAccount: MaterialTextView
    private lateinit var campoEmail: TextInputLayout
    private lateinit var email: TextInputEditText
    private lateinit var campoPassword: TextInputLayout
    private lateinit var password: TextInputEditText
    private lateinit var pulsanteIndietro: ImageButton
    private lateinit var pulsanteAccedi: MaterialButton
    private lateinit var pulsanteGoogle: ShapeableImageView
    private lateinit var pulsanteFacebook: ShapeableImageView
    private lateinit var pulsanteGitHub: ShapeableImageView
    private lateinit var pulsanteX: ShapeableImageView
    private lateinit var layout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.accesso)

        trovaElementiInterfaccia()

        impostaMessaggioCorpo()

        impostaEventiClick()

        impostaEventiDiCambiamentoCampi()
    }


    @UIBuilder
    private fun trovaElementiInterfaccia() {
        tipoAccount = findViewById(R.id.accesso_tipoAccount)
        campoEmail = findViewById(R.id.accesso_campoEmail)
        email = findViewById(R.id.accesso_email)
        campoPassword = findViewById(R.id.accesso_campoPassword)
        password = findViewById(R.id.accesso_password)
        pulsanteIndietro = findViewById(R.id.accesso_pulsanteIndietro)
        pulsanteAccedi = findViewById(R.id.accesso_pulsanteAccedi)
        pulsanteGoogle = findViewById(R.id.accesso_pulsanteGoogle)
        pulsanteFacebook = findViewById(R.id.accesso_pulsanteFacebook)
        pulsanteGitHub = findViewById(R.id.accesso_pulsanteGitHub)
        pulsanteX = findViewById(R.id.accesso_pulsanteX)
        layout = findViewById(R.id.accesso_linearLayout)
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
        pulsanteIndietro.setOnClickListener { clickIndietro() }
        pulsanteAccedi.setOnClickListener { clickAccedi() }
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


    @EventHandler
    private fun clickIndietro() {
        val nuovaAttivita = Intent(this, ControllerSelezioneAccessoRegistrazione::class.java)
        nuovaAttivita.putExtra("tipoAccount", intent.extras?.getString("tipoAccount"))
        startActivity(nuovaAttivita)
    }

    @EventHandler
    private fun clickAccedi() {
        viewModel.email = estraiTestoDaElemento(email)
        viewModel.password = estraiTestoDaElemento(password)

        try {
            viewModel.validate()

            accedi(viewModel.email, viewModel.password)
        } catch (eccezione: EccezioneAccountNonEsistente) {
            rimuoviMessaggioErrore(layout)
            erroreCredenzialiNonCorrette()
        }
    }

    @EventHandler
    private fun clickGoogle() {
        try {
            accessoGoogle()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            rimuoviMessaggioErrore(layout)
            erroreAccessoSocial()
        }
    }

    @EventHandler
    private fun clickFacebook() {
        try {
            accessoFacebook()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            rimuoviMessaggioErrore(layout)
            erroreAccessoSocial()
        }
    }

    @EventHandler
    private fun clickGitHub() {
        try {
            accessoGitHub()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            rimuoviMessaggioErrore(layout)
            erroreAccessoSocial()
        }
    }

    @EventHandler
    private fun clickX() {
        try {
            accessoConX()
        } catch (eccezione: EccezioneCollegamentoSocialNonRiuscito) {
            rimuoviMessaggioErrore(layout)
            erroreAccessoSocial()
        }
    }


    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    private fun accessoGoogle() {
        TODO()
    }

    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    private fun accessoFacebook() {
        TODO()
    }

    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    private fun accessoGitHub() {
        TODO()
    }

    @Throws(EccezioneCollegamentoSocialNonRiuscito::class)
    private fun accessoConX() {
        TODO()
    }

    @GET("account?email={email}&password={password}")
    @Throws(EccezioneAccountNonEsistente::class)
    private fun accedi(email: String, password: String): Call<Any>? {
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
    private fun erroreCredenzialiNonCorrette() {
        creaMessaggioErroreCredenzialiNonCorrette(layout)
        evidenziaCampiErrore(campoEmail, campoPassword)
    }

    @ErrorHandler
    private fun erroreAccessoSocial() {
        creaMessaggioErroreSocial(layout)
    }

    @ErrorHandler
    private fun creaMessaggioErroreCredenzialiNonCorrette(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.accesso_erroreCredenzialiNonCorrette)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore, 2)
    }

    @ErrorHandler
    private fun creaMessaggioErroreSocial(layoutDoveInserireErrore: LinearLayout) {
        val testoMessaggio = getString(R.string.accesso_erroreSocial)
        val messaggioDiErrore = creaMessaggioErrore(testoMessaggio)

        layoutDoveInserireErrore.addView(messaggioDiErrore, 2)
    }
}
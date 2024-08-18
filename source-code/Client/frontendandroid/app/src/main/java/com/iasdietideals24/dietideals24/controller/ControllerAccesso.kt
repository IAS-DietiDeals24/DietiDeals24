package com.iasdietideals24.dietideals24.controller

import android.content.Context
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
import com.iasdietideals24.dietideals24.activities.Home
import com.iasdietideals24.dietideals24.model.ModelAccesso
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneAPI
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneAccountNonEsistente
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeActivity
import kotlinx.coroutines.runBlocking

class ControllerAccesso : Controller(R.layout.accesso) {

    private var viewModel: ModelAccesso = ModelAccesso()

    private lateinit var tipoAccount: MaterialTextView
    private lateinit var campoEmail: TextInputLayout
    private lateinit var email: TextInputEditText
    private lateinit var campoPassword: TextInputLayout
    private lateinit var password: TextInputEditText
    private lateinit var pulsanteIndietro: ImageButton
    private lateinit var pulsanteAccedi: MaterialButton
    private lateinit var pulsanteFacebook: LoginButton
    private lateinit var layout: LinearLayout

    private var facebookCallbackManager = create()

    private var listenerBackButton: OnFragmentBackButton? = null
    private var listenerChangeActivity: OnFragmentChangeActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnFragmentBackButton) {
            listenerBackButton = requireContext() as OnFragmentBackButton
        }
        if (requireContext() is OnFragmentChangeActivity) {
            listenerChangeActivity = requireContext() as OnFragmentChangeActivity
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerBackButton = null
        listenerChangeActivity = null
    }

    @UIBuilder
    override fun trovaElementiInterfaccia() {
        tipoAccount = fragmentView.findViewById(R.id.accesso_tipoAccount)
        campoEmail = fragmentView.findViewById(R.id.accesso_campoEmail)
        email = fragmentView.findViewById(R.id.accesso_email)
        campoPassword = fragmentView.findViewById(R.id.accesso_campoPassword)
        password = fragmentView.findViewById(R.id.accesso_password)
        pulsanteIndietro = fragmentView.findViewById(R.id.accesso_pulsanteIndietro)
        pulsanteAccedi = fragmentView.findViewById(R.id.accesso_pulsanteAccedi)
        pulsanteFacebook = fragmentView.findViewById(R.id.accesso_pulsanteFacebook)
        layout = fragmentView.findViewById(R.id.accesso_linearLayout1)
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        when (runBlocking { caricaPreferenzaStringa("tipoAccount") }) {
            "compratore" -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_compratore)
                tipoAccount.text = getString(
                    R.string.placeholder,
                    stringaTipoAccount
                )
            }

            "venditore" -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_venditore)
                tipoAccount.text = getString(
                    R.string.placeholder,
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
            facebookCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Toast.makeText(
                        fragmentContext,
                        getString(R.string.accesso_loginSocialOK),
                        Toast.LENGTH_SHORT
                    ).show()

                    val returned: Int? = eseguiChiamataREST(
                        "accountFacebook",
                        result.accessToken.userId, tipoAccount.text.toString()
                    )

                    if (returned == null) // errore comunicazione con il backend
                        Toast.makeText(
                            fragmentContext,
                            getString(R.string.apiError),
                            Toast.LENGTH_SHORT
                        ).show()
                    else if (returned == 0) // non esiste un account associato a questo account Facebook con questo tipo
                        Toast.makeText(
                            fragmentContext,
                            getString(R.string.accesso_noAccountFacebookCollegato),
                            Toast.LENGTH_SHORT
                        ).show()
                    else // esiste un account associato a questo account Facebook con questo tipo, accedi
                        listenerChangeActivity?.onFragmentChangeActivity(Home::class.java)
                }

                override fun onCancel() {
                    // Non fare nulla
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(
                        fragmentContext,
                        getString(R.string.accesso_erroreSocial),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    @UIBuilder
    override fun impostaEventiDiCambiamentoCampi() {
        email.addTextChangedListener {
            rimuoviErroreCampo(campoEmail)
            rimuoviErroreCampo(campoPassword)
        }

        password.addTextChangedListener {
            rimuoviErroreCampo(campoEmail)
            rimuoviErroreCampo(campoPassword)
        }
    }

    @EventHandler
    private fun clickIndietro() {
        listenerBackButton?.onFragmentBackButton()
    }

    @EventHandler
    private fun clickAccedi() {
        viewModel.email.value = estraiTestoDaElemento(email)
        viewModel.password.value = estraiTestoDaElemento(password)
        viewModel.tipoAccount.value = estraiTestoDaElemento(tipoAccount)

        try {
            viewModel.validate()

            val returned: Int? = eseguiChiamataREST(
                "accedi",
                viewModel.email.value,
                viewModel.password.value,
                viewModel.tipoAccount.value
            )
            if (returned == null)
                throw EccezioneAPI("Errore di comunicazione con il server.")
            else if (returned == 0)
                throw EccezioneAccountNonEsistente("Account non esistente.")
            else
                listenerChangeActivity?.onFragmentChangeActivity(Home::class.java)
        } catch (eccezione: EccezioneAccountNonEsistente) {
            erroreCampo(R.string.accesso_erroreCredenzialiNonCorrette, campoEmail, campoPassword)
        } catch (eccezione: EccezioneAPI) {
            Toast.makeText(
                fragmentContext,
                getString(R.string.apiError),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
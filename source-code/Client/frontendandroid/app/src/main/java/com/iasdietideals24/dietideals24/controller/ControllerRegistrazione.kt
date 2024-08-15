package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Context.WINDOW_SERVICE
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.Display
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.WindowMetrics
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.facebook.AccessToken
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneAPI
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneEmailNonValida
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneEmailUsata
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezionePasswordNonSicura
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeActivity
import kotlinx.coroutines.runBlocking
import org.apache.commons.lang3.RandomStringUtils
import org.json.JSONObject


class ControllerRegistrazione : Controller(R.layout.registrazione) {

    private lateinit var viewModel: ModelRegistrazione

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

    override fun elaborazioneAggiuntiva() {
        /* Al fine di rendere il popup di info password più visibile, il frame di registrazione ha
        una cortina nera come foreground. Viene resa invisibile quando la pagina di registrazione è
        creata e quando è chiuso il popup. Viene resa visibile quando il popup è aperto. */
        constraintLayout.foreground.alpha = 0

        viewModel = ViewModelProvider(fragmentActivity).get(ModelRegistrazione::class)
    }

    @UIBuilder
    override fun trovaElementiInterfaccia() {
        tipoAccount = fragmentView.findViewById(R.id.registrazione_tipoAccount)
        campoEmail = fragmentView.findViewById(R.id.registrazione_campoEmail)
        email = fragmentView.findViewById(R.id.registrazione_email)
        campoPassword = fragmentView.findViewById(R.id.registrazione_campoPassword)
        password = fragmentView.findViewById(R.id.registrazione_password)
        pulsanteIndietro = fragmentView.findViewById(R.id.registrazione_pulsanteIndietro)
        pulsanteAvanti = fragmentView.findViewById(R.id.registrazione_pulsanteAvanti)
        pulsanteFacebook = fragmentView.findViewById(R.id.registrazione_pulsanteFacebook)
        linearLayout = fragmentView.findViewById(R.id.registrazione_linearLayout)
        constraintLayout = fragmentView.findViewById(R.id.registrazione_constraintLayout)
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        when (runBlocking { caricaPreferenzaStringa("tipoAccount") }) {
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
            "openid", "email", "public_profile", "user_birthday",
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

                    if (returned == null) { // errore di comunicazione con il backend
                        Toast.makeText(
                            fragmentContext,
                            getString(R.string.apiError),
                            Toast.LENGTH_SHORT
                        ).show()

                        LoginManager.getInstance().logOut()
                    } else if (returned == 0) { // non esiste un account associato a questo account Facebook con questo tipo, registrati
                        queryFacebookGraph(result.accessToken)


                        try {
                            scegliAssociaCreaProfilo()
                        } catch (eccezione: EccezioneAPI) {
                            Toast.makeText(
                                fragmentContext,
                                getString(R.string.apiError),
                                Toast.LENGTH_SHORT
                            ).show()

                            viewModel.clear()
                            LoginManager.getInstance().logOut()
                        }
                    } else { // esiste un account associato a questo account Facebook con questo tipo
                        Toast.makeText(
                            fragmentContext,
                            getString(R.string.registrazione_accountFacebookGiàCollegato),
                            Toast.LENGTH_SHORT
                        ).show()

                        LoginManager.getInstance().logOut()
                    }
                }

                override fun onCancel() {
                    // Non fare nulla
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(
                        fragmentContext,
                        getString(R.string.registrazione_erroreRegistrazioneSocial),
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
    private fun clickInfoPassword() {
        mostraPopupInfoPassword()
    }

    @EventHandler
    private fun clickIndietro() {
        listenerBackButton?.onFragmentBackButton()
    }

    @EventHandler
    private fun clickAvanti() {
        viewModel.clear()
        viewModel.email.value = estraiTestoDaElemento(email)
        viewModel.password.value = estraiTestoDaElemento(password)
        viewModel.tipoAccount.value = estraiTestoDaElemento(tipoAccount)

        try {
            viewModel.validateAccount()

            scegliAssociaCreaProfilo()
        } catch (eccezione: EccezioneCampiNonCompilati) {
            erroreCampo(
                R.string.registrazione_erroreCampiObbligatoriNonCompilati,
                campoEmail,
                campoPassword
            )
        } catch (eccezione: EccezioneEmailNonValida) {
            erroreCampo(R.string.registrazione_erroreFormatoEmail, campoEmail)
        } catch (eccezione: EccezioneEmailUsata) {
            erroreCampo(R.string.registrazione_erroreEmailGiàUsata, campoEmail)
        } catch (eccezione: EccezionePasswordNonSicura) {
            erroreCampo(R.string.registrazione_errorePasswordNonSicura, campoPassword)
        } catch (eccezione: EccezioneAPI) {
            Toast.makeText(
                context,
                getString(R.string.apiError),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun scegliAssociaCreaProfilo() {
        val returned: Boolean? = eseguiChiamataREST(
            "associaCreaProfilo",
            viewModel.email.value,
            viewModel.password.value,
            viewModel.tipoAccount.value
        )

        when {
            returned == null -> throw EccezioneAPI("Errore di comunicazione con il server.")

            // Un account di altro tipo con la stessa email è stato trovato
            returned == true -> navController.navigate(R.id.action_controllerRegistrazione_to_controllerAssociazioneProfilo)

            // Un account di altro tipo con la stessa email non è stato trovato
            returned == false -> navController.navigate(R.id.action_controllerRegistrazione_to_controllerCreazioneProfiloFase1)
        }
    }

    private fun queryFacebookGraph(accessToken: AccessToken?) {
        val request: GraphRequest = GraphRequest.newMeRequest(
            accessToken,
            object : GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(obj: JSONObject?, response: GraphResponse?) {
                    viewModel.facebookAccountID.postValue(obj?.optString("id"))
                    viewModel.email.postValue(obj?.optString("email"))
                    viewModel.password.postValue(RandomStringUtils.randomAlphanumeric(16))
                    viewModel.nome.postValue(
                        obj?.optString("name") +
                                if (obj?.optString("middle_name") != "")
                                    " " + obj?.optString("middle_name")
                                else ""
                    )
                    viewModel.cognome.postValue(obj?.optString("last_name"))
                    viewModel.dataNascita.postValue(obj?.optString("birthday")
                        ?.let { estraiDataDaStringa("MM/dd/yyyy", it) })
                    viewModel.immagineProfilo.postValue(
                        obj?.optJSONObject("picture")?.optJSONObject("data")?.optString("url")
                            ?.toUri()
                    )
                    viewModel.biografia.postValue(obj?.optString("about"))
                    viewModel.areaGeografica.postValue(
                        obj?.optJSONObject("location")?.optString("name")
                    )
                    viewModel.genere.postValue(obj?.optString("gender"))
                }
            })

        val parameters = Bundle()
        parameters.putString(
            "fields",
            "email,name,middle_name,last_name,birthday,picture,about,location,gender"
        )
        request.parameters = parameters
        request.executeAsync()
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
        val servizioInflater =
            fragmentContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val radiceLayout =
            fragmentView.findViewById<ConstraintLayout>(R.id.infoPassword_constraintLayout)

        return servizioInflater.inflate(R.layout.infopassword, radiceLayout)
    }

    @UIBuilder
    private fun creaFinestraPopup(popupDaCreare: View): PopupWindow {
        val display: Display?
        val windowManager: WindowManager =
            fragmentContext.getSystemService(WINDOW_SERVICE) as WindowManager
        val windowMetrics: WindowMetrics
        val size = Point()
        val width: Int

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowMetrics = windowManager.currentWindowMetrics
            width = windowMetrics.bounds.width()
        } else {
            display = windowManager.defaultDisplay
            display?.getSize(size)
            width = size.x
        }

        val larghezzaFinestra = width
        val chiusuraConTapEsterno = false

        return PopupWindow(
            popupDaCreare,
            (larghezzaFinestra / 1.5).toInt(),
            LinearLayout.LayoutParams.WRAP_CONTENT,
            chiusuraConTapEsterno
        )
    }

    @UIBuilder
    private fun trovaElementiInterfaccia(layoutPopupCreato: View) {
        constraintLayoutPopup = fragmentView.findViewById(R.id.registrazione_constraintLayout)
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

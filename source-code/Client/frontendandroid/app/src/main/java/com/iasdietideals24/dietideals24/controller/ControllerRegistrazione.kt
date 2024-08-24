package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.os.Bundle
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.RegistrazioneBinding
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ControllerRegistrazione : Controller<RegistrazioneBinding>() {

    private lateinit var viewModel: ModelRegistrazione

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
    override fun impostaMessaggiCorpo() {
        when (runBlocking { caricaPreferenzaStringa("tipoAccount") }) {
            "compratore" -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_compratore)
                binding.registrazioneTipoAccount.text = getString(
                    R.string.placeholder,
                    stringaTipoAccount
                )
            }

            "venditore" -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_venditore)
                binding.registrazioneTipoAccount.text = getString(
                    R.string.placeholder,
                    stringaTipoAccount
                )
            }
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.registrazioneCampoPassword.setStartIconOnClickListener { clickInfoPassword() }
        binding.registrazionePulsanteIndietro.setOnClickListener { clickIndietro() }
        binding.registrazionePulsanteAvanti.setOnClickListener { clickAvanti() }

        binding.registrazionePulsanteFacebook.permissions = listOf(
            "openid", "email", "public_profile", "user_birthday",
            "user_gender", "user_link", "user_location"
        )
        binding.registrazionePulsanteFacebook.authType = "rerequest"
        binding.registrazionePulsanteFacebook.registerCallback(
            facebookCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Snackbar.make(
                        fragmentView,
                        R.string.accesso_loginSocialOK,
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(resources.getColor(R.color.arancione, null))
                        .setTextColor(resources.getColor(R.color.grigio, null))
                        .show()

                    val returned: Long? = eseguiChiamataREST(
                        "accountFacebook",
                        result.accessToken.userId, binding.registrazioneTipoAccount.text.toString()
                    )

                    if (returned == null) { // errore di comunicazione con il backend
                        Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.arancione, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()

                        LoginManager.getInstance().logOut()
                    } else if (returned == 0L) { // non esiste un account associato a questo account Facebook con questo tipo, registrati
                        queryFacebookGraph(result.accessToken)

                        try {
                            scegliAssociaCreaProfilo()
                        } catch (eccezione: EccezioneAPI) {
                            Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(resources.getColor(R.color.arancione, null))
                                .setTextColor(resources.getColor(R.color.grigio, null))
                                .show()

                            viewModel.clear()
                            LoginManager.getInstance().logOut()
                        }
                    } else { // esiste un account associato a questo account Facebook con questo tipo
                        Snackbar.make(
                            fragmentView,
                            R.string.registrazione_accountFacebookGiàCollegato,
                            Snackbar.LENGTH_SHORT
                        )
                            .setBackgroundTint(resources.getColor(R.color.arancione, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()

                        LoginManager.getInstance().logOut()
                    }
                }

                override fun onCancel() {
                    // Non fare nulla
                }

                override fun onError(error: FacebookException) {
                    Snackbar.make(
                        fragmentView,
                        R.string.registrazione_erroreRegistrazioneSocial,
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(resources.getColor(R.color.arancione, null))
                        .setTextColor(resources.getColor(R.color.grigio, null))
                        .show()
                }
            })
    }

    @UIBuilder
    override fun impostaEventiDiCambiamentoCampi() {
        binding.registrazioneEmail.addTextChangedListener {
            rimuoviErroreCampo(binding.registrazioneCampoEmail)
            rimuoviErroreCampo(binding.registrazioneCampoPassword)
        }

        binding.registrazionePassword.addTextChangedListener {
            rimuoviErroreCampo(binding.registrazioneCampoEmail)
            rimuoviErroreCampo(binding.registrazioneCampoPassword)
        }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity).get(ModelRegistrazione::class)
    }

    @EventHandler
    private fun clickInfoPassword() {
        MaterialAlertDialogBuilder(fragmentContext, R.style.Dialog)
            .setTitle(R.string.infoPassword_titolo)
            .setIcon(R.drawable.icona_info_arancione)
            .setMessage(R.string.infoPassword_messaggioCorpo)
            .setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
            .show()
    }

    @EventHandler
    private fun clickIndietro() {
        listenerBackButton?.onFragmentBackButton()
    }

    @EventHandler
    private fun clickAvanti() {
        viewModel.clear()
        viewModel.email.value = estraiTestoDaElemento(binding.registrazioneEmail)
        viewModel.password.value = estraiTestoDaElemento(binding.registrazionePassword)
        viewModel.tipoAccount.value = estraiTestoDaElemento(binding.registrazioneTipoAccount)

        try {
            viewModel.validateAccount()

            scegliAssociaCreaProfilo()
        } catch (eccezione: EccezioneCampiNonCompilati) {
            erroreCampo(
                R.string.registrazione_erroreCampiObbligatoriNonCompilati,
                binding.registrazioneCampoEmail,
                binding.registrazioneCampoPassword
            )
        } catch (eccezione: EccezioneEmailNonValida) {
            erroreCampo(R.string.registrazione_erroreFormatoEmail, binding.registrazioneCampoEmail)
        } catch (eccezione: EccezioneEmailUsata) {
            erroreCampo(R.string.registrazione_erroreEmailGiàUsata, binding.registrazioneCampoEmail)
        } catch (eccezione: EccezionePasswordNonSicura) {
            erroreCampo(
                R.string.registrazione_errorePasswordNonSicura,
                binding.registrazioneCampoPassword
            )
        } catch (eccezione: EccezioneAPI) {
            Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.arancione, null))
                .setTextColor(resources.getColor(R.color.grigio, null))
                .show()
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
                    viewModel.dataNascita.postValue(
                        LocalDate.parse(
                            obj?.optString("birthday"),
                            DateTimeFormatter.ofPattern("MM/dd/yyyy")
                        )
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
            "email,name,middle_name,last_name,birthday,about,location,gender"
        )
        request.parameters = parameters
        request.executeAsync()
    }
}

package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.RegistrazioneBinding
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.data.Account
import com.iasdietideals24.dietideals24.utilities.dto.AccountDto
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneEmailNonValida
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneEmailUsata
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezionePasswordNonSicura
import com.iasdietideals24.dietideals24.utilities.kscripts.OnBackButton
import com.iasdietideals24.dietideals24.utilities.kscripts.OnChangeActivity
import com.iasdietideals24.dietideals24.utilities.kscripts.OnNextStep
import com.iasdietideals24.dietideals24.utilities.kscripts.OnSkipStep
import com.iasdietideals24.dietideals24.utilities.repositories.CompratoreRepository
import com.iasdietideals24.dietideals24.utilities.repositories.VenditoreRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import com.iasdietideals24.dietideals24.utilities.tools.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.text.RandomStringGenerator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ControllerRegistrazione : Controller<RegistrazioneBinding>() {

    // ViewModel
    private val viewModel: ModelRegistrazione by activityViewModel()

    // Repositories
    private val compratoreRepository: CompratoreRepository by inject()
    private val venditoreRepository: VenditoreRepository by inject()

    // Listeners
    private val facebookCallbackManager: CallbackManager by inject()
    private var listenerBackButton: OnBackButton? = null
    private var listenerChangeActivity: OnChangeActivity? = null
    private var listenerNextStep: OnNextStep? = null
    private var listenerSkipStep: OnSkipStep? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnBackButton) {
            listenerBackButton = requireContext() as OnBackButton
        }
        if (requireContext() is OnChangeActivity) {
            listenerChangeActivity = requireContext() as OnChangeActivity
        }
        if (requireContext() is OnNextStep) {
            listenerNextStep = requireContext() as OnNextStep
        }
        if (requireContext() is OnSkipStep) {
            listenerSkipStep = requireContext() as OnSkipStep
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerBackButton = null
        listenerChangeActivity = null
        listenerNextStep = null
        listenerSkipStep = null
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_compratore)
                binding.registrazioneTipoAccount.text = getString(
                    R.string.placeholder,
                    stringaTipoAccount
                )
            }

            TipoAccount.VENDITORE -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_venditore)
                binding.registrazioneTipoAccount.text = getString(
                    R.string.placeholder,
                    stringaTipoAccount
                )
            }

            else -> {
                // Non fare nulla
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
            facebookSignupCallback()
        )
    }

    private fun facebookSignupCallback(): FacebookCallback<LoginResult> {
        return object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Snackbar.make(
                    fragmentView,
                    R.string.accesso_loginSocialOK,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.arancione, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                lifecycleScope.launch {
                    try {
                        val returned: Account = withContext(Dispatchers.IO) {
                            accountFacebook(result.accessToken.userId).toAccount()
                        }

                        when (returned.email) {
                            "" -> { // non esiste un account associato a questo account Facebook con questo tipo, registrati
                                queryFacebookGraph(result.accessToken)

                                scegliAssociaCreaProfilo()

                                Logger.log("Facebook sign-up successful")
                            }

                            else -> { // esiste un account associato a questo account Facebook con questo tipo
                                Snackbar.make(
                                    fragmentView,
                                    R.string.registrazione_accountFacebookGiàCollegato,
                                    Snackbar.LENGTH_SHORT
                                )
                                    .setBackgroundTint(
                                        resources.getColor(
                                            R.color.arancione,
                                            null
                                        )
                                    )
                                    .setTextColor(resources.getColor(R.color.grigio, null))
                                    .show()

                                LoginManager.getInstance().logOut()
                            }
                        }
                    } catch (_: Exception) {
                        Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.blu, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()

                        viewModel.clear()
                        LoginManager.getInstance().logOut()
                    }
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
        }
    }

    private suspend fun accountFacebook(facebookId: String): AccountDto {
        return if (CurrentUser.tipoAccount == TipoAccount.COMPRATORE) {
            compratoreRepository.accountFacebookCompratore(facebookId)
        } else {
            venditoreRepository.accountFacebookVenditore(facebookId)
        }
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
        listenerBackButton?.onBackButton()
    }

    @EventHandler
    private fun clickAvanti() {
        viewModel.clear()
        viewModel.email.value = estraiTestoDaElemento(binding.registrazioneEmail)
        viewModel.password.value = estraiTestoDaElemento(binding.registrazionePassword)
        viewModel.tipoAccount.value = CurrentUser.tipoAccount

        lifecycleScope.launch {
            try {
                viewModel.validateAccount()

                scegliAssociaCreaProfilo()
            } catch (_: EccezioneCampiNonCompilati) {
                erroreCampo(
                    R.string.registrazione_erroreCampiObbligatoriNonCompilati,
                    binding.registrazioneCampoEmail,
                    binding.registrazioneCampoPassword
                )
            } catch (_: EccezioneEmailNonValida) {
                erroreCampo(
                    R.string.registrazione_erroreFormatoEmail,
                    binding.registrazioneCampoEmail
                )
            } catch (_: EccezioneEmailUsata) {
                erroreCampo(
                    R.string.registrazione_erroreEmailGiàUsata,
                    binding.registrazioneCampoEmail
                )
            } catch (_: EccezionePasswordNonSicura) {
                erroreCampo(
                    R.string.registrazione_errorePasswordNonSicura,
                    binding.registrazioneCampoPassword
                )
            } catch (_: Exception) {
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.arancione, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    private suspend fun scegliAssociaCreaProfilo() {
        val returned = withContext(Dispatchers.IO) { associaCreaProfilo().toAccount() }

        Logger.log("Sign-up successful")

        when (returned.email) {
            // Un account di altro tipo con la stessa email non è stato trovato
            "" -> listenerNextStep?.onNextStep(this::class)

            // Un account di altro tipo con la stessa email è stato trovato
            else -> listenerSkipStep?.onSkipStep(this::class)
        }
    }

    private suspend fun associaCreaProfilo(): AccountDto {
        return if (CurrentUser.tipoAccount == TipoAccount.COMPRATORE) {
            compratoreRepository.associaCreaProfiloCompratore(viewModel.email.value!!)
        } else {
            venditoreRepository.associaCreaProfiloVenditore(viewModel.email.value!!)
        }
    }

    private fun queryFacebookGraph(accessToken: AccessToken?) {
        val request: GraphRequest = GraphRequest.newMeRequest(
            accessToken
        ) { obj, _ ->
            viewModel.facebookAccountID.postValue(obj?.optString("id"))
            viewModel.email.postValue(obj?.optString("email"))
            viewModel.password.postValue(
                RandomStringGenerator.Builder().withinRange(33, 122).get().generate(16)
            )
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

        val parameters = Bundle()
        parameters.putString(
            "fields",
            "email,name,middle_name,last_name,birthday,about,location,gender"
        )
        request.parameters = parameters
        request.executeAsync()
    }
}

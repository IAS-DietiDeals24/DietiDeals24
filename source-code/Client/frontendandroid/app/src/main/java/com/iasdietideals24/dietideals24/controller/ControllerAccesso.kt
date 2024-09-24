package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.core.widget.addTextChangedListener
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.Home
import com.iasdietideals24.dietideals24.databinding.AccessoBinding
import com.iasdietideals24.dietideals24.model.ModelAccesso
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.APIController
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.Logger
import com.iasdietideals24.dietideals24.utilities.classes.TipoAccount
import com.iasdietideals24.dietideals24.utilities.classes.data.Account
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneAccountNonEsistente
import com.iasdietideals24.dietideals24.utilities.interfaces.OnBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnChangeActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ControllerAccesso : Controller<AccessoBinding>() {

    private var viewModel: ModelAccesso = ModelAccesso()

    private var facebookCallbackManager = create()

    private var listenerBackButton: OnBackButton? = null
    private var listenerChangeActivity: OnChangeActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnBackButton) {
            listenerBackButton = requireContext() as OnBackButton
        }
        if (requireContext() is OnChangeActivity) {
            listenerChangeActivity = requireContext() as OnChangeActivity
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerBackButton = null
        listenerChangeActivity = null
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_compratore)
                binding.accessoTipoAccount.text = getString(
                    R.string.placeholder,
                    stringaTipoAccount
                )
            }

            TipoAccount.VENDITORE -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_venditore)
                binding.accessoTipoAccount.text = getString(
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
        binding.accessoPulsanteIndietro.setOnClickListener { clickIndietro() }
        binding.accessoPulsanteAccedi.setOnClickListener { clickAccedi() }

        binding.accessoPulsanteFacebook.permissions = listOf(
            "email", "public_profile", "user_birthday",
            "user_gender", "user_link", "user_location"
        )
        binding.accessoPulsanteFacebook.authType = "rerequest"
        binding.accessoPulsanteFacebook.registerCallback(
            facebookCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Snackbar.make(
                        fragmentView,
                        R.string.accesso_loginSocialOK,
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(resources.getColor(R.color.blu, null))
                        .setTextColor(resources.getColor(R.color.grigio, null))
                        .show()

                    val returned: Account = accountFacebook(result)

                    when (returned.facebookId) {
                        // non esiste un account associato a questo account Facebook con questo tipo
                        "" -> Snackbar.make(
                            fragmentView,
                            R.string.accesso_noAccountFacebookCollegato,
                            Snackbar.LENGTH_SHORT
                        )
                            .setBackgroundTint(resources.getColor(R.color.blu, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()

                        // esiste un account associato a questo account Facebook con questo tipo, accedi
                        else -> {
                            Logger.log("Facebook sign-in successful")

                            listenerChangeActivity?.onChangeActivity(Home::class.java)
                        }
                    }
                }

                override fun onCancel() {
                    // Non fare nulla
                }

                override fun onError(error: FacebookException) {
                    Snackbar.make(
                        fragmentView,
                        R.string.accesso_erroreSocial,
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(resources.getColor(R.color.blu, null))
                        .setTextColor(resources.getColor(R.color.grigio, null))
                        .show()
                }
            })
    }

    private fun accountFacebook(result: LoginResult): Account {
        return if (CurrentUser.tipoAccount == TipoAccount.COMPRATORE) {
            val call = APIController.instance.accountFacebookCompratore(result.accessToken.userId)
            chiamaAPI(call).toAccount()
        } else {
            val call = APIController.instance.accountFacebookVenditore(result.accessToken.userId)
            chiamaAPI(call).toAccount()
        }
    }

    @UIBuilder
    override fun impostaEventiDiCambiamentoCampi() {
        binding.accessoEmail.addTextChangedListener {
            rimuoviErroreCampo(binding.accessoCampoEmail)
            rimuoviErroreCampo(binding.accessoCampoPassword)
        }

        binding.accessoPassword.addTextChangedListener {
            rimuoviErroreCampo(binding.accessoCampoEmail)
            rimuoviErroreCampo(binding.accessoCampoPassword)
        }
    }

    @EventHandler
    private fun clickIndietro() {
        listenerBackButton?.onBackButton()
    }

    @OptIn(DelicateCoroutinesApi::class)
    @EventHandler
    private fun clickAccedi() {
        viewModel.email.value = estraiTestoDaElemento(binding.accessoEmail)
        viewModel.password.value = estraiTestoDaElemento(binding.accessoPassword)
        viewModel.tipoAccount.value = CurrentUser.tipoAccount

        try {
            viewModel.validate()

            val returned: Account = accedi()

            when (returned.email) {
                "" -> throw EccezioneAccountNonEsistente("Account non esistente.")

                else -> {
                    GlobalScope.launch {
                        salvaPreferenzaStringa("email", viewModel.email.value!!)
                        salvaPreferenzaStringa("password", viewModel.password.value!!)
                    }

                    Logger.log("Sign-in successful")

                    CurrentUser.id = returned.email
                    listenerChangeActivity?.onChangeActivity(Home::class.java)
                }
            }
        } catch (_: EccezioneAccountNonEsistente) {
            erroreCampo(
                R.string.accesso_erroreCredenzialiNonCorrette,
                binding.accessoCampoEmail,
                binding.accessoCampoPassword
            )
        }
    }

    private fun accedi(): Account {
        return if (CurrentUser.tipoAccount == TipoAccount.COMPRATORE) {
            val call = APIController.instance.accediCompratore(
                viewModel.email.value!!,
                viewModel.password.value!!
            )
            chiamaAPI(call).toAccount()
        } else {
            val call = APIController.instance.accediVenditore(
                viewModel.email.value!!,
                viewModel.password.value!!
            )
            chiamaAPI(call).toAccount()
        }

    }
}
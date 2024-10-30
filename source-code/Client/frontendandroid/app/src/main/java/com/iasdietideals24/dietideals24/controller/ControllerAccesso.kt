package com.iasdietideals24.dietideals24.controller

import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.amplifyframework.core.Amplify
import com.facebook.CallbackManager
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
import com.iasdietideals24.dietideals24.utilities.data.Account
import com.iasdietideals24.dietideals24.utilities.dto.AccountDto
import com.iasdietideals24.dietideals24.utilities.dto.CompratoreDto
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneAccountNonEsistente
import com.iasdietideals24.dietideals24.utilities.kscripts.OnBackButton
import com.iasdietideals24.dietideals24.utilities.kscripts.OnChangeActivity
import com.iasdietideals24.dietideals24.utilities.repositories.CompratoreRepository
import com.iasdietideals24.dietideals24.utilities.repositories.VenditoreRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import com.iasdietideals24.dietideals24.utilities.tools.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ControllerAccesso : Controller<AccessoBinding>() {

    // ViewModel
    private val viewModel: ModelAccesso by activityViewModel()

    // Repositories
    private val compratoreRepository: CompratoreRepository by inject()
    private val venditoreRepository: VenditoreRepository by inject()

    // Listeners
    private val facebookCallbackManager: CallbackManager by inject()
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
            facebookLoginCallback()
        )
    }

    private fun facebookLoginCallback(): FacebookCallback<LoginResult> {
        return object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Snackbar.make(
                    fragmentView,
                    R.string.accesso_loginSocialOK,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                lifecycleScope.launch {
                    try {
                        val returned: Account =
                            withContext(Dispatchers.IO) { accountFacebook(result).toAccount() }

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

                                CurrentUser.id = returned.email

                                accessoAccountManager()

                                listenerChangeActivity?.onChangeActivity(Home::class.java)
                            }
                        }
                    } catch (_: Exception) {
                        Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                            .setBackgroundTint(resources.getColor(R.color.blu, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()
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
        }
    }

    private suspend fun accountFacebook(result: LoginResult): AccountDto {
        return when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> compratoreRepository.accountFacebookCompratore(result.accessToken.userId)

            TipoAccount.VENDITORE -> venditoreRepository.accountFacebookVenditore(result.accessToken.userId)

            else -> CompratoreDto()
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

    @EventHandler
    private fun clickAccedi() {
        lifecycleScope.launch {
            viewModel.email.value = estraiTestoDaElemento(binding.accessoEmail)
            viewModel.password.value = estraiTestoDaElemento(binding.accessoPassword)
            viewModel.tipoAccount.value = CurrentUser.tipoAccount

            try {
                Amplify.Auth.signIn(viewModel.email.value!!, viewModel.password.value!!, {}, {})

                viewModel.validate()

                val returned: Account = withContext(Dispatchers.IO) { accedi().toAccount() }

                when (returned.email) {
                    "" -> throw EccezioneAccountNonEsistente("Account non esistente.")

                    else -> {
                        Logger.log("Sign-in successful")

                        CurrentUser.id = returned.email

                        accessoAccountManager()

                        listenerChangeActivity?.onChangeActivity(Home::class.java)
                    }
                }
            } catch (_: EccezioneAccountNonEsistente) {
                erroreCampo(
                    R.string.accesso_erroreCredenzialiNonCorrette,
                    binding.accessoCampoEmail,
                    binding.accessoCampoPassword
                )
            } catch (_: Exception) {
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    private suspend fun accedi(): AccountDto {
        return when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> compratoreRepository.accediCompratore(
                viewModel.email.value!!,
                viewModel.password.value!!
            )

            TipoAccount.VENDITORE -> venditoreRepository.accediVenditore(
                viewModel.email.value!!,
                viewModel.password.value!!
            )

            else -> CompratoreDto()
        }
    }

    private fun accessoAccountManager() {
        val accountManager = AccountManager.get(context)

        val accountData = Bundle()
        accountData.putString("TipoAccount", viewModel.tipoAccount.value.toString())

        val account = android.accounts.Account(
            viewModel.email.value,
            "com.iasdietideals24.dietideals24.account"
        )

        accountManager.addAccountExplicitly(account, viewModel.password.value, accountData)
    }
}
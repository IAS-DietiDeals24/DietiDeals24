package com.iasdietideals24.dietideals24.controller

import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.Home
import com.iasdietideals24.dietideals24.databinding.Creazioneprofilofase3Binding
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.data.Account
import com.iasdietideals24.dietideals24.utilities.dto.exceptional.PutProfiloDto
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
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

class ControllerCreazioneProfiloFase3 : Controller<Creazioneprofilofase3Binding>() {

    // ViewModel
    private val viewModel: ModelRegistrazione by activityViewModel()

    // Services
    private val compratoreRepository: CompratoreRepository by inject()
    private val venditoreRepository: VenditoreRepository by inject()

    // Listeners
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
    override fun impostaEventiClick() {
        binding.creazioneProfiloFase3PulsanteIndietro.setOnClickListener { clickIndietro() }
        binding.creazioneProfiloFase3PulsanteFine.setOnClickListener { clickFine() }
    }

    @UIBuilder
    override fun impostaOsservatori() {
        val linkPersonaleObserver = Observer<String> { newLinkPersonale ->
            binding.creazioneProfiloFase3LinkPersonale.setText(newLinkPersonale)
        }
        viewModel.linkPersonale.observe(viewLifecycleOwner, linkPersonaleObserver)

        val linkInstagramObserver = Observer<String> { newLinkInstagram ->
            binding.creazioneProfiloFase3LinkInstagram.setText(newLinkInstagram)
        }
        viewModel.linkInstagram.observe(viewLifecycleOwner, linkInstagramObserver)

        val linkFacebookObserver = Observer<String> { newLinkFacebook ->
            binding.creazioneProfiloFase3LinkFacebook.setText(newLinkFacebook)
        }
        viewModel.linkFacebook.observe(viewLifecycleOwner, linkFacebookObserver)

        val linkGitHubObserver = Observer<String> { newLinkGitHub ->
            binding.creazioneProfiloFase3LinkGitHub.setText(newLinkGitHub)
        }
        viewModel.linkGitHub.observe(viewLifecycleOwner, linkGitHubObserver)

        val linkXObserver = Observer<String> { newLinkX ->
            binding.creazioneProfiloFase3LinkX.setText(newLinkX)
        }
        viewModel.linkX.observe(viewLifecycleOwner, linkXObserver)
    }

    @EventHandler
    private fun clickIndietro() {
        listenerBackButton?.onBackButton()
    }

    @EventHandler
    private fun clickFine() {
        lifecycleScope.launch {
            try {
                Amplify.Auth.signUp(
                    viewModel.email.value!!,
                    viewModel.password.value!!,
                    AuthSignUpOptions.builder()
                        .userAttribute(
                            AuthUserAttributeKey.birthdate(),
                            viewModel.dataNascita.value.toString()
                        )
                        .userAttribute(
                            AuthUserAttributeKey.preferredUsername(),
                            viewModel.nomeUtente.value!!
                        )
                        .userAttribute(AuthUserAttributeKey.email(), viewModel.email.value!!)
                        .userAttribute(AuthUserAttributeKey.givenName(), viewModel.nome.value!!)
                        .userAttribute(AuthUserAttributeKey.familyName(), viewModel.cognome.value!!)
                        .build(),
                    {},
                    {}
                )

                val account: Account =
                    withContext(Dispatchers.IO) { creazioneAccount().toAccount() }

                if (account.email != "") {
                    Logger.log("Profile creation successful")

                    CurrentUser.id = account.email

                    registrazioneAccountManager()

                    listenerChangeActivity?.onChangeActivity(Home::class.java)
                }
            } catch (_: Exception) {
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    private suspend fun creazioneAccount(): PutProfiloDto {
        return if (CurrentUser.tipoAccount == TipoAccount.COMPRATORE) {
            compratoreRepository.creazioneAccountCompratore(
                viewModel.email.value!!,
                viewModel.toAccountCompratore()
            )
        } else {
            venditoreRepository.creazioneAccountVenditore(
                viewModel.email.value!!,
                viewModel.toAccountVenditore()
            )
        }
    }

    private fun registrazioneAccountManager() {
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
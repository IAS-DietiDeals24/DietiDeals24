package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.lifecycle.lifecycleScope
import com.facebook.AccessToken
import com.facebook.LoginStatusCallback
import com.facebook.login.LoginManager.Companion.getInstance
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.Home
import com.iasdietideals24.dietideals24.databinding.SelezionetipoaccountBinding
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.data.Account
import com.iasdietideals24.dietideals24.utilities.dto.AccountDto
import com.iasdietideals24.dietideals24.utilities.dto.CompratoreDto
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.kscripts.CurrentUser
import com.iasdietideals24.dietideals24.utilities.kscripts.Logger
import com.iasdietideals24.dietideals24.utilities.kscripts.OnChangeActivity
import com.iasdietideals24.dietideals24.utilities.kscripts.OnHideBackButton
import com.iasdietideals24.dietideals24.utilities.kscripts.OnNextStep
import com.iasdietideals24.dietideals24.utilities.kscripts.OnShowBackButton
import com.iasdietideals24.dietideals24.utilities.repositories.CompratoreRepository
import com.iasdietideals24.dietideals24.utilities.repositories.VenditoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class ControllerSelezioneTipoAccount : Controller<SelezionetipoaccountBinding>() {

    // Repositories
    private val compratoreRepository: CompratoreRepository by inject()
    private val venditoreRepository: VenditoreRepository by inject()

    // Listeners
    private var changeActivityListener: OnChangeActivity? = null
    private var hideBackButtonListener: OnHideBackButton? = null
    private var showBackButtonListener: OnShowBackButton? = null
    private var nextStepListener: OnNextStep? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnChangeActivity) {
            changeActivityListener = requireContext() as OnChangeActivity
        }
        if (requireContext() is OnHideBackButton) {
            hideBackButtonListener = requireContext() as OnHideBackButton
        }
        if (requireContext() is OnShowBackButton) {
            showBackButtonListener = requireContext() as OnShowBackButton
        }
        if (requireContext() is OnNextStep) {
            nextStepListener = requireContext() as OnNextStep
        }
    }

    override fun onDetach() {
        super.onDetach()

        changeActivityListener = null
        hideBackButtonListener = null
        showBackButtonListener = null
        nextStepListener = null
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        hideBackButtonListener?.onHideBackButton()

        // Controlla l'accesso automatico con Facebook
        getInstance()
            .retrieveLoginStatus(
                fragmentContext,
                object : LoginStatusCallback {
                    override fun onCompleted(accessToken: AccessToken) {
                        // Non fare nulla
                    }

                    override fun onFailure() {
                        // Non fare nulla
                    }

                    override fun onError(exception: Exception) {
                        Snackbar.make(
                            fragmentView,
                            R.string.selezioneTipoAccount_erroreFacebook,
                            Snackbar.LENGTH_SHORT
                        )
                            .setBackgroundTint(resources.getColor(R.color.blu, null))
                            .setTextColor(resources.getColor(R.color.grigio, null))
                            .show()
                    }
                })

        var email: String?
        var password: String?
        var tipoAccount: String?

        runBlocking { email = caricaPreferenzaStringa("email") }
        runBlocking { password = caricaPreferenzaStringa("password") }
        runBlocking { tipoAccount = caricaPreferenzaStringa("tipoAccount") }

        CurrentUser.tipoAccount = when (tipoAccount) {
            "COMPRATORE" -> TipoAccount.COMPRATORE
            "VENDITORE" -> TipoAccount.VENDITORE
            else -> TipoAccount.OSPITE
        }

        if (email != "" && password != "" && tipoAccount != "") {
            lifecycleScope.launch {
                try {
                    val returned: Account =
                        withContext(Dispatchers.IO) { accedi(email!!, password!!).toAccount() }

                    if (returned.email != "") {
                        CurrentUser.id = returned.email
                        changeActivityListener?.onChangeActivity(Home::class.java)
                    }
                } catch (e: Exception) {
                    Snackbar.make(
                        fragmentView,
                        R.string.apiError,
                        Snackbar.LENGTH_SHORT
                    )
                        .setBackgroundTint(resources.getColor(R.color.blu, null))
                        .setTextColor(resources.getColor(R.color.grigio, null))
                        .show()
                }
            }
        }
    }

    private suspend fun accedi(email: String, password: String): AccountDto {
        return when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> compratoreRepository.accediCompratore(email, password)

            TipoAccount.VENDITORE -> venditoreRepository.accediVenditore(email, password)

            else -> CompratoreDto()
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.selezioneTipoAccountPulsanteCompratore.setOnClickListener {
            clickCompratore()
        }

        binding.selezioneTipoAccountPulsanteVenditore.setOnClickListener {
            clickVenditore()
        }

        binding.selezioneTipoAccountPulsanteOspite.setOnClickListener {
            clickOspite()
        }
    }

    @EventHandler
    private fun clickCompratore() {
        Logger.log("Buyer account selected")

        showBackButtonListener?.onShowBackButton()
        runBlocking { salvaPreferenzaStringa("tipoAccount", "COMPRATORE") }
        CurrentUser.tipoAccount = TipoAccount.COMPRATORE
        nextStepListener?.onNextStep(this::class)
    }

    @EventHandler
    private fun clickVenditore() {
        Logger.log("Seller account selected")

        showBackButtonListener?.onShowBackButton()
        runBlocking { salvaPreferenzaStringa("tipoAccount", "VENDITORE") }
        CurrentUser.tipoAccount = TipoAccount.VENDITORE
        nextStepListener?.onNextStep(this::class)
    }

    @EventHandler
    private fun clickOspite() {
        Logger.log("Guest selected")

        CurrentUser.tipoAccount = TipoAccount.OSPITE
        changeActivityListener?.onChangeActivity(Home::class.java)
    }
}
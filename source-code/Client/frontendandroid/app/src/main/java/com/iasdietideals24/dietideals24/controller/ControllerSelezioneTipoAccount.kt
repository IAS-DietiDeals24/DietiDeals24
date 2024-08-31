package com.iasdietideals24.dietideals24.controller

import android.content.Context
import com.facebook.AccessToken
import com.facebook.LoginStatusCallback
import com.facebook.login.LoginManager.Companion.getInstance
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.Home
import com.iasdietideals24.dietideals24.databinding.SelezionetipoaccountBinding
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.interfaces.OnChangeActivity
import com.iasdietideals24.dietideals24.utilities.interfaces.OnHideBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnNextStep
import com.iasdietideals24.dietideals24.utilities.interfaces.OnShowBackButton
import kotlinx.coroutines.runBlocking

class ControllerSelezioneTipoAccount : Controller<SelezionetipoaccountBinding>() {

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

        if (email != "" && password != "" && tipoAccount != "") {
            val returned: Long? = eseguiChiamataREST("accedi", email, password, tipoAccount)

            if (returned != null && returned != 0L) {
                CurrentUser.id = returned
                changeActivityListener?.onChangeActivity(Home::class.java)
            }
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
        showBackButtonListener?.onShowBackButton()
        runBlocking { salvaPreferenzaStringa("tipoAccount", "compratore") }
        nextStepListener?.onNextStep(this::class)
    }

    @EventHandler
    private fun clickVenditore() {
        showBackButtonListener?.onShowBackButton()
        runBlocking { salvaPreferenzaStringa("tipoAccount", "venditore") }
        nextStepListener?.onNextStep(this::class)
    }

    @EventHandler
    private fun clickOspite() {
        changeActivityListener?.onChangeActivity(Home::class.java)
    }
}
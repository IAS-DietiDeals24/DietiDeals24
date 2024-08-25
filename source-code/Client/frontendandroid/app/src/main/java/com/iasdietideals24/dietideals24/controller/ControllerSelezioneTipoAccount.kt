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
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeActivity
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentHideBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentNextStep
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentShowBackButton
import kotlinx.coroutines.runBlocking

class ControllerSelezioneTipoAccount : Controller<SelezionetipoaccountBinding>() {

    private var changeActivityListener: OnFragmentChangeActivity? = null
    private var hideBackButtonListener: OnFragmentHideBackButton? = null
    private var showBackButtonListener: OnFragmentShowBackButton? = null
    private var nextStepListener: OnFragmentNextStep? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnFragmentChangeActivity) {
            changeActivityListener = requireContext() as OnFragmentChangeActivity
        }
        if (requireContext() is OnFragmentHideBackButton) {
            hideBackButtonListener = requireContext() as OnFragmentHideBackButton
        }
        if (requireContext() is OnFragmentShowBackButton) {
            showBackButtonListener = requireContext() as OnFragmentShowBackButton
        }
        if (requireContext() is OnFragmentNextStep) {
            nextStepListener = requireContext() as OnFragmentNextStep
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
        hideBackButtonListener?.onFragmentHideBackButton()

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
                changeActivityListener?.onFragmentChangeActivity(Home::class.java)
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
        showBackButtonListener?.onFragmentShowBackButton()
        runBlocking { salvaPreferenzaStringa("tipoAccount", "compratore") }
        nextStepListener?.onFragmentNextStep(this::class)
    }

    @EventHandler
    private fun clickVenditore() {
        showBackButtonListener?.onFragmentShowBackButton()
        runBlocking { salvaPreferenzaStringa("tipoAccount", "venditore") }
        nextStepListener?.onFragmentNextStep(this::class)
    }

    @EventHandler
    private fun clickOspite() {
        changeActivityListener?.onFragmentChangeActivity(Home::class.java)
    }
}
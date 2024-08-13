package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.LoginStatusCallback
import com.facebook.login.LoginManager.Companion.getInstance
import com.google.android.material.button.MaterialButton
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.Home
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeActivity
import kotlinx.coroutines.runBlocking

class ControllerSelezioneTipoAccount : Controller(R.layout.selezionetipoaccount) {

    private lateinit var pulsanteCompratore: MaterialButton
    private lateinit var pulsanteVenditore: MaterialButton
    private lateinit var pulsanteOspite: MaterialButton

    private var listener: OnFragmentChangeActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnFragmentChangeActivity) {
            listener = requireContext() as OnFragmentChangeActivity
        }
    }

    override fun onDetach() {
        super.onDetach()

        listener = null
    }

    override fun elaborazioneAggiuntiva() {
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
                        Toast.makeText(
                            fragmentContext,
                            "Errore durante l'accesso con Facebook",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
    }

    @UIBuilder
    override fun trovaElementiInterfaccia() {
        pulsanteCompratore = fragmentView.findViewById(R.id.selezioneTipoAccount_pulsanteCompratore)
        pulsanteVenditore = fragmentView.findViewById(R.id.selezioneTipoAccount_pulsanteVenditore)
        pulsanteOspite = fragmentView.findViewById(R.id.selezioneTipoAccount_pulsanteOspite)
    }

    @UIBuilder
    override fun impostaEventiClick() {
        pulsanteCompratore.setOnClickListener {
            clickCompratore()
        }

        pulsanteVenditore.setOnClickListener {
            clickVenditore()
        }

        pulsanteOspite.setOnClickListener {
            clickOspite()
        }
    }

    @EventHandler
    private fun clickCompratore() {
        runBlocking { salvaPreferenzaStringa("tipoAccount", "compratore") }
        navController.navigate(R.id.action_controllerSelezioneTipoAccount_to_controllerSelezioneAccessoRegistrazione)
    }

    @EventHandler
    private fun clickVenditore() {
        runBlocking { salvaPreferenzaStringa("tipoAccount", "venditore") }
        navController.navigate(R.id.action_controllerSelezioneTipoAccount_to_controllerSelezioneAccessoRegistrazione)
    }

    @EventHandler
    private fun clickOspite() {
        listener?.onFragmentChangeActivity(Home::class.java)
    }
}
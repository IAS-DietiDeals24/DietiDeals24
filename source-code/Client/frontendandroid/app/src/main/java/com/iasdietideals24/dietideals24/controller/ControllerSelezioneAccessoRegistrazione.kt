package com.iasdietideals24.dietideals24.controller

import android.content.Context
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.Accesso
import com.iasdietideals24.dietideals24.activities.Registrazione
import com.iasdietideals24.dietideals24.databinding.SelezioneaccessoregistrazioneBinding
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.Logger
import com.iasdietideals24.dietideals24.utilities.interfaces.OnChangeActivity
import kotlinx.coroutines.runBlocking

class ControllerSelezioneAccessoRegistrazione : Controller<SelezioneaccessoregistrazioneBinding>() {

    private var listener: OnChangeActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnChangeActivity) {
            listener = requireContext() as OnChangeActivity
        }
    }

    override fun onDetach() {
        super.onDetach()

        listener = null
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        when (runBlocking { caricaPreferenzaStringa("tipoAccount") }) {
            "compratore" -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_compratore)
                binding.selezioneAccessoRegistrazioneSaluto.text = getString(
                    R.string.selezioneAccessoRegistrazione_saluto,
                    stringaTipoAccount
                )
            }

            "venditore" -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_venditore)
                binding.selezioneAccessoRegistrazioneSaluto.text = getString(
                    R.string.selezioneAccessoRegistrazione_saluto,
                    stringaTipoAccount
                )
            }
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.selezioneAccessoRegistrazionePulsanteAccedi.setOnClickListener {
            clickAccedi()
        }

        binding.selezioneAccessoRegistrazionePulsanteRegistrati.setOnClickListener {
            clickRegistrati()
        }
    }

    @EventHandler
    private fun clickAccedi() {
        Logger.log("Sign-in selected")

        listener?.onChangeActivity(Accesso::class.java)
    }

    @EventHandler
    private fun clickRegistrati() {
        Logger.log("Sign-up selected")

        listener?.onChangeActivity(Registrazione::class.java)
    }
}
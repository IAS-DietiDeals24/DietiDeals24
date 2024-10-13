package com.iasdietideals24.dietideals24.controller

import android.content.Context
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.Accesso
import com.iasdietideals24.dietideals24.activities.Registrazione
import com.iasdietideals24.dietideals24.databinding.SelezioneaccessoregistrazioneBinding
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.kscripts.OnChangeActivity
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import com.iasdietideals24.dietideals24.utilities.tools.Logger

class ControllerSelezioneAccessoRegistrazione : Controller<SelezioneaccessoregistrazioneBinding>() {

    // Listeners
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
        when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_compratore)
                binding.selezioneAccessoRegistrazioneSaluto.text = getString(
                    R.string.selezioneAccessoRegistrazione_saluto,
                    stringaTipoAccount
                )
            }

            TipoAccount.VENDITORE -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_venditore)
                binding.selezioneAccessoRegistrazioneSaluto.text = getString(
                    R.string.selezioneAccessoRegistrazione_saluto,
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
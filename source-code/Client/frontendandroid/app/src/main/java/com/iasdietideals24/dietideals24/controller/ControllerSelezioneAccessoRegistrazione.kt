package com.iasdietideals24.dietideals24.controller

import android.content.Context
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.Accesso
import com.iasdietideals24.dietideals24.activities.Registrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeActivity
import kotlinx.coroutines.runBlocking

class ControllerSelezioneAccessoRegistrazione : Controller(R.layout.selezioneaccessoregistrazione) {

    private lateinit var pulsanteAccedi: MaterialButton
    private lateinit var pulsanteRegistrati: MaterialButton
    private lateinit var saluto: MaterialTextView

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

    @UIBuilder
    override fun trovaElementiInterfaccia() {
        pulsanteAccedi =
            fragmentView.findViewById(R.id.selezioneAccessoRegistrazione_pulsanteAccedi)
        pulsanteRegistrati =
            fragmentView.findViewById(R.id.selezioneAccessoRegistrazione_pulsanteRegistrati)
        saluto = fragmentView.findViewById(R.id.selezioneAccessoRegistrazione_saluto)
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        when (runBlocking { caricaPreferenzaStringa("tipoAccount") }) {
            "compratore" -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_compratore)
                saluto.text = getString(
                    R.string.selezioneAccessoRegistrazione_saluto,
                    stringaTipoAccount
                )
            }

            "venditore" -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_venditore)
                saluto.text = getString(
                    R.string.selezioneAccessoRegistrazione_saluto,
                    stringaTipoAccount
                )
            }
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        pulsanteAccedi.setOnClickListener {
            clickAccedi()
        }

        pulsanteRegistrati.setOnClickListener {
            clickRegistrati()
        }
    }

    @EventHandler
    private fun clickAccedi() {
        listener?.onFragmentChangeActivity(Accesso::class.java)
    }

    @EventHandler
    private fun clickRegistrati() {
        listener?.onFragmentChangeActivity(Registrazione::class.java)
    }
}
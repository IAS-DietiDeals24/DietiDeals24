package com.iasdietideals24.dietideals24.scelteIniziali

import android.widget.ImageButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R

class FrameSelezioneAccessoRegistrazione(private val controller: ControllerScelteIniziali) {
    private lateinit var pulsanteAccedi: MaterialButton
    private lateinit var pulsanteRegistrati: MaterialButton
    private lateinit var saluto: MaterialTextView
    private lateinit var pulsanteIndietro: ImageButton

    init {
        controller.setContentView(R.layout.selezioneaccessoregistrazione)

        trovaElementiInterfaccia()

        impostaMessaggioCorpo()

        impostaEventiClick()
    }

    private fun trovaElementiInterfaccia() {
        pulsanteAccedi = controller.findViewById(R.id.selezioneAccessoRegistrazione_pulsanteAccedi)
        pulsanteRegistrati =
            controller.findViewById(R.id.selezioneAccessoRegistrazione_pulsanteRegistrati)
        saluto = controller.findViewById(R.id.selezioneAccessoRegistrazione_saluto)
        pulsanteIndietro =
            controller.findViewById(R.id.selezioneAccessoRegistrazione_pulsanteIndietro)
    }

    private fun impostaMessaggioCorpo() {
        when (controller.tipoAccount) {
            "compratore" -> {
                val stringaTipoAccount = controller.getString(R.string.tipoAccount_compratore)
                saluto.text = controller.getString(
                    R.string.selezioneAccessoRegistrazione_saluto,
                    stringaTipoAccount
                )
            }

            "venditore" -> {
                val stringaTipoAccount = controller.getString(R.string.tipoAccount_venditore)
                saluto.text = controller.getString(
                    R.string.selezioneAccessoRegistrazione_saluto,
                    stringaTipoAccount
                )
            }
        }
    }

    private fun impostaEventiClick() {
        pulsanteAccedi.setOnClickListener { clickAccedi() }
        pulsanteRegistrati.setOnClickListener { clickRegistrati() }
        pulsanteIndietro.setOnClickListener { clickIndietro() }
    }

    private fun clickAccedi() {
        controller.apriAccesso()
    }

    private fun clickRegistrati() {
        controller.apriRegistrazione()
    }

    private fun clickIndietro() {
        controller.mostraSelezioneTipoAccount()
    }

}

package com.iasdietideals24.dietideals24.scelteIniziali

import com.google.android.material.button.MaterialButton
import com.iasdietideals24.dietideals24.R

class FrameSelezioneTipoAccount(private val controller: ControllerScelteIniziali) {
    private lateinit var pulsanteCompratore: MaterialButton
    private lateinit var pulsanteVenditore: MaterialButton
    private lateinit var pulsanteOspite: MaterialButton

    init {
        controller.setContentView(R.layout.selezionetipoaccount)

        trovaElementiInterfaccia()

        impostaEventiClick()
    }

    private fun trovaElementiInterfaccia() {
        pulsanteCompratore = controller.findViewById(R.id.selezioneTipoAccount_pulsanteCompratore)
        pulsanteVenditore = controller.findViewById(R.id.selezioneTipoAccount_pulsanteVenditore)
        pulsanteOspite = controller.findViewById(R.id.selezioneTipoAccount_pulsanteOspite)
    }

    private fun impostaEventiClick() {
        pulsanteCompratore.setOnClickListener { clickCompratore() }
        pulsanteVenditore.setOnClickListener { clickVenditore() }
        pulsanteOspite.setOnClickListener { clickOspite() }
    }

    private fun clickCompratore() {
        controller.tipoAccount = "compratore"

        controller.mostraSelezioneAccessoRegistrazione()
    }

    private fun clickVenditore() {
        controller.tipoAccount = "venditore"

        controller.mostraSelezioneAccessoRegistrazione()
    }

    private fun clickOspite() {
        controller.apriHome()
    }
}
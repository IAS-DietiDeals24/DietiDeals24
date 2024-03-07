package com.iasdietideals24.dietideals24.scelteIniziali

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.iasdietideals24.dietideals24.accesso.ControllerAccesso
import com.iasdietideals24.dietideals24.registrazione.ControllerRegistrazione


class ControllerScelteIniziali : AppCompatActivity() {
    private var tipoAccount: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recuperaAttributiPassati()

        mostraSchermataCorretta()
    }

    private fun recuperaAttributiPassati() {
        val vecchiaAttivita = intent
        val dati = vecchiaAttivita.extras
        val datoTipoAccount = dati?.getString("tipoAccount")
        val stringaTipoAccount = datoTipoAccount.toString()
        tipoAccount = stringaTipoAccount
        dati?.remove("tipoAccount")
    }

    private fun mostraSchermataCorretta() {
        val vecchiaAttivita = intent
        val dati = vecchiaAttivita.extras
        val datoAttivitaChiamante = dati?.getString("attivitaChiamante")
        val stringaAttivitaChiamante = datoAttivitaChiamante.toString()
        dati?.remove("attivitaChiamante")

        if (stringaAttivitaChiamante == "ControllerAccesso" ||
            stringaAttivitaChiamante == "ControllerRegistrazione"
        )
            mostraSelezioneAccessoRegistrazione()
        else
            mostraSelezioneTipoAccount()
    }

    fun mostraSelezioneTipoAccount() {
        FrameSelezioneTipoAccount(this)
    }

    fun mostraSelezioneAccessoRegistrazione() {
        setTipoAccount(tipoAccount)

        FrameSelezioneAccessoRegistrazione(this)
    }

    fun setTipoAccount(tipoAccount: String) {
        this.tipoAccount = tipoAccount
    }

    fun getTipoAccount(): String {
        return tipoAccount
    }


    fun apriAccesso() {
        cambiaAttivita(ControllerAccesso::class.java)
    }

    private fun cambiaAttivita(controllerAttivita: Class<*>) {
        val nuovaAttivita = Intent(this, controllerAttivita)
        nuovaAttivita.putExtra("tipoAccount", tipoAccount)
        startActivity(nuovaAttivita)
    }

    fun apriRegistrazione() {
        cambiaAttivita(ControllerRegistrazione::class.java)
    }

    fun apriHome() {
        TODO("Not yet implemented")
    }
}
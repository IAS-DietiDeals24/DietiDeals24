package com.iasdietideals24.dietideals24.scelteIniziali

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.iasdietideals24.dietideals24.accesso.ControllerAccesso
import com.iasdietideals24.dietideals24.registrazione.ControllerRegistrazione


class ControllerScelteIniziali : AppCompatActivity() {
    private lateinit var _tipoAccount: String

    var tipoAccount: String
        get() = _tipoAccount
        set(valore) {
            _tipoAccount = valore
        }

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

    fun mostraSelezioneAccessoRegistrazione() {
        FrameSelezioneAccessoRegistrazione(this)
    }

    fun mostraSelezioneTipoAccount() {
        FrameSelezioneTipoAccount(this)
    }

    fun apriAccesso() {
        val nuovaAttivita = Intent(this, ControllerAccesso::class.java)
        nuovaAttivita.putExtra("tipoAccount", tipoAccount)
        startActivity(nuovaAttivita)
    }

    fun apriRegistrazione() {
        val nuovaAttivita = Intent(this, ControllerRegistrazione::class.java)
        nuovaAttivita.putExtra("tipoAccount", tipoAccount)
        startActivity(nuovaAttivita)
    }

    fun apriHome() {
        TODO()
    }
}
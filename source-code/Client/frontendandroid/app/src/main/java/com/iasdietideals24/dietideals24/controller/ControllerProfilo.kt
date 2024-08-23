package com.iasdietideals24.dietideals24.controller

import androidx.core.content.res.ResourcesCompat
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.data.DatiAnteprimaProfilo

class ControllerProfilo : Controller(R.layout.profilo) {

    private lateinit var nome: MaterialTextView
    private lateinit var tipoAccount: MaterialTextView
    private lateinit var immagine: ShapeableImageView
    private lateinit var pulsanteInformazioniUtente: MaterialButton
    private lateinit var pulsanteAsteCreate: MaterialButton
    private lateinit var pulsanteStoricoPartecipazioni: MaterialButton
    private lateinit var pulsanteAiuto: MaterialButton
    private lateinit var pulsanteEsci: MaterialButton

    @UIBuilder
    override fun trovaElementiInterfaccia() {
        nome = fragmentView.findViewById(R.id.profilo_nome)
        tipoAccount = fragmentView.findViewById(R.id.profilo_tipoAccount)
        immagine = fragmentView.findViewById(R.id.profilo_immagine)
        pulsanteInformazioniUtente = fragmentView.findViewById(R.id.profilo_pulsanteUtente)
        pulsanteAsteCreate = fragmentView.findViewById(R.id.profilo_pulsanteAste)
        pulsanteStoricoPartecipazioni = fragmentView.findViewById(R.id.profilo_pulsanteStorico)
        pulsanteAiuto = fragmentView.findViewById(R.id.profilo_pulsanteAiuto)
        pulsanteEsci = fragmentView.findViewById(R.id.profilo_pulsanteEsci)
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        if (CurrentUser.id != 0L) {
            val result: DatiAnteprimaProfilo? =
                eseguiChiamataREST("recuperaNotifiche", CurrentUser.id)

            if (result != null) {
                when (result._tipoAccount) {
                    "compratore" -> tipoAccount.text = getString(
                        R.string.profilo_tipoAccount,
                        getString(R.string.tipoAccount_compratore)
                    )

                    "venditore" -> tipoAccount.text = getString(
                        R.string.profilo_tipoAccount,
                        getString(R.string.tipoAccount_venditore)
                    )
                }
                nome.text = getString(R.string.placeholder, result._nome)

                if (result._immagineProfilo.isNotEmpty())
                    immagine.load(result._immagineProfilo) {
                        crossfade(0)
                    }
            } else
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
        } else {
            tipoAccount.text =
                getString(R.string.profilo_tipoAccount, getString(R.string.tipoAccount_ospite))
            nome.text = getString(R.string.tipoAccount_ospite)
            pulsanteInformazioniUtente.isEnabled = false
            pulsanteInformazioniUtente.setIconTintResource(R.color.grigioScuro)
            pulsanteAsteCreate.isEnabled = false
            pulsanteAsteCreate.setIconTintResource(R.color.grigioScuro)
            pulsanteStoricoPartecipazioni.isEnabled = false
            pulsanteStoricoPartecipazioni.setIconTintResource(R.color.grigioScuro)
            pulsanteEsci.text = getString(R.string.profilo_pulsante5O)
            pulsanteEsci.icon = ResourcesCompat.getDrawable(resources, R.drawable.icona_porta, null)
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {

    }
}
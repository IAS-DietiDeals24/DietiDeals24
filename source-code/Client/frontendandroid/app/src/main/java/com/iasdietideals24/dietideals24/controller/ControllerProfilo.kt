package com.iasdietideals24.dietideals24.controller

import androidx.core.content.res.ResourcesCompat
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.ProfiloBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.data.DatiAnteprimaProfilo

class ControllerProfilo : Controller<ProfiloBinding>() {

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        if (CurrentUser.id != 0L) {
            val result: DatiAnteprimaProfilo? =
                eseguiChiamataREST("recuperaNotifiche", CurrentUser.id)

            if (result != null) {
                when (result._tipoAccount) {
                    "compratore" -> binding.profiloTipoAccount.text = getString(
                        R.string.profilo_tipoAccount,
                        getString(R.string.tipoAccount_compratore)
                    )

                    "venditore" -> binding.profiloTipoAccount.text = getString(
                        R.string.profilo_tipoAccount,
                        getString(R.string.tipoAccount_venditore)
                    )
                }
                binding.profiloNome.text = getString(R.string.placeholder, result._nome)

                if (result._immagineProfilo.isNotEmpty())
                    binding.profiloImmagine.load(result._immagineProfilo) {
                        crossfade(0)
                    }
            } else
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
        } else {
            binding.profiloTipoAccount.text =
                getString(R.string.profilo_tipoAccount, getString(R.string.tipoAccount_ospite))
            binding.profiloNome.text = getString(R.string.tipoAccount_ospite)
            binding.profiloPulsanteUtente.isEnabled = false
            binding.profiloPulsanteUtente.setIconTintResource(R.color.grigioScuro)
            binding.profiloPulsanteAste.isEnabled = false
            binding.profiloPulsanteAste.setIconTintResource(R.color.grigioScuro)
            binding.profiloPulsanteStorico.isEnabled = false
            binding.profiloPulsanteStorico.setIconTintResource(R.color.grigioScuro)
            binding.profiloPulsanteEsci.text = getString(R.string.profilo_pulsante5O)
            binding.profiloPulsanteEsci.icon =
                ResourcesCompat.getDrawable(resources, R.drawable.icona_porta, null)
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {

    }
}
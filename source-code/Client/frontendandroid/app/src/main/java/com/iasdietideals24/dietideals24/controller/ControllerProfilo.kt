package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.ScelteIniziali
import com.iasdietideals24.dietideals24.databinding.ProfiloBinding
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaProfilo
import com.iasdietideals24.dietideals24.utilities.interfaces.OnChangeActivity
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToCreatedAuctions
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToParticipation
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile

class ControllerProfilo : Controller<ProfiloBinding>() {

    private var listenerProfile: OnGoToProfile? = null
    private var listenerCreatedAuctions: OnGoToCreatedAuctions? = null
    private var listenerParticipation: OnGoToParticipation? = null
    private var listenerChangeActivity: OnChangeActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnGoToProfile) {
            listenerProfile = requireContext() as OnGoToProfile
        }
        if (requireContext() is OnGoToCreatedAuctions) {
            listenerCreatedAuctions = requireContext() as OnGoToCreatedAuctions
        }
        if (requireContext() is OnGoToParticipation) {
            listenerParticipation = requireContext() as OnGoToParticipation
        }
        if (requireContext() is OnChangeActivity) {
            listenerChangeActivity = requireContext() as OnChangeActivity
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerProfile = null
        listenerCreatedAuctions = null
        listenerParticipation = null
        listenerChangeActivity = null
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        if (CurrentUser.id != 0L) {
            val result: AnteprimaProfilo? =
                eseguiChiamataREST("recuperaNotifiche", CurrentUser.id)

            if (result != null) {
                when (result.tipoAccount) {
                    "compratore" -> binding.profiloTipoAccount.text = getString(
                        R.string.profilo_tipoAccount,
                        getString(R.string.tipoAccount_compratore)
                    )

                    "venditore" -> binding.profiloTipoAccount.text = getString(
                        R.string.profilo_tipoAccount,
                        getString(R.string.tipoAccount_venditore)
                    )
                }
                binding.profiloNome.text = getString(R.string.placeholder, result.nome)

                if (result.immagineProfilo.isNotEmpty()) {
                    binding.profiloImmagine.load(result.immagineProfilo) {
                        crossfade(true)
                    }
                    binding.profiloImmagine.scaleType = ImageView.ScaleType.CENTER_CROP
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
        binding.profiloPulsanteUtente.setOnClickListener { clickUtente() }

        binding.profiloPulsanteAste.setOnClickListener { clickAste() }

        binding.profiloPulsanteStorico.setOnClickListener { clickStorico() }

        binding.profiloPulsanteAiuto.setOnClickListener { clickAiuto() }

        binding.profiloPulsanteEsci.setOnClickListener { clickEsci() }
    }

    @EventHandler
    private fun clickUtente() {
        listenerProfile?.onGoToProfile(CurrentUser.id, ControllerProfilo::class)
    }

    @EventHandler
    private fun clickAste() {
        listenerCreatedAuctions?.onGoToCreatedAuctions()
    }

    @EventHandler
    private fun clickStorico() {
        listenerParticipation?.onGoToParticipation()
    }

    @EventHandler
    private fun clickAiuto() {
        //TODO
    }

    @EventHandler
    private fun clickEsci() {
        listenerChangeActivity?.onChangeActivity(ScelteIniziali::class.java)
    }

}
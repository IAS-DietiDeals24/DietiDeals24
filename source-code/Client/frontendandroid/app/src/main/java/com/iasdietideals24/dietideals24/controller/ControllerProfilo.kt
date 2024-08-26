package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.ScelteIniziali
import com.iasdietideals24.dietideals24.databinding.ProfiloBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaProfilo
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeActivity
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile

class ControllerProfilo : Controller<ProfiloBinding>() {

    private var profileListener: OnGoToProfile? = null
    private var startListener: OnFragmentChangeActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnGoToProfile) {
            profileListener = requireContext() as OnGoToProfile
        }
        if (requireContext() is OnFragmentChangeActivity) {
            startListener = requireContext() as OnFragmentChangeActivity
        }
    }

    override fun onDetach() {
        super.onDetach()

        profileListener = null
        startListener = null
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
        binding.profiloPulsanteUtente.setOnClickListener {
            profileListener?.onGoToProfile(CurrentUser.id, ControllerProfilo::class)
        }
        binding.profiloPulsanteAste.setOnClickListener {
            //TODO
        }
        binding.profiloPulsanteStorico.setOnClickListener {
            //TODO
        }
        binding.profiloPulsanteAiuto.setOnClickListener {
            //TODO
        }
        binding.profiloPulsanteEsci.setOnClickListener {
            startListener?.onFragmentChangeActivity(ScelteIniziali::class.java)
        }
    }
}
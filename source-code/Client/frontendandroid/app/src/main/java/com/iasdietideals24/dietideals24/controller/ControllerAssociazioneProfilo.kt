package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.Home
import com.iasdietideals24.dietideals24.databinding.AssociaprofiloBinding
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.interfaces.OnChangeActivity

class ControllerAssociazioneProfilo : Controller<AssociaprofiloBinding>() {

    private lateinit var viewModel: ModelRegistrazione

    private var listenerChangeActivity: OnChangeActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnChangeActivity) {
            listenerChangeActivity = requireContext() as OnChangeActivity
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerChangeActivity = null
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.associaProfiloPulsanteFine.setOnClickListener { clickFine() }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity)[ModelRegistrazione::class]
    }

    @EventHandler
    private fun clickFine() {
        val returned: Long? =
            eseguiChiamataREST("associazioneProfilo", viewModel.toAccount())

        if (returned == null || returned == 0L) {
            binding.associaProfiloPulsanteFine.isEnabled = false

            Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.arancione, null))
                .setTextColor(resources.getColor(R.color.grigio, null))
                .show()
        } else {
            CurrentUser.id = returned
            listenerChangeActivity?.onChangeActivity(Home::class.java)
        }
    }
}
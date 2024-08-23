package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.Home
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeActivity

class ControllerAssociazioneProfilo : Controller(R.layout.associaprofilo) {

    private lateinit var viewModel: ModelRegistrazione

    private lateinit var pulsanteFine: MaterialButton

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
        pulsanteFine = fragmentView.findViewById(R.id.associaProfilo_pulsanteFine)
    }

    @UIBuilder
    override fun impostaEventiClick() {
        pulsanteFine.setOnClickListener { clickFine() }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity).get(ModelRegistrazione::class)

    }

    @EventHandler
    private fun clickFine() {
        val returned: Long? =
            eseguiChiamataREST("associazioneProfilo", viewModel.toAccountInfo())

        if (returned == null || returned == 0L) {
            pulsanteFine.isEnabled = false

            Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.arancione, null))
                .setTextColor(resources.getColor(R.color.grigio, null))
                .show()
        } else {
            CurrentUser.id = returned
            listener?.onFragmentChangeActivity(Home::class.java)
        }
    }
}
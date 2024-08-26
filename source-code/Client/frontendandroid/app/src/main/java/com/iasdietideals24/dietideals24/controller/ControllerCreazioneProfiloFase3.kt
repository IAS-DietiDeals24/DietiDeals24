package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.Home
import com.iasdietideals24.dietideals24.databinding.Creazioneprofilofase3Binding
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeActivity

class ControllerCreazioneProfiloFase3 : Controller<Creazioneprofilofase3Binding>() {

    private lateinit var viewModel: ModelRegistrazione

    private var listenerBackButton: OnFragmentBackButton? = null
    private var listenerChangeActivity: OnFragmentChangeActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnFragmentBackButton) {
            listenerBackButton = requireContext() as OnFragmentBackButton
        }
        if (requireContext() is OnFragmentChangeActivity) {
            listenerChangeActivity = requireContext() as OnFragmentChangeActivity
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerBackButton = null
        listenerChangeActivity = null
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.creazioneProfiloFase3PulsanteIndietro.setOnClickListener { clickIndietro() }
        binding.creazioneProfiloFase3PulsanteFine.setOnClickListener { clickFine() }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity)[ModelRegistrazione::class]
    }

    @UIBuilder
    override fun impostaOsservatori() {
        val linkPersonaleObserver = Observer<String> { newLinkPersonale ->
            binding.creazioneProfiloFase3LinkPersonale.setText(newLinkPersonale)
        }
        viewModel.linkPersonale.observe(viewLifecycleOwner, linkPersonaleObserver)

        val linkInstagramObserver = Observer<String> { newLinkInstagram ->
            binding.creazioneProfiloFase3LinkInstagram.setText(newLinkInstagram)
        }
        viewModel.linkInstagram.observe(viewLifecycleOwner, linkInstagramObserver)

        val linkFacebookObserver = Observer<String> { newLinkFacebook ->
            binding.creazioneProfiloFase3LinkFacebook.setText(newLinkFacebook)
        }
        viewModel.linkFacebook.observe(viewLifecycleOwner, linkFacebookObserver)

        val linkGitHubObserver = Observer<String> { newLinkGitHub ->
            binding.creazioneProfiloFase3LinkGitHub.setText(newLinkGitHub)
        }
        viewModel.linkGitHub.observe(viewLifecycleOwner, linkGitHubObserver)

        val linkXObserver = Observer<String> { newLinkX ->
            binding.creazioneProfiloFase3LinkX.setText(newLinkX)
        }
        viewModel.linkX.observe(viewLifecycleOwner, linkXObserver)
    }

    @EventHandler
    private fun clickIndietro() {
        listenerBackButton?.onFragmentBackButton()
    }

    @EventHandler
    private fun clickFine() {
        val returned: Long? =
            eseguiChiamataREST("creazioneProfilo", viewModel.toAccountProfilo())

        if (returned == null || returned == 0L) {
            Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.arancione, null))
                .setTextColor(resources.getColor(R.color.grigio, null))
                .show()
        } else {
            CurrentUser.id = returned
            listenerChangeActivity?.onFragmentChangeActivity(Home::class.java)
        }
    }
}
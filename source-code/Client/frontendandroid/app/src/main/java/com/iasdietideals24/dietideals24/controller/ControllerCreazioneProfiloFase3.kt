package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.Home
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentChangeActivity

class ControllerCreazioneProfiloFase3 : Controller(R.layout.creazioneprofilofase3) {

    private lateinit var viewModel: ModelRegistrazione

    private lateinit var pulsanteIndietro: ImageButton
    private lateinit var linkPersonale: TextInputEditText
    private lateinit var linkInstagram: TextInputEditText
    private lateinit var linkFacebook: TextInputEditText
    private lateinit var linkGitHub: TextInputEditText
    private lateinit var linkX: TextInputEditText
    private lateinit var pulsanteFine: MaterialButton

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
    override fun trovaElementiInterfaccia() {
        pulsanteIndietro = fragmentView.findViewById(R.id.creazioneProfiloFase3_pulsanteIndietro)
        linkPersonale = fragmentView.findViewById(R.id.creazioneProfiloFase3_linkPersonale)
        linkInstagram = fragmentView.findViewById(R.id.creazioneProfiloFase3_linkInstagram)
        linkFacebook = fragmentView.findViewById(R.id.creazioneProfiloFase3_linkFacebook)
        linkGitHub = fragmentView.findViewById(R.id.creazioneProfiloFase3_linkGitHub)
        linkX = fragmentView.findViewById(R.id.creazioneProfiloFase3_linkX)
        pulsanteFine = fragmentView.findViewById(R.id.creazioneProfiloFase3_pulsanteFine)
    }

    @UIBuilder
    override fun impostaEventiClick() {
        pulsanteIndietro.setOnClickListener { clickIndietro() }
        pulsanteFine.setOnClickListener { clickFine() }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity).get(ModelRegistrazione::class)
    }

    @UIBuilder
    override fun impostaOsservatori() {
        val linkPersonaleObserver = Observer<String> { newLinkPersonale ->
            linkPersonale.setText(newLinkPersonale)
        }
        viewModel.linkPersonale.observe(viewLifecycleOwner, linkPersonaleObserver)

        val linkInstagramObserver = Observer<String> { newLinkInstagram ->
            linkInstagram.setText(newLinkInstagram)
        }
        viewModel.linkInstagram.observe(viewLifecycleOwner, linkInstagramObserver)


        val linkFacebookObserver = Observer<String> { newLinkFacebook ->
            linkFacebook.setText(newLinkFacebook)
        }
        viewModel.linkFacebook.observe(viewLifecycleOwner, linkFacebookObserver)


        val linkGitHubObserver = Observer<String> { newLinkGitHub ->
            linkGitHub.setText(newLinkGitHub)
        }
        viewModel.linkGitHub.observe(viewLifecycleOwner, linkGitHubObserver)

        val linkXObserver = Observer<String> { newLinkX ->
            linkX.setText(newLinkX)
        }
        viewModel.linkX.observe(viewLifecycleOwner, linkXObserver)
    }

    @EventHandler
    private fun clickIndietro() {
        listenerBackButton?.onFragmentBackButton()
    }

    @EventHandler
    private fun clickFine() {
        val returned: Boolean? =
            eseguiChiamataREST("creazioneProfilo", viewModel.toAccountProfileInfo())

        if (returned != true) {
            Toast.makeText(
                fragmentContext,
                R.string.apiError,
                Toast.LENGTH_SHORT
            ).show()
        } else
            listenerChangeActivity?.onFragmentChangeActivity(Home::class.java)
    }
}
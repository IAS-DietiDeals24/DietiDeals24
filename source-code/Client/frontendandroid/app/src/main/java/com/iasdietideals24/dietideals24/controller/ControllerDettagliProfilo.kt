package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.DettagliprofiloBinding
import com.iasdietideals24.dietideals24.model.ModelProfilo
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.data.Profilo
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.interfaces.OnBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnEditButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnOpenUrl
import java.time.LocalDate

class ControllerDettagliProfilo : Controller<DettagliprofiloBinding>() {

    private val args: ControllerDettagliProfiloArgs by navArgs()

    private lateinit var viewModel: ModelProfilo

    private var listenerBackButton: OnBackButton? = null
    private var listenerEditButton: OnEditButton? = null
    private var listenerOpenUrl: OnOpenUrl? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnBackButton) {
            listenerBackButton = requireContext() as OnBackButton
        }
        if (requireContext() is OnEditButton) {
            listenerEditButton = requireContext() as OnEditButton
        }
        if (requireContext() is OnOpenUrl) {
            listenerOpenUrl = requireContext() as OnOpenUrl
        }
    }

    override fun onDetach() {
        super.onDetach()

        listenerBackButton = null
        listenerEditButton = null
        listenerOpenUrl = null
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.dettagliProfiloPulsanteIndietro.setOnClickListener { clickIndietro() }

        binding.dettagliProfiloPulsanteModifica.setOnClickListener { clickModifica() }

        binding.dettagliProfiloFacebook.setOnClickListener { clickFacebook() }

        binding.dettagliProfiloInstagram.setOnClickListener { clickInstagram() }

        binding.dettagliProfiloGithub.setOnClickListener { clickGitHub() }

        binding.dettagliProfiloX.setOnClickListener { clickX() }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity)[ModelProfilo::class]

        val returned: Profilo? = eseguiChiamataREST(
            "caricaProfilo",
            if (args.id == "") CurrentUser.id else args.id
        )

        if (returned != null) {
            viewModel.tipoAccount.value = when (returned.tipoAccount) {
                "compratore" -> getString(R.string.tipoAccount_compratore)
                "venditore" -> getString(R.string.tipoAccount_venditore)
                else -> ""
            }
            viewModel.nomeUtente.value = returned.nomeUtente
            viewModel.immagineProfilo.value = returned.immagineProfilo
            viewModel.nome.value = returned.nome
            viewModel.cognome.value = returned.cognome
            viewModel.email.value = returned.email
            viewModel.dataNascita.value = returned.dataNascita
            viewModel.genere.value = returned.genere
            viewModel.areaGeografica.value = returned.areaGeografica
            viewModel.biografia.value = returned.biografia
            viewModel.linkInstagram.value = returned.linkInstagram
            viewModel.linkFacebook.value = returned.linkFacebook
            viewModel.linkGitHub.value = returned.linkGitHub
            viewModel.linkX.value = returned.linkX
            viewModel.linkPersonale.value = returned.linkPersonale

            if (CurrentUser.id != returned.idAccountCollegati)
                binding.dettagliProfiloPulsanteModifica.visibility = View.GONE
        } else {
            Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.blu, null))
                .setTextColor(resources.getColor(R.color.grigio, null))
                .show()
        }
    }

    @UIBuilder
    override fun impostaOsservatori() {
        val tipoAccountObserver = Observer<String> { newTipoAccount ->
            binding.dettagliProfiloTipoAccount.text = getString(
                R.string.dettagliProfilo_tipoAccount,
                when (newTipoAccount) {
                    "compratore" -> getString(R.string.tipoAccount_compratore)
                    "venditore" -> getString(R.string.tipoAccount_venditore)
                    else -> ""
                }
            )
        }
        viewModel.tipoAccount.observe(viewLifecycleOwner, tipoAccountObserver)

        val nomeUtenteObserver = Observer<String> { newNomeUtente ->
            binding.dettagliProfiloNomeUtente.text = newNomeUtente
        }
        viewModel.nomeUtente.observe(viewLifecycleOwner, nomeUtenteObserver)

        val immagineObserver = Observer<ByteArray> { newByteArray ->
            if (newByteArray.isNotEmpty()) {
                binding.dettagliProfiloImmagineUtente.load(newByteArray) {
                    crossfade(true)
                }
                binding.dettagliProfiloImmagineUtente.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }
        viewModel.immagineProfilo.observe(viewLifecycleOwner, immagineObserver)

        val nomeObserver = Observer<String> { newNome ->
            binding.dettagliProfiloNome.text = newNome
        }
        viewModel.nome.observe(viewLifecycleOwner, nomeObserver)

        val cognomeObserver = Observer<String> { newCognome ->
            binding.dettagliProfiloCognome.text = newCognome
        }
        viewModel.cognome.observe(viewLifecycleOwner, cognomeObserver)

        val dataNascitaObserver = Observer<LocalDate> { newData ->
            binding.dettagliProfiloDataNascita.text = newData.toLocalStringShort()
        }
        viewModel.dataNascita.observe(viewLifecycleOwner, dataNascitaObserver)

        val genereObserver = Observer<String> { newGenere ->
            binding.dettagliProfiloGenere.text = newGenere
        }
        viewModel.genere.observe(viewLifecycleOwner, genereObserver)

        val areaGeograficaObserver = Observer<String> { newAreaGeografica ->
            binding.dettagliProfiloAreaGeografica.text = newAreaGeografica
        }
        viewModel.areaGeografica.observe(viewLifecycleOwner, areaGeograficaObserver)

        val biografiaObserver = Observer<String> { newBiografia ->
            binding.dettagliProfiloBiografia.text = newBiografia
        }
        viewModel.biografia.observe(viewLifecycleOwner, biografiaObserver)

        val linkPersonaleObserver = Observer<String> { newLinkPersonale ->
            binding.dettagliProfiloLinkPersonale.text = newLinkPersonale
        }
        viewModel.linkPersonale.observe(viewLifecycleOwner, linkPersonaleObserver)
    }

    @EventHandler
    private fun clickIndietro() {
        viewModel.clear()

        listenerBackButton?.onBackButton()
    }

    @EventHandler
    private fun clickModifica() {
        listenerEditButton?.onEditButton(sender = ControllerDettagliProfilo::class)
    }

    @EventHandler
    private fun clickFacebook() {
        if (viewModel.linkFacebook.value != null)
            listenerOpenUrl?.onOpenUrl(viewModel.linkFacebook.value!!)
    }

    @EventHandler
    private fun clickInstagram() {
        if (viewModel.linkInstagram.value != null)
            listenerOpenUrl?.onOpenUrl(viewModel.linkInstagram.value!!)
    }

    @EventHandler
    private fun clickGitHub() {
        if (viewModel.linkGitHub.value != null)
            listenerOpenUrl?.onOpenUrl(viewModel.linkGitHub.value!!)
    }

    @EventHandler
    private fun clickX() {
        if (viewModel.linkX.value != null)
            listenerOpenUrl?.onOpenUrl(viewModel.linkX.value!!)
    }

}
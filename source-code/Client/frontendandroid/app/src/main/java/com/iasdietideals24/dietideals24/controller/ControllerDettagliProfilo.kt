package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.DettagliprofiloBinding
import com.iasdietideals24.dietideals24.model.ModelProfilo
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.data.Profilo
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentBackButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentEditButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnFragmentOpenUrl
import java.time.LocalDate

class ControllerDettagliProfilo : Controller<DettagliprofiloBinding>() {

    private val args: ControllerDettagliProfiloArgs by navArgs()

    private lateinit var viewModel: ModelProfilo

    private var listenerBackButton: OnFragmentBackButton? = null
    private var listenerEditButton: OnFragmentEditButton? = null
    private var listenerOpenUrl: OnFragmentOpenUrl? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnFragmentBackButton) {
            listenerBackButton = requireContext() as OnFragmentBackButton
        }
        if (requireContext() is OnFragmentEditButton) {
            listenerEditButton = requireContext() as OnFragmentEditButton
        }
        if (requireContext() is OnFragmentOpenUrl) {
            listenerOpenUrl = requireContext() as OnFragmentOpenUrl
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
        binding.dettagliProfiloPulsanteIndietro.setOnClickListener {
            viewModel.clear()

            listenerBackButton?.onFragmentBackButton()
        }
        binding.dettagliProfiloPulsanteModifica.setOnClickListener {
            listenerEditButton?.onFragmentEditButton(args.id, ControllerDettagliProfilo::class)
        }
        binding.dettagliProfiloFacebook.setOnClickListener {
            if (viewModel.linkFacebook.value != null)
                listenerOpenUrl?.onFragmentOpenUrl(viewModel.linkFacebook.value!!)
        }
        binding.dettagliProfiloInstagram.setOnClickListener {
            if (viewModel.linkInstagram.value != null)
                listenerOpenUrl?.onFragmentOpenUrl(viewModel.linkInstagram.value!!)
        }
        binding.dettagliProfiloGithub.setOnClickListener {
            if (viewModel.linkGitHub.value != null)
                listenerOpenUrl?.onFragmentOpenUrl(viewModel.linkGitHub.value!!)
        }
        binding.dettagliProfiloX.setOnClickListener {
            if (viewModel.linkX.value != null)
                listenerOpenUrl?.onFragmentOpenUrl(viewModel.linkX.value!!)
        }
    }

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity).get(ModelProfilo::class)

        val returned: Profilo? = eseguiChiamataREST(
            "caricaProfilo",
            if (args.id == 0L) CurrentUser.id else args.id
        )

        if (returned != null) {
            viewModel.idProfilo.value = returned._idProfilo
            viewModel.tipoAccount.value = when (returned._tipoAccount) {
                "compratore" -> getString(R.string.tipoAccount_compratore)
                "venditore" -> getString(R.string.tipoAccount_venditore)
                else -> ""
            }
            viewModel.nomeUtente.value = returned._nomeUtente
            viewModel.immagineProfilo.value = returned._immagineProfilo
            viewModel.nome.value = returned._nome
            viewModel.cognome.value = returned._cognome
            viewModel.email.value = returned._email
            viewModel.dataNascita.value = returned._dataNascita
            viewModel.genere.value = returned._genere
            viewModel.areaGeografica.value = returned._areaGeografica
            viewModel.biografia.value = returned._biografia
            viewModel.linkInstagram.value = returned._linkInstagram
            viewModel.linkFacebook.value = returned._linkFacebook
            viewModel.linkGitHub.value = returned._linkGitHub
            viewModel.linkX.value = returned._linkX
            viewModel.linkPersonale.value = returned._linkPersonale

            if (CurrentUser.id != returned._idAccountCollegati.first &&
                CurrentUser.id != returned._idAccountCollegati.second
            )
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
                newTipoAccount
            )
        }
        viewModel.tipoAccount.observe(viewLifecycleOwner, tipoAccountObserver)

        val nomeUtenteObserver = Observer<String> { newNomeUtente ->
            binding.dettagliProfiloNomeUtente.text = newNomeUtente
        }
        viewModel.nomeUtente.observe(viewLifecycleOwner, nomeUtenteObserver)

        val immagineObserver = Observer<ByteArray> { newByteArray: ByteArray? ->
            if (newByteArray?.isNotEmpty() == true)
                binding.dettagliProfiloImmagineUtente.load(newByteArray) {
                    crossfade(true)
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
}
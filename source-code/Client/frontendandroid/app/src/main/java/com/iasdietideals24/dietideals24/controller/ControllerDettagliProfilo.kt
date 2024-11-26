package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.DettagliprofiloBinding
import com.iasdietideals24.dietideals24.model.ModelProfilo
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.data.Account
import com.iasdietideals24.dietideals24.utilities.data.Profilo
import com.iasdietideals24.dietideals24.utilities.dto.AccountDto
import com.iasdietideals24.dietideals24.utilities.dto.ProfiloDto
import com.iasdietideals24.dietideals24.utilities.kscripts.OnBackButton
import com.iasdietideals24.dietideals24.utilities.kscripts.OnEditButton
import com.iasdietideals24.dietideals24.utilities.kscripts.OnOpenUrl
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.repositories.CompratoreRepository
import com.iasdietideals24.dietideals24.utilities.repositories.ProfiloRepository
import com.iasdietideals24.dietideals24.utilities.repositories.VenditoreRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import com.iasdietideals24.dietideals24.utilities.tools.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.time.LocalDate

class ControllerDettagliProfilo : Controller<DettagliprofiloBinding>() {

    // Navigation
    private val args: ControllerDettagliProfiloArgs by navArgs()

    // ViewModel
    private val viewModel: ModelProfilo by activityViewModel()

    // Repositories
    private val compratoreRepository: CompratoreRepository by inject()
    private val venditoreRepository: VenditoreRepository by inject()
    private val profiloRepository: ProfiloRepository by inject()

    // Listeners
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
        lifecycleScope.launch {
            try {
                var userName: String
                val account: Account = withContext(Dispatchers.IO) {
                    val returned = caricaAccount()
                    userName = returned.profiloShallow.nomeUtente
                    returned.toAccount()
                }
                val profilo: Profilo =
                    withContext(Dispatchers.IO) { caricaProfilo(userName).toProfilo() }

                if (profilo.nomeUtente != "") {
                    viewModel.tipoAccount.value = account.tipoAccount.name
                    viewModel.nomeUtente.value = profilo.nomeUtente
                    viewModel.immagineProfilo.value = profilo.immagineProfilo
                    viewModel.nome.value = profilo.nome
                    viewModel.cognome.value = profilo.cognome
                    viewModel.email.value = account.email
                    viewModel.dataNascita.value = profilo.dataNascita
                    viewModel.genere.value = profilo.genere
                    viewModel.areaGeografica.value = profilo.areaGeografica
                    viewModel.biografia.value = profilo.biografia
                    viewModel.linkInstagram.value = profilo.linkInstagram
                    viewModel.linkFacebook.value = profilo.linkFacebook
                    viewModel.linkGitHub.value = profilo.linkGitHub
                    viewModel.linkX.value = profilo.linkX
                    viewModel.linkPersonale.value = profilo.linkPersonale

                    if (CurrentUser.id != viewModel.email.value)
                        binding.dettagliProfiloPulsanteModifica.visibility = View.GONE
                    if (viewModel.linkInstagram.value == "")
                        binding.dettagliProfiloInstagram.visibility = View.GONE
                    if (viewModel.linkFacebook.value == "")
                        binding.dettagliProfiloFacebook.visibility = View.GONE
                    if (viewModel.linkGitHub.value == "")
                        binding.dettagliProfiloGithub.visibility = View.GONE
                    if (viewModel.linkX.value == "")
                        binding.dettagliProfiloX.visibility = View.GONE
                }
            } catch (_: Exception) {
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    private suspend fun caricaAccount(): AccountDto {
        var account: AccountDto = compratoreRepository.caricaAccountCompratore(args.id)

        if (account.email == "")
            account = venditoreRepository.caricaAccountVenditore(args.id)

        return account
    }

    private suspend fun caricaProfilo(nomeUtente: String): ProfiloDto {
        return profiloRepository.caricaProfilo(nomeUtente)
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

        val immagineObserver = Observer { newByteArray: ByteArray? ->
            when {
                newByteArray == null || newByteArray.isEmpty() -> {
                    binding.dettagliProfiloImmagineUtente.scaleType =
                        ImageView.ScaleType.CENTER_INSIDE
                    binding.dettagliProfiloImmagineUtente.setImageResource(R.drawable.icona_profilo)
                }

                else -> {
                    binding.dettagliProfiloImmagineUtente.load(newByteArray) {
                        crossfade(true)
                    }
                    binding.dettagliProfiloImmagineUtente.scaleType =
                        ImageView.ScaleType.CENTER_CROP
                }
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

        val emailObserver = Observer<String> { newEmail ->
            binding.dettagliProfiloEmail.text = newEmail
        }
        viewModel.email.observe(viewLifecycleOwner, emailObserver)

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
        Logger.log("Editing profile")

        listenerEditButton?.onEditButton(sender = ControllerDettagliProfilo::class)
    }

    @EventHandler
    private fun clickFacebook() {
        if (viewModel.linkFacebook.value != "")
            listenerOpenUrl?.onOpenUrl(viewModel.linkFacebook.value!!)
    }

    @EventHandler
    private fun clickInstagram() {
        if (viewModel.linkInstagram.value != "")
            listenerOpenUrl?.onOpenUrl(viewModel.linkInstagram.value!!)
    }

    @EventHandler
    private fun clickGitHub() {
        if (viewModel.linkGitHub.value != "")
            listenerOpenUrl?.onOpenUrl(viewModel.linkGitHub.value!!)
    }

    @EventHandler
    private fun clickX() {
        if (viewModel.linkX.value != "")
            listenerOpenUrl?.onOpenUrl(viewModel.linkX.value!!)
    }

}
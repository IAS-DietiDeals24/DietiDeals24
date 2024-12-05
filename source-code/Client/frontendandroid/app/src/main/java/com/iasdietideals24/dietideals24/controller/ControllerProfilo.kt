package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.ScelteIniziali
import com.iasdietideals24.dietideals24.databinding.ProfiloBinding
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.data.AnteprimaProfilo
import com.iasdietideals24.dietideals24.utilities.dto.AccountDto
import com.iasdietideals24.dietideals24.utilities.dto.CompratoreDto
import com.iasdietideals24.dietideals24.utilities.dto.ProfiloDto
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.kscripts.OnChangeActivity
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToCreatedAuctions
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToHelp
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToParticipation
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToProfile
import com.iasdietideals24.dietideals24.utilities.repositories.AuthRepository
import com.iasdietideals24.dietideals24.utilities.repositories.CompratoreRepository
import com.iasdietideals24.dietideals24.utilities.repositories.ProfiloRepository
import com.iasdietideals24.dietideals24.utilities.repositories.VenditoreRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class ControllerProfilo : Controller<ProfiloBinding>() {

    // Repositories
    private val authRepository: AuthRepository by inject()
    private val compratoreRepository: CompratoreRepository by inject()
    private val venditoreRepository: VenditoreRepository by inject()
    private val profiloRepository: ProfiloRepository by inject()

    // Listeners
    private var listenerProfile: OnGoToProfile? = null
    private var listenerCreatedAuctions: OnGoToCreatedAuctions? = null
    private var listenerParticipation: OnGoToParticipation? = null
    private var listenerHelp: OnGoToHelp? = null
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
        if (requireContext() is OnGoToHelp) {
            listenerHelp = requireContext() as OnGoToHelp
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
        listenerHelp = null
        listenerChangeActivity = null
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        lifecycleScope.launch {
            try {
                if (CurrentUser.id != 0L) {
                    val account: AccountDto = withContext(Dispatchers.IO) { caricaAccount() }
                    val nomeUtente = account.profiloShallow.nomeUtente
                    val result: AnteprimaProfilo =
                        withContext(Dispatchers.IO) { caricaProfilo(nomeUtente).toAnteprimaProfilo() }

                    if (result.nome != "") {
                        when (CurrentUser.tipoAccount) {
                            TipoAccount.COMPRATORE -> binding.profiloTipoAccount.text = getString(
                                R.string.profilo_tipoAccount,
                                getString(R.string.tipoAccount_compratore)
                            )

                            TipoAccount.VENDITORE -> binding.profiloTipoAccount.text = getString(
                                R.string.profilo_tipoAccount,
                                getString(R.string.tipoAccount_venditore)
                            )

                            TipoAccount.OSPITE -> binding.profiloTipoAccount.text = getString(
                                R.string.profilo_tipoAccount,
                                getString(R.string.tipoAccount_ospite)
                            )
                        }
                        binding.profiloNome.text = getString(R.string.placeholder, result.nome)

                        if (result.immagineProfilo.isNotEmpty()) {
                            binding.profiloImmagine.load(result.immagineProfilo) {
                                crossfade(true)
                            }
                            binding.profiloImmagine.scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                    }
                } else {
                    binding.profiloTipoAccount.text =
                        getString(
                            R.string.profilo_tipoAccount,
                            getString(R.string.tipoAccount_ospite)
                        )
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
            } catch (e: Exception) {
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    private suspend fun caricaAccount(): AccountDto {
        return when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> {
                compratoreRepository.caricaAccountCompratore(CurrentUser.id)
            }

            TipoAccount.VENDITORE -> {
                venditoreRepository.caricaAccountVenditore(CurrentUser.id)
            }

            else -> CompratoreDto()
        }
    }

    private suspend fun caricaProfilo(nomeUtente: String): ProfiloDto {
        return profiloRepository.caricaProfilo(nomeUtente)
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
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                logger.scriviLog("Showing user profile")
            }
        }

        listenerProfile?.onGoToProfile(CurrentUser.id, ControllerProfilo::class)
    }

    @EventHandler
    private fun clickAste() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                logger.scriviLog("Showing created auctions")
            }
        }

        listenerCreatedAuctions?.onGoToCreatedAuctions()
    }

    @EventHandler
    private fun clickStorico() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                logger.scriviLog("Showing participations")
            }
        }

        listenerParticipation?.onGoToParticipation()
    }

    @EventHandler
    private fun clickAiuto() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                logger.scriviLog("Showing help section")
            }
        }

        listenerHelp?.onGoToHelp()
    }

    @EventHandler
    private fun clickEsci() {
        // Cancella i dati di autenticazione memorizzati
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                CurrentUser.jwt = ""
                authRepository.cancellaJWT()
                authRepository.cancellaRuolo()
                logger.cancellaLog()
            }
        }

        listenerChangeActivity?.onChangeActivity(ScelteIniziali::class.java)
    }

}
package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.lifecycleScope
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.SelezioneaccessoregistrazioneBinding
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.kscripts.OnChangeActivity
import com.iasdietideals24.dietideals24.utilities.repositories.AuthRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject


class ControllerSelezioneAccessoRegistrazione : Controller<SelezioneaccessoregistrazioneBinding>() {

    // Repositories
    private val authRepository: AuthRepository by inject()

    // Listeners
    private var listener: OnChangeActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (requireContext() is OnChangeActivity) {
            listener = requireContext() as OnChangeActivity
        }
    }

    override fun onDetach() {
        super.onDetach()

        listener = null
    }

    @UIBuilder
    override fun impostaMessaggiCorpo() {
        when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_compratore)
                binding.selezioneAccessoRegistrazioneSaluto.text = getString(
                    R.string.selezioneAccessoRegistrazione_saluto,
                    stringaTipoAccount
                )
            }

            TipoAccount.VENDITORE -> {
                val stringaTipoAccount = getString(R.string.tipoAccount_venditore)
                binding.selezioneAccessoRegistrazioneSaluto.text = getString(
                    R.string.selezioneAccessoRegistrazione_saluto,
                    stringaTipoAccount
                )
            }

            else -> {
                // Non fare nulla
            }
        }
    }

    @UIBuilder
    override fun impostaEventiClick() {
        binding.selezioneAccessoRegistrazionePulsanteAccedi.setOnClickListener {
            clickAccedi()
        }

        binding.selezioneAccessoRegistrazionePulsanteRegistrati.setOnClickListener {
            clickRegistrati()
        }
    }

    @EventHandler
    private fun clickAccedi() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                logger.scriviLog("Sign-in selected")
            }

            val url = withContext(Dispatchers.IO) {
                authRepository.recuperaUrlAutenticazione("ias://com.iasdietideals24.dietideals24/signin")
                    .replace("/oauth2/authorize", "/login")
            }

            val intent = CustomTabsIntent.Builder()
                .setShowTitle(false)
                .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
                .setDefaultColorSchemeParams(
                    CustomTabColorSchemeParams.Builder()
                        .setToolbarColor(
                            resources.getColor(
                                R.color.arancione,
                                fragmentContext.theme
                            )
                        )
                        .setNavigationBarColor(
                            resources.getColor(
                                R.color.arancione,
                                fragmentContext.theme
                            )
                        )
                        .build()
                )
                .build()
            intent.launchUrl(fragmentContext, Uri.parse(url))
        }
    }

    @EventHandler
    private fun clickRegistrati() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                logger.scriviLog("Sign-up selected")
            }

            val url = withContext(Dispatchers.IO) {
                authRepository.recuperaUrlAutenticazione("ias://com.iasdietideals24.dietideals24/signup")
                    .replace("/oauth2/authorize", "/signup")
            }

            val intent = CustomTabsIntent.Builder()
                .setShowTitle(false)
                .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
                .setDefaultColorSchemeParams(
                    CustomTabColorSchemeParams.Builder()
                        .setToolbarColor(resources.getColor(R.color.blu, fragmentContext.theme))
                        .setNavigationBarColor(
                            resources.getColor(
                                R.color.blu,
                                fragmentContext.theme
                            )
                        )
                        .build()
                )
                .build()
            intent.launchUrl(fragmentContext, Uri.parse(url))
        }
    }
}
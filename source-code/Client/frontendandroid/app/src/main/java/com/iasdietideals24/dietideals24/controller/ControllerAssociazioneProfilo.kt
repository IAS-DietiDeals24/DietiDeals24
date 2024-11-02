package com.iasdietideals24.dietideals24.controller

import android.accounts.AccountManager
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.Home
import com.iasdietideals24.dietideals24.databinding.AssociaprofiloBinding
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.data.Account
import com.iasdietideals24.dietideals24.utilities.dto.exceptional.PutProfiloDto
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.kscripts.OnChangeActivity
import com.iasdietideals24.dietideals24.utilities.repositories.ProfiloRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import com.iasdietideals24.dietideals24.utilities.tools.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ControllerAssociazioneProfilo : Controller<AssociaprofiloBinding>() {

    // ViewModel
    private val viewModel: ModelRegistrazione by activityViewModel()

    // Repositories
    private val profiloRepository: ProfiloRepository by inject()

    // Listeners
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

    @EventHandler
    private fun clickFine() {
        lifecycleScope.launch {
            try {
                val returned: Account =
                    withContext(Dispatchers.IO) { associazioneProfilo().toAccount() }

                when (returned.email) {
                    "" -> binding.associaProfiloPulsanteFine.isEnabled = false

                    else -> {
                        Logger.log("Profile linking successful")

                        CurrentUser.id = returned.email

                        registrazioneAccountManager()

                        listenerChangeActivity?.onChangeActivity(Home::class.java)
                    }
                }
            } catch (_: Exception) {
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    private suspend fun associazioneProfilo(): PutProfiloDto {
        return when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> profiloRepository.creazioneAccountProfilo(
                viewModel.nomeUtente.value!!,
                viewModel.toAccountCompratore()
            )

            TipoAccount.VENDITORE -> profiloRepository.creazioneAccountProfilo(
                viewModel.nomeUtente.value!!,
                viewModel.toAccountVenditore()
            )

            else -> PutProfiloDto()
        }
    }

    private fun registrazioneAccountManager() {
        val accountManager = AccountManager.get(context)

        val accountData = Bundle()
        accountData.putString("TipoAccount", viewModel.tipoAccount.value.toString())

        val account = android.accounts.Account(
            viewModel.email.value,
            "com.iasdietideals24.dietideals24.account"
        )

        accountManager.addAccountExplicitly(account, viewModel.password.value, accountData)
    }
}
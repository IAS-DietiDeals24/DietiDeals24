package com.iasdietideals24.dietideals24.controller

import android.content.Context
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.activities.Home
import com.iasdietideals24.dietideals24.databinding.AssociaprofiloBinding
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.data.Account
import com.iasdietideals24.dietideals24.utilities.dto.AccountDto
import com.iasdietideals24.dietideals24.utilities.dto.CompratoreDto
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.kscripts.OnChangeActivity
import com.iasdietideals24.dietideals24.utilities.repositories.CompratoreRepository
import com.iasdietideals24.dietideals24.utilities.repositories.VenditoreRepository
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
    private val compratoreRepository: CompratoreRepository by inject()
    private val venditoreRepository: VenditoreRepository by inject()

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

                        CurrentUser.id = returned.idAccount

                        listenerChangeActivity?.onChangeActivity(Home::class.java)
                    }
                }
            } catch (e: Exception) {
                Log.d("Debug", e.message.toString())
                Snackbar.make(fragmentView, R.string.apiError, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    // TODO: modificare dopo aver implementato trova account per email
    private suspend fun associazioneProfilo(): AccountDto {
        viewModel.nomeUtente.postValue(
            when (CurrentUser.tipoAccount) {
                TipoAccount.COMPRATORE ->
                    venditoreRepository.caricaAccountVenditore(0L).profiloShallow.nomeUtente

                TipoAccount.VENDITORE ->
                    compratoreRepository.caricaAccountCompratore(0L).profiloShallow.nomeUtente

                else -> ""
            }
        )

        return when (CurrentUser.tipoAccount) {
            TipoAccount.COMPRATORE -> compratoreRepository.creaAccountCompratore(
                viewModel.toAccountCompratore()
            )

            TipoAccount.VENDITORE -> venditoreRepository.creaAccountVenditore(
                viewModel.toAccountVenditore()
            )

            else -> CompratoreDto()
        }
    }
}
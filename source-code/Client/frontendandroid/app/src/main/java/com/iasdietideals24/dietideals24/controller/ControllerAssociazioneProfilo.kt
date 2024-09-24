package com.iasdietideals24.dietideals24.controller

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.iasdietideals24.dietideals24.activities.Home
import com.iasdietideals24.dietideals24.databinding.AssociaprofiloBinding
import com.iasdietideals24.dietideals24.model.ModelRegistrazione
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.APIController
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.Logger
import com.iasdietideals24.dietideals24.utilities.classes.TipoAccount
import com.iasdietideals24.dietideals24.utilities.classes.data.Account
import com.iasdietideals24.dietideals24.utilities.interfaces.OnChangeActivity

class ControllerAssociazioneProfilo : Controller<AssociaprofiloBinding>() {

    private lateinit var viewModel: ModelRegistrazione

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

    @UIBuilder
    override fun elaborazioneAggiuntiva() {
        viewModel = ViewModelProvider(fragmentActivity)[ModelRegistrazione::class]
    }

    @EventHandler
    private fun clickFine() {
        val returned: Account = associazioneProfilo()

        when (returned.email) {
            "" -> binding.associaProfiloPulsanteFine.isEnabled = false

            else -> {
                Logger.log("Profile linking successful")

                CurrentUser.id = returned.email
                listenerChangeActivity?.onChangeActivity(Home::class.java)
            }
        }
    }

    private fun associazioneProfilo(): Account {
        return if (CurrentUser.tipoAccount == TipoAccount.COMPRATORE) {
            val call = APIController.instance.creazioneAccountCompratore(
                viewModel.email.value!!,
                viewModel.toAccountCompratore()
            )
            chiamaAPI(call).toAccount()
        } else {
            val call = APIController.instance.creazioneAccountVenditore(
                viewModel.email.value!!,
                viewModel.toAccountVenditore()
            )
            chiamaAPI(call).toAccount()
        }
    }
}
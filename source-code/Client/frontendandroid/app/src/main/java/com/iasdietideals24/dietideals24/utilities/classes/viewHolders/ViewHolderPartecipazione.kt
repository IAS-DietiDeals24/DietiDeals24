package com.iasdietideals24.dietideals24.utilities.classes.viewHolders

import android.content.Context
import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.AstaBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.APIController
import com.iasdietideals24.dietideals24.utilities.classes.CurrentUser
import com.iasdietideals24.dietideals24.utilities.classes.Logger
import com.iasdietideals24.dietideals24.utilities.classes.TipoAsta
import com.iasdietideals24.dietideals24.utilities.classes.chiamaAPI
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.data.Offerta
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToDetails

class ViewHolderPartecipazione(private val binding: AstaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var listenerGoToDetails: OnGoToDetails? = null

    @UIBuilder
    fun setListeners(context: Context) {
        if (context is OnGoToDetails) {
            listenerGoToDetails = context
        }
    }

    @UIBuilder
    fun bind(currentAsta: AnteprimaAsta, resources: Resources) {
        when (currentAsta.tipoAsta) {
            TipoAsta.INVERSA -> {
                binding.astaTipo.text = resources.getString(R.string.tipoAsta_astaInversa)
            }

            TipoAsta.SILENZIOSA -> {
                binding.astaTipo.text = resources.getString(R.string.tipoAsta_astaSilenziosa)
            }

            TipoAsta.TEMPO_FISSO -> {
                binding.astaTipo.text = resources.getString(R.string.tipoAsta_astaTempoFisso)
            }
        }

        binding.astaMessaggio.text = resources.getString(R.string.asta_messaggioPartecipazione)
        binding.astaDataScadenza.text =
            currentAsta.dataScadenza.toLocalStringShort()
        binding.astaOraScadenza.text = currentAsta.oraScadenza.toString()

        if (currentAsta.foto.isNotEmpty())
            binding.astaImmagine.load(currentAsta.foto) {
                crossfade(true)
            }

        binding.astaNome.text = currentAsta.nome

        binding.astaOfferta.text = resources.getString(
            R.string.placeholder_prezzo,
            if (currentAsta.tipoAsta != TipoAsta.SILENZIOSA) {
                val offerta: Offerta = recuperaOffertaPersonale(currentAsta)

                offerta.offerta.toString()
            } else
                "???"
        )

        binding.astaLinearLayout3.setOnClickListener {
            Logger.log("Showing auction details")

            listenerGoToDetails?.onGoToDetails(currentAsta.id, currentAsta.tipoAsta, this::class)
        }
    }

    private fun recuperaOffertaPersonale(currentAsta: AnteprimaAsta): Offerta {
        return when (currentAsta.tipoAsta) {
            TipoAsta.INVERSA -> {
                val call = APIController.instance.recuperaOffertaPersonalePiuBassaInversa(
                    currentAsta.id,
                    CurrentUser.id
                )

                chiamaAPI(call).toOfferta()
            }

            TipoAsta.TEMPO_FISSO -> {
                val call = APIController.instance.recuperaOffertaPersonalePiuAltaTempoFisso(
                    currentAsta.id,
                    CurrentUser.id
                )

                chiamaAPI(call).toOfferta()
            }

            else -> {
                val call = APIController.instance.recuperaOffertaPersonalePiuAltaSilenziosa(
                    currentAsta.id,
                    CurrentUser.id
                )

                chiamaAPI(call).toOfferta()
            }
        }
    }

    fun cleanListeners() {
        listenerGoToDetails = null
    }
}
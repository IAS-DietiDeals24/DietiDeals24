package com.iasdietideals24.dietideals24.utilities.classes.viewHolders

import android.content.Context
import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.AstaBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.APIController
import com.iasdietideals24.dietideals24.utilities.classes.Logger
import com.iasdietideals24.dietideals24.utilities.classes.TipoAsta
import com.iasdietideals24.dietideals24.utilities.classes.chiamaAPI
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.data.Offerta
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.interfaces.OnEditButton
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToBids
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.interfaces.OnRefresh
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewHolderAstaCreata(private val binding: AstaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var listenerGoToDetails: OnGoToDetails? = null
    private var listenerEditButton: OnEditButton? = null
    private var listenerRefresh: OnRefresh? = null
    private var listenerGoToBids: OnGoToBids? = null

    @UIBuilder
    fun setListeners(context: Context) {
        if (context is OnGoToDetails) {
            listenerGoToDetails = context
        }
        if (context is OnEditButton) {
            listenerEditButton = context
        }
        if (context is OnRefresh) {
            listenerRefresh = context
        }
        if (context is OnGoToBids) {
            listenerGoToBids = context
        }
    }

    @UIBuilder
    fun bind(currentAsta: AnteprimaAsta, resources: Resources) {
        when (currentAsta.tipoAsta) {
            TipoAsta.INVERSA -> {
                binding.astaTipo.text = resources.getString(R.string.tipoAsta_astaInversa)
                binding.astaMessaggio.text =
                    resources.getString(R.string.dettagliAsta_testoOfferta2)
            }

            TipoAsta.SILENZIOSA -> {
                binding.astaTipo.text = resources.getString(R.string.tipoAsta_astaSilenziosa)
                binding.astaMessaggio.text =
                    resources.getString(R.string.dettagliAsta_testoOfferta1)
            }

            TipoAsta.TEMPO_FISSO -> {
                binding.astaTipo.text = resources.getString(R.string.tipoAsta_astaTempoFisso)
                binding.astaMessaggio.text =
                    resources.getString(R.string.dettagliAsta_testoOfferta1)
            }
        }
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
            {
                val offerta: Offerta = recuperaOfferta(currentAsta)

                offerta.offerta.toString()
            }
        )

        binding.astaLinearLayout3.setOnClickListener {
            Logger.log("Showing auction details")

            listenerGoToDetails?.onGoToDetails(currentAsta.id, currentAsta.tipoAsta, this::class)
        }

        binding.astaModificaAsta.setOnClickListener {
            Logger.log("Editing acution")

            listenerEditButton?.onEditButton(currentAsta.id, this::class)
        }

        binding.astaEliminaAsta.setOnClickListener {
            MaterialAlertDialogBuilder(itemView.context, R.style.Dialog)
                .setTitle(R.string.elimina_titoloConfermaElimina)
                .setMessage(R.string.elimina_testoConfermaElimina)
                .setPositiveButton(R.string.ok) { _, _ ->
                    Logger.log("Deleting auction")

                    clickConferma(currentAsta, resources)
                }
                .setNegativeButton(R.string.annulla) { _, _ -> }
                .show()
        }

        binding.astaElencoOfferte.setOnClickListener {
            Logger.log("Showing auction bids")

            listenerGoToBids?.onGoToBids(currentAsta.id, currentAsta.tipoAsta, this::class)
        }
    }

    private fun clickConferma(currentAsta: AnteprimaAsta, resources: Resources) {
        val call = when (currentAsta.tipoAsta) {
            TipoAsta.INVERSA -> APIController.instance.eliminaAstaInversa(currentAsta.id)
            TipoAsta.TEMPO_FISSO -> APIController.instance.eliminaAstaTempoFisso(currentAsta.id)
            TipoAsta.SILENZIOSA -> APIController.instance.eliminaAstaSilenziosa(currentAsta.id)
        }

        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful)
                    onRESTSuccess(resources)
                else
                    onRESTUnsuccess(resources)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                onRESTFailure(resources)
            }
        })
    }

    private fun onRESTSuccess(resources: Resources) {
        Snackbar.make(
            itemView,
            R.string.dettagliAsta_successoEliminazione,
            Snackbar.LENGTH_SHORT
        )
            .setBackgroundTint(resources.getColor(R.color.blu, null))
            .setTextColor(resources.getColor(R.color.grigio, null))
            .show()

        listenerRefresh?.onRefresh(sender = this::class)
    }

    private fun onRESTUnsuccess(resources: Resources) {
        Snackbar.make(
            itemView,
            R.string.dettagliAsta_erroreEliminazione,
            Snackbar.LENGTH_SHORT
        )
            .setBackgroundTint(resources.getColor(R.color.blu, null))
            .setTextColor(resources.getColor(R.color.grigio, null))
            .show()
    }

    private fun onRESTFailure(resources: Resources) {
        Snackbar.make(itemView, R.string.apiError, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(resources.getColor(R.color.blu, null))
            .setTextColor(resources.getColor(R.color.grigio, null))
            .show()
    }

    private fun recuperaOfferta(currentAsta: AnteprimaAsta): Offerta {
        return if (currentAsta.tipoAsta == TipoAsta.INVERSA) {
            val call = APIController.instance.recuperaOffertaPiuBassa(currentAsta.id)

            chiamaAPI(call).toOfferta()
        } else {
            val call = APIController.instance.recuperaOffertaPiuAlta(currentAsta.id)

            chiamaAPI(call).toOfferta()
        }
    }

    fun cleanListeners() {
        listenerGoToDetails = null
        listenerEditButton = null
        listenerRefresh = null
        listenerGoToBids = null
    }
}
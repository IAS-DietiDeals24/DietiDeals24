package com.iasdietideals24.dietideals24.utilities.classes.viewHolders

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.AstaBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.Logger
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToDetails

class ViewHolderAnteprimaAsta(private val binding: AstaBinding) :
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
            "Inversa" -> {
                binding.astaTipo.text = resources.getString(R.string.tipoAsta_astaInversa)
                binding.astaMessaggio.text =
                    resources.getString(R.string.dettagliAsta_testoOfferta2)
            }

            "Silenziosa" -> {
                binding.astaTipo.text = resources.getString(R.string.tipoAsta_astaSilenziosa)
                binding.astaMessaggio.text =
                    resources.getString(R.string.dettagliAsta_testoOfferta1)
            }

            "Tempo fisso" -> {
                binding.astaTipo.text = resources.getString(R.string.tipoAsta_astaTempoFisso)
                binding.astaMessaggio.text =
                    resources.getString(R.string.dettagliAsta_testoOfferta1)
            }

            else -> {
                binding.astaTipo.text = ""
                binding.astaMessaggio.text = ""
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
            if (currentAsta.tipoAsta != "Silenziosa")
                currentAsta.offerta.toString()
            else
                "???"
        )
        binding.astaModificaAsta.visibility = View.GONE
        binding.astaEliminaAsta.visibility = View.GONE
        binding.astaElencoOfferte.visibility = View.GONE

        binding.astaLinearLayout3.setOnClickListener {
            Logger.log("Showing auction details")

            listenerGoToDetails?.onGoToDetails(currentAsta.id, this::class)
        }
    }

    fun cleanListeners() {
        listenerGoToDetails = null
    }
}
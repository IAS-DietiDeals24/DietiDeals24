package com.iasdietideals24.dietideals24.utilities.classes.viewHolders

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.AstaBinding
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToDetails

class ViewHolderAnteprimaAsta(binding: AstaBinding) : RecyclerView.ViewHolder(binding.root) {

    private val binding = binding

    private var listener: OnGoToDetails? = null

    fun setListeners(context: Context) {
        if (context is OnGoToDetails) {
            listener = context
        }
    }

    fun bind(currentAsta: AnteprimaAsta, resources: Resources) {
        when (currentAsta._tipoAsta) {
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
            currentAsta._dataScadenza.toLocalStringShort()
        binding.astaOraScadenza.text = currentAsta._oraScadenza.toString()

        if (currentAsta._foto.isNotEmpty())
            binding.astaImmagine.load(currentAsta._foto) {
                crossfade(true)
            }
        binding.astaNome.text = currentAsta._nome
        binding.astaOfferta.text = currentAsta._offerta.toString()
        binding.astaModificaAsta.visibility = View.GONE
        binding.astaEliminaAsta.visibility = View.GONE
        binding.astaElencoOfferte.visibility = View.GONE

        binding.astaLinearLayout3.setOnClickListener {
            listener?.onGoToDetails(itemId, this::class)
        }
    }

    fun cleanListeners() {
        listener = null
    }
}
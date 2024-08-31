package com.iasdietideals24.dietideals24.utilities.classes.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iasdietideals24.dietideals24.databinding.AstaBinding
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.viewHolders.ViewHolderPartecipazione

class AdapterPartecipazioni(
    private val aste: Array<AnteprimaAsta>,
    private val resources: Resources
) : RecyclerView.Adapter<ViewHolderPartecipazione>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPartecipazione {
        val binding = AstaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val viewHolderPartecipazione = ViewHolderPartecipazione(binding)
        viewHolderPartecipazione.setListeners(parent.context)

        return viewHolderPartecipazione
    }

    override fun getItemCount(): Int {
        return aste.size
    }

    override fun onBindViewHolder(holder: ViewHolderPartecipazione, position: Int) {
        val currentAsta = aste[position]

        holder.bind(currentAsta, resources)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolderPartecipazione) {
        super.onViewDetachedFromWindow(holder)

        holder.cleanListeners()
    }
}

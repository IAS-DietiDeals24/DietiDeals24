package com.iasdietideals24.dietideals24.utilities.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.iasdietideals24.dietideals24.databinding.AstaBinding
import com.iasdietideals24.dietideals24.utilities.dto.AstaDto
import com.iasdietideals24.dietideals24.utilities.viewHolders.ViewHolderPartecipazione

class AdapterPartecipazioni(
    diffCallback: DiffUtil.ItemCallback<AstaDto>,
    private val resources: Resources
) : PagingDataAdapter<AstaDto, ViewHolderPartecipazione>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPartecipazione {
        val binding = AstaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val viewHolderPartecipazione = ViewHolderPartecipazione(binding)
        viewHolderPartecipazione.setListeners(parent.context)

        return viewHolderPartecipazione
    }

    override fun onBindViewHolder(holder: ViewHolderPartecipazione, position: Int) {
        val currentAsta = getItem(position)

        if (currentAsta != null) holder.bind(currentAsta.toAnteprimaAsta(), resources)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolderPartecipazione) {
        super.onViewDetachedFromWindow(holder)

        holder.cleanListeners()
    }
}

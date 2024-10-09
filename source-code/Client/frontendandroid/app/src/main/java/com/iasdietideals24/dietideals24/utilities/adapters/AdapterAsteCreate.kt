package com.iasdietideals24.dietideals24.utilities.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.iasdietideals24.dietideals24.databinding.AstaBinding
import com.iasdietideals24.dietideals24.utilities.dto.AstaDto
import com.iasdietideals24.dietideals24.utilities.viewHolders.ViewHolderAstaCreata

class AdapterAsteCreate(
    diffCallback: DiffUtil.ItemCallback<AstaDto>,
    private val resources: Resources
) : PagingDataAdapter<AstaDto, ViewHolderAstaCreata>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAstaCreata {
        val binding = AstaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val viewHolderAstaCreata = ViewHolderAstaCreata(binding)
        viewHolderAstaCreata.setListeners(parent.context)

        return viewHolderAstaCreata
    }

    override fun onBindViewHolder(holder: ViewHolderAstaCreata, position: Int) {
        val currentAsta = getItem(position)

        if (currentAsta != null) holder.bind(currentAsta.toAnteprimaAsta(), resources)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolderAstaCreata) {
        super.onViewDetachedFromWindow(holder)

        holder.cleanListeners()
    }
}

package com.iasdietideals24.dietideals24.utilities.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.iasdietideals24.dietideals24.databinding.AstaBinding
import com.iasdietideals24.dietideals24.utilities.dto.AstaDto
import com.iasdietideals24.dietideals24.utilities.enumerations.StatoAsta
import com.iasdietideals24.dietideals24.utilities.viewHolders.ViewHolderAnteprimaAsta


class AdapterHome(
    diffCallback: DiffUtil.ItemCallback<AstaDto>,
    private val resources: Resources
) : PagingDataAdapter<AstaDto, ViewHolderAnteprimaAsta>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAnteprimaAsta {
        val binding = AstaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val viewHolderAnteprimaAsta = ViewHolderAnteprimaAsta(binding)
        viewHolderAnteprimaAsta.setListeners(parent.context)

        return viewHolderAnteprimaAsta
    }

    override fun onBindViewHolder(holder: ViewHolderAnteprimaAsta, position: Int) {
        val currentAsta = getItem(position)

        if (currentAsta != null && StatoAsta.valueOf(currentAsta.stato) != StatoAsta.CLOSED)
            holder.bind(currentAsta.toAnteprimaAsta(), resources)
        else {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolderAnteprimaAsta) {
        super.onViewDetachedFromWindow(holder)

        holder.cleanListeners()
    }
}

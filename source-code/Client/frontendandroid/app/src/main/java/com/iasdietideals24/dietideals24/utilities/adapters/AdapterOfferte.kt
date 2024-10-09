package com.iasdietideals24.dietideals24.utilities.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.iasdietideals24.dietideals24.databinding.OffertaBinding
import com.iasdietideals24.dietideals24.utilities.dto.OffertaDto
import com.iasdietideals24.dietideals24.utilities.viewHolders.ViewHolderOfferta

class AdapterOfferte(
    diffCallback: DiffUtil.ItemCallback<OffertaDto>,
    private val resources: Resources
) : PagingDataAdapter<OffertaDto, ViewHolderOfferta>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderOfferta {
        val binding = OffertaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val viewHolderOfferta = ViewHolderOfferta(binding)
        viewHolderOfferta.setListeners(parent.context)

        return viewHolderOfferta
    }

    override fun onBindViewHolder(holder: ViewHolderOfferta, position: Int) {
        val currentOfferta = getItem(position)

        if (currentOfferta != null) holder.bind(currentOfferta.toOffertaRicevuta(), resources)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolderOfferta) {
        super.onViewDetachedFromWindow(holder)

        holder.cleanListeners()
    }
}

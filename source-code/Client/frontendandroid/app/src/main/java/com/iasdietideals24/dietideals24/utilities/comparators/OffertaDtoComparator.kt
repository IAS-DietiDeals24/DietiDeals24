package com.iasdietideals24.dietideals24.utilities.comparators

import androidx.recyclerview.widget.DiffUtil
import com.iasdietideals24.dietideals24.utilities.dto.OffertaDto

object OffertaDtoComparator : DiffUtil.ItemCallback<OffertaDto>() {
    override fun areItemsTheSame(oldItem: OffertaDto, newItem: OffertaDto): Boolean {
        return oldItem.idOfferta == newItem.idOfferta
    }

    override fun areContentsTheSame(oldItem: OffertaDto, newItem: OffertaDto): Boolean {
        return oldItem == newItem
    }
}
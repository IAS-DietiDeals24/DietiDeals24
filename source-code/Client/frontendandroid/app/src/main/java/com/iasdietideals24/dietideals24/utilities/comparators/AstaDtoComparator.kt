package com.iasdietideals24.dietideals24.utilities.comparators

import androidx.recyclerview.widget.DiffUtil
import com.iasdietideals24.dietideals24.utilities.dto.AstaDto

object AstaDtoComparator : DiffUtil.ItemCallback<AstaDto>() {
    override fun areItemsTheSame(oldItem: AstaDto, newItem: AstaDto): Boolean {
        return oldItem.idAsta == newItem.idAsta
    }

    override fun areContentsTheSame(oldItem: AstaDto, newItem: AstaDto): Boolean {
        return oldItem == newItem
    }
}
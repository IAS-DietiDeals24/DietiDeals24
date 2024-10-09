package com.iasdietideals24.dietideals24.utilities.comparators

import androidx.recyclerview.widget.DiffUtil
import com.iasdietideals24.dietideals24.utilities.dto.NotificaDto

object NotificaDtoComparator : DiffUtil.ItemCallback<NotificaDto>() {
    override fun areItemsTheSame(oldItem: NotificaDto, newItem: NotificaDto): Boolean {
        return oldItem.idNotifica == newItem.idNotifica
    }

    override fun areContentsTheSame(oldItem: NotificaDto, newItem: NotificaDto): Boolean {
        return oldItem == newItem
    }
}
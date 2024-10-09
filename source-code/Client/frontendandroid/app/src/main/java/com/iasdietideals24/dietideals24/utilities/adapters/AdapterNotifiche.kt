package com.iasdietideals24.dietideals24.utilities.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.iasdietideals24.dietideals24.databinding.NotificaBinding
import com.iasdietideals24.dietideals24.utilities.dto.NotificaDto
import com.iasdietideals24.dietideals24.utilities.viewHolders.ViewHolderNotifica

class AdapterNotifiche(
    diffCallback: DiffUtil.ItemCallback<NotificaDto>,
    private val resources: Resources
) : PagingDataAdapter<NotificaDto, ViewHolderNotifica>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNotifica {
        val binding = NotificaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val viewHolderNotifica = ViewHolderNotifica(binding)
        viewHolderNotifica.setListeners(parent.context)

        return viewHolderNotifica
    }

    override fun onBindViewHolder(holder: ViewHolderNotifica, position: Int) {
        val currentNotifica = getItem(position)

        if (currentNotifica != null) holder.bind(currentNotifica.toNotifica(), resources)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolderNotifica) {
        super.onViewDetachedFromWindow(holder)

        holder.cleanListeners()
    }
}

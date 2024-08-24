package com.iasdietideals24.dietideals24.utilities.classes.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iasdietideals24.dietideals24.databinding.NotificaBinding
import com.iasdietideals24.dietideals24.utilities.classes.data.Notifica
import com.iasdietideals24.dietideals24.utilities.classes.viewHolders.ViewHolderNotifica

class AdapterNotifiche(
    private val notifiche: Array<Notifica>,
    private val resources: Resources
) : RecyclerView.Adapter<ViewHolderNotifica>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNotifica {
        val binding = NotificaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val viewHolderNotifica = ViewHolderNotifica(binding)
        viewHolderNotifica.setListeners(parent.context)

        return viewHolderNotifica
    }

    override fun getItemCount(): Int {
        return notifiche.size
    }

    override fun onBindViewHolder(holder: ViewHolderNotifica, position: Int) {
        val currentNotifica = notifiche[position]

        holder.bind(currentNotifica, resources)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolderNotifica) {
        super.onViewDetachedFromWindow(holder)

        holder.cleanListeners()
    }
}

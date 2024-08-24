package com.iasdietideals24.dietideals24.utilities.classes.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iasdietideals24.dietideals24.databinding.NotificaBinding
import com.iasdietideals24.dietideals24.utilities.classes.data.DatiNotifica
import com.iasdietideals24.dietideals24.utilities.classes.viewHolders.Notifica

class AdapterNotifiche(
    private val notifiche: Array<DatiNotifica>,
    private val resources: Resources
) : RecyclerView.Adapter<Notifica>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Notifica {
        val binding = NotificaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val notifica = Notifica(binding)
        notifica.setListeners(parent.context)

        return notifica
    }

    override fun getItemCount(): Int {
        return notifiche.size
    }

    override fun onBindViewHolder(holder: Notifica, position: Int) {
        val currentNotifica = notifiche[position]

        holder.bind(currentNotifica, resources)
    }

    override fun onViewDetachedFromWindow(holder: Notifica) {
        super.onViewDetachedFromWindow(holder)

        holder.cleanListeners()
    }
}

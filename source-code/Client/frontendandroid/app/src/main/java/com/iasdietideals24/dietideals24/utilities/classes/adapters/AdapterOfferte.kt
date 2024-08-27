package com.iasdietideals24.dietideals24.utilities.classes.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iasdietideals24.dietideals24.databinding.OffertaBinding
import com.iasdietideals24.dietideals24.utilities.classes.data.OffertaRicevuta
import com.iasdietideals24.dietideals24.utilities.classes.viewHolders.ViewHolderOfferta

class AdapterOfferte(
    private val notifiche: Array<OffertaRicevuta>,
    private val resources: Resources
) : RecyclerView.Adapter<ViewHolderOfferta>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderOfferta {
        val binding = OffertaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val viewHolderOfferta = ViewHolderOfferta(binding)
        viewHolderOfferta.setListeners(parent.context)

        return viewHolderOfferta
    }

    override fun getItemCount(): Int {
        return notifiche.size
    }

    override fun onBindViewHolder(holder: ViewHolderOfferta, position: Int) {
        val currentOfferta = notifiche[position]

        holder.bind(currentOfferta, resources)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolderOfferta) {
        super.onViewDetachedFromWindow(holder)

        holder.cleanListeners()
    }
}

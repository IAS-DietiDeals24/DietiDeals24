package com.iasdietideals24.dietideals24.utilities.classes.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iasdietideals24.dietideals24.databinding.AstaBinding
import com.iasdietideals24.dietideals24.utilities.classes.data.DatiAnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.viewHolders.AnteprimaAsta

class AdapterHome(
    private val aste: Array<DatiAnteprimaAsta>,
    private val resources: Resources
) : RecyclerView.Adapter<AnteprimaAsta>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnteprimaAsta {
        val binding = AstaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val anteprimaAsta = AnteprimaAsta(binding)
        anteprimaAsta.setListeners(parent.context)

        return anteprimaAsta
    }

    override fun getItemCount(): Int {
        return aste.size
    }

    override fun getItemId(position: Int): Long {
        return aste[position]._id
    }

    override fun onBindViewHolder(holder: AnteprimaAsta, position: Int) {
        val currentAsta = aste[position]

        holder.bind(currentAsta, resources)
    }

    override fun onViewDetachedFromWindow(holder: AnteprimaAsta) {
        super.onViewDetachedFromWindow(holder)

        holder.cleanListeners()
    }
}

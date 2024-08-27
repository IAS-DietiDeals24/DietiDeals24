package com.iasdietideals24.dietideals24.utilities.classes.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iasdietideals24.dietideals24.databinding.AstaBinding
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.viewHolders.ViewHolderAnteprimaAsta

class AdapterHome(
    private val aste: Array<AnteprimaAsta>,
    private val resources: Resources
) : RecyclerView.Adapter<ViewHolderAnteprimaAsta>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAnteprimaAsta {
        val binding = AstaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val viewHolderAnteprimaAsta = ViewHolderAnteprimaAsta(binding)
        viewHolderAnteprimaAsta.setListeners(parent.context)

        return viewHolderAnteprimaAsta
    }

    override fun getItemCount(): Int {
        return aste.size
    }

    override fun onBindViewHolder(holder: ViewHolderAnteprimaAsta, position: Int) {
        val currentAsta = aste[position]

        holder.bind(currentAsta, resources)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolderAnteprimaAsta) {
        super.onViewDetachedFromWindow(holder)

        holder.cleanListeners()
    }
}

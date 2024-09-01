package com.iasdietideals24.dietideals24.utilities.classes.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iasdietideals24.dietideals24.databinding.AstaBinding
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.viewHolders.ViewHolderAstaCreata

class AdapterAsteCreate(
    private val aste: Array<AnteprimaAsta>,
    private val resources: Resources
) : RecyclerView.Adapter<ViewHolderAstaCreata>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAstaCreata {
        val binding = AstaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val viewHolderAstaCreata = ViewHolderAstaCreata(binding)
        viewHolderAstaCreata.setListeners(parent.context)

        return viewHolderAstaCreata
    }

    override fun getItemCount(): Int {
        return aste.size
    }

    override fun onBindViewHolder(holder: ViewHolderAstaCreata, position: Int) {
        val currentAsta = aste[position]

        holder.bind(currentAsta, resources)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolderAstaCreata) {
        super.onViewDetachedFromWindow(holder)

        holder.cleanListeners()
    }
}

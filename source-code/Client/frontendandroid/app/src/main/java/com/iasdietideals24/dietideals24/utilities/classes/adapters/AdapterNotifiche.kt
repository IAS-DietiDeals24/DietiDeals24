package com.iasdietideals24.dietideals24.utilities.classes.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.utilities.classes.data.DatiNotifica
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.classes.viewHolders.Notifica
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile
import java.time.LocalDate

class AdapterNotifiche(
    private val notifiche: Array<DatiNotifica>,
    private val resources: Resources
) : RecyclerView.Adapter<Notifica>() {

    private var layoutListener: OnGoToDetails? = null
    private var immagineListener: OnGoToProfile? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Notifica {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notifica, parent, false)

        if (parent.context is OnGoToDetails) {
            layoutListener = parent.context as OnGoToDetails
        }
        if (parent.context is OnGoToProfile) {
            immagineListener = parent.context as OnGoToProfile
        }

        return Notifica(view)
    }

    override fun getItemCount(): Int {
        return notifiche.size
    }

    override fun onBindViewHolder(holder: Notifica, position: Int) {
        val currentNotifica = notifiche[position]

        holder.mittente.text = currentNotifica._mittente
        holder.immagineMittente.load(currentNotifica._immagineMittente) {
            crossfade(true)
        }
        holder.messaggio.text = currentNotifica._messaggio

        val tempoFa = when {
            LocalDate.now() == currentNotifica._dataInvio -> currentNotifica._oraInvio.toString()
            else -> currentNotifica._dataInvio.toLocalStringShort() + " " + currentNotifica._oraInvio.toString()
        }

        holder.dataInvio.text = resources.getString(R.string.placeholder, tempoFa)

        holder.layout.setOnClickListener {
            layoutListener?.onGoToDetails(currentNotifica._idAsta, this::class)
        }

        holder.immagineMittente.setOnClickListener {
            immagineListener?.onGoToProfile(currentNotifica._idMittente, this::class)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        layoutListener = null
        immagineListener = null
    }
}

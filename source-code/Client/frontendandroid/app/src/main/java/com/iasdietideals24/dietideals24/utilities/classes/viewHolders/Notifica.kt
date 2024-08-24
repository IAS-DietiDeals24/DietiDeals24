package com.iasdietideals24.dietideals24.utilities.classes.viewHolders

import android.content.Context
import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.NotificaBinding
import com.iasdietideals24.dietideals24.utilities.classes.data.DatiNotifica
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile
import java.time.LocalDate

class Notifica(binding: NotificaBinding) : RecyclerView.ViewHolder(binding.root) {

    private val binding = binding

    private var layoutListener: OnGoToDetails? = null
    private var immagineListener: OnGoToProfile? = null

    fun setListeners(context: Context) {
        if (context is OnGoToDetails) {
            layoutListener = context
        }
        if (context is OnGoToProfile) {
            immagineListener = context
        }
    }

    fun bind(currentNotifica: DatiNotifica, resources: Resources) {
        binding.notificaNome.text = currentNotifica._mittente
        if (currentNotifica._immagineMittente.isNotEmpty())
            binding.notificaImmagine.load(currentNotifica._immagineMittente) {
                crossfade(true)
            }
        binding.notificaTesto.text = currentNotifica._messaggio

        val tempoFa = when {
            LocalDate.now() == currentNotifica._dataInvio -> currentNotifica._oraInvio.toString()
            else -> currentNotifica._dataInvio.toLocalStringShort() + " " + currentNotifica._oraInvio.toString()
        }

        binding.notificaTempo.text = resources.getString(R.string.placeholder, tempoFa)

        binding.notificaLinearLayout1.setOnClickListener {
            layoutListener?.onGoToDetails(currentNotifica._idAsta, this::class)
        }

        binding.notificaImmagine.setOnClickListener {
            immagineListener?.onGoToProfile(currentNotifica._idMittente, this::class)
        }
    }

    fun cleanListeners() {
        layoutListener = null
        immagineListener = null
    }
}
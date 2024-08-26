package com.iasdietideals24.dietideals24.utilities.classes.viewHolders

import android.content.Context
import android.content.res.Resources
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.NotificaBinding
import com.iasdietideals24.dietideals24.utilities.classes.data.Notifica
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile
import java.time.LocalDate

class ViewHolderNotifica(private val binding: NotificaBinding) :
    RecyclerView.ViewHolder(binding.root) {

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

    fun bind(currentNotifica: Notifica, resources: Resources) {
        binding.notificaNome.text = currentNotifica.mittente
        if (currentNotifica.immagineMittente.isNotEmpty()) {
            binding.notificaImmagine.load(currentNotifica.immagineMittente) {
                crossfade(true)
            }
            binding.notificaImmagine.scaleType = ImageView.ScaleType.CENTER_CROP
        }
        binding.notificaTesto.text = currentNotifica.messaggio

        val tempoFa = when {
            LocalDate.now() == currentNotifica.dataInvio -> currentNotifica.oraInvio.toString()
            else -> currentNotifica.dataInvio.toLocalStringShort() + " " + currentNotifica.oraInvio.toString()
        }

        binding.notificaTempo.text = resources.getString(R.string.placeholder, tempoFa)

        binding.notificaLinearLayout1.setOnClickListener {
            layoutListener?.onGoToDetails(currentNotifica.idAsta, this::class)
        }

        binding.notificaImmagine.setOnClickListener {
            immagineListener?.onGoToProfile(currentNotifica.idMittente, this::class)
        }
    }

    fun cleanListeners() {
        layoutListener = null
        immagineListener = null
    }
}
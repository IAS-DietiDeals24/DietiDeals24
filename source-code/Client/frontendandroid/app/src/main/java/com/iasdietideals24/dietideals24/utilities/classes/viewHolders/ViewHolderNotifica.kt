package com.iasdietideals24.dietideals24.utilities.classes.viewHolders

import android.content.Context
import android.content.res.Resources
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.NotificaBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.classes.Logger
import com.iasdietideals24.dietideals24.utilities.classes.data.Notifica
import com.iasdietideals24.dietideals24.utilities.classes.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.interfaces.OnGoToProfile
import java.time.LocalDate

class ViewHolderNotifica(private val binding: NotificaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private var listenerGoToDetails: OnGoToDetails? = null
    private var listenerGoToProfile: OnGoToProfile? = null

    @UIBuilder
    fun setListeners(context: Context) {
        if (context is OnGoToDetails) {
            listenerGoToDetails = context
        }
        if (context is OnGoToProfile) {
            listenerGoToProfile = context
        }
    }

    @UIBuilder
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
            Logger.log("Showing auction details")

            listenerGoToDetails?.onGoToDetails(currentNotifica.idAsta, this::class)
        }

        binding.notificaImmagine.setOnClickListener {
            Logger.log("Showing user profile")

            listenerGoToProfile?.onGoToProfile(currentNotifica.idMittente, this::class)
        }
    }

    fun cleanListeners() {
        listenerGoToDetails = null
        listenerGoToProfile = null
    }
}
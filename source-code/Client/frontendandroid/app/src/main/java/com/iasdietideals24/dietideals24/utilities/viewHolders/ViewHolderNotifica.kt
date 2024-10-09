package com.iasdietideals24.dietideals24.utilities.viewHolders

import android.content.Context
import android.content.res.Resources
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.NotificaBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.data.Notifica
import com.iasdietideals24.dietideals24.utilities.data.Profilo
import com.iasdietideals24.dietideals24.utilities.dto.ProfiloDto
import com.iasdietideals24.dietideals24.utilities.kscripts.Logger
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToProfile
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.repositories.ProfiloRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import java.time.LocalDate

class ViewHolderNotifica(private val binding: NotificaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // Scope
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // Repositories
    private val profiloRepository: ProfiloRepository by inject(ProfiloRepository::class.java)

    // Listeners
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
        scope.launch {
            val mittente: Profilo =
                withContext(Dispatchers.IO) { recuperaMittente(currentNotifica).toProfilo() }

            binding.notificaNome.text = mittente.nomeUtente

            if (mittente.immagineProfilo.isNotEmpty()) {
                binding.notificaImmagine.load(mittente.immagineProfilo) {
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

                listenerGoToDetails?.onGoToDetails(
                    currentNotifica.idAsta,
                    currentNotifica.tipoAsta,
                    this::class
                )
            }

            binding.notificaImmagine.setOnClickListener {
                Logger.log("Showing user profile")

                listenerGoToProfile?.onGoToProfile(currentNotifica.idMittente, this::class)
            }
        }
    }

    private suspend fun recuperaMittente(currentNotifica: Notifica): ProfiloDto {
        return profiloRepository.caricaProfiloDaAccount(currentNotifica.idMittente)
    }

    fun cleanListeners() {
        listenerGoToDetails = null
        listenerGoToProfile = null
        scope.cancel()
    }
}
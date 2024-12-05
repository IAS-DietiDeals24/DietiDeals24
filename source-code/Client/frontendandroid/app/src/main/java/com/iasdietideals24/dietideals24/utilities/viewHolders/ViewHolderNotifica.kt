package com.iasdietideals24.dietideals24.utilities.viewHolders

import android.content.Context
import android.content.res.Resources
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.NotificaBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.data.Asta
import com.iasdietideals24.dietideals24.utilities.data.Notifica
import com.iasdietideals24.dietideals24.utilities.data.Profilo
import com.iasdietideals24.dietideals24.utilities.dto.AstaDto
import com.iasdietideals24.dietideals24.utilities.dto.CompratoreDto
import com.iasdietideals24.dietideals24.utilities.dto.ProfiloDto
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToProfile
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.kscripts.toStringShort
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.repositories.CompratoreRepository
import com.iasdietideals24.dietideals24.utilities.repositories.ProfiloRepository
import com.iasdietideals24.dietideals24.utilities.repositories.VenditoreRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import com.iasdietideals24.dietideals24.utilities.tools.Logger
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
    private val compratoreRepository: CompratoreRepository by inject(CompratoreRepository::class.java)
    private val venditoreRepository: VenditoreRepository by inject(VenditoreRepository::class.java)
    private val silenziosaRepository: AstaSilenziosaRepository by inject(AstaSilenziosaRepository::class.java)
    private val tempoFissoRepository: AstaTempoFissoRepository by inject(AstaTempoFissoRepository::class.java)
    private val inversaRepository: AstaInversaRepository by inject(AstaInversaRepository::class.java)

    // Logger
    private val logger: Logger by inject(Logger::class.java)

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

            val asta: Asta =
                withContext(Dispatchers.IO) { caricaAsta(currentNotifica).toAsta() }

            binding.notificaTesto.text =
                resources.getString(R.string.testoNotifica, currentNotifica.messaggio, asta.nome)

            val tempoFa = when {
                LocalDate.now() == currentNotifica.dataInvio -> currentNotifica.oraInvio.toStringShort()
                else -> currentNotifica.dataInvio.toLocalStringShort() + " " + currentNotifica.oraInvio.toStringShort()
            }

            binding.notificaTempo.text = resources.getString(R.string.placeholder, tempoFa)

            binding.notificaLinearLayout1.setOnClickListener {
                scope.launch {
                    withContext(Dispatchers.IO) {
                        logger.scriviLog("Showing auction details")
                    }
                }

                listenerGoToDetails?.onGoToDetails(
                    currentNotifica.idAsta,
                    currentNotifica.tipoAsta,
                    ViewHolderNotifica::class
                )
            }

            binding.notificaImmagine.setOnClickListener {
                scope.launch {
                    withContext(Dispatchers.IO) {
                        logger.scriviLog("Showing user profile")
                    }
                }

                listenerGoToProfile?.onGoToProfile(
                    currentNotifica.idMittente,
                    ViewHolderNotifica::class
                )
            }
        }
    }

    private suspend fun recuperaMittente(currentNotifica: Notifica): ProfiloDto {
        val account = when (CurrentUser.tipoAccount) {
            TipoAccount.VENDITORE -> compratoreRepository.caricaAccountCompratore(currentNotifica.idMittente)
            TipoAccount.COMPRATORE -> venditoreRepository.caricaAccountVenditore(currentNotifica.idMittente)
            else -> CompratoreDto()
        }
        return profiloRepository.caricaProfilo(account.profiloShallow.nomeUtente)
    }

    private suspend fun caricaAsta(currentNotifica: Notifica): AstaDto {
        return when (currentNotifica.tipoAsta) {
            TipoAsta.SILENZIOSA -> silenziosaRepository.caricaAstaSilenziosa(currentNotifica.idAsta)
            TipoAsta.TEMPO_FISSO -> tempoFissoRepository.caricaAstaTempoFisso(currentNotifica.idAsta)
            TipoAsta.INVERSA -> inversaRepository.caricaAstaInversa(currentNotifica.idAsta)
        }
    }

    fun cleanListeners() {
        listenerGoToDetails = null
        listenerGoToProfile = null
        scope.cancel()
    }
}
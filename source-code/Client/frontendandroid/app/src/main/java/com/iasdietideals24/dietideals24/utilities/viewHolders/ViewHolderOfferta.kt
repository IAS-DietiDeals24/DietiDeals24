package com.iasdietideals24.dietideals24.utilities.viewHolders

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.OffertaBinding
import com.iasdietideals24.dietideals24.utilities.annotations.EventHandler
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.data.OffertaRicevuta
import com.iasdietideals24.dietideals24.utilities.data.Profilo
import com.iasdietideals24.dietideals24.utilities.dto.OffertaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.dto.ProfiloDto
import com.iasdietideals24.dietideals24.utilities.enumerations.StatoOfferta
import com.iasdietideals24.dietideals24.utilities.kscripts.Logger
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToProfile
import com.iasdietideals24.dietideals24.utilities.kscripts.OnRefresh
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.ProfiloRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import java.time.LocalDate

class ViewHolderOfferta(private val binding: OffertaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // Scope
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // Repositories
    private val silenziosaRepository: OffertaSilenziosaRepository by inject(
        OffertaSilenziosaRepository::class.java
    )
    private val profiloRepository: ProfiloRepository by inject(ProfiloRepository::class.java)

    // Listeners
    private var immagineListener: OnGoToProfile? = null
    private var refreshListener: OnRefresh? = null

    @UIBuilder
    fun setListeners(context: Context) {
        if (context is OnGoToProfile) {
            immagineListener = context
        }
        if (context is OnRefresh) {
            refreshListener = context
        }
    }

    @UIBuilder
    fun bind(currentOfferta: OffertaRicevuta, resources: Resources) {
        scope.launch {
            val offerente: Profilo =
                withContext(Dispatchers.IO) { recuperaOfferente(currentOfferta).toProfilo() }

            binding.offertaNome.text = offerente.nomeUtente

            if (offerente.immagineProfilo.isNotEmpty()) {
                binding.offertaImmagine.load(offerente.immagineProfilo) {
                    crossfade(true)
                }
                binding.offertaImmagine.scaleType = ImageView.ScaleType.CENTER_CROP
            }

            binding.offertaValoreOfferta.text =
                resources.getString(R.string.placeholder_prezzo, currentOfferta.offerta.toString())

            val tempoFa = when {
                LocalDate.now() == currentOfferta.dataInvio -> currentOfferta.oraInvio.toString()
                else -> currentOfferta.dataInvio.toLocalStringShort() + " " + currentOfferta.oraInvio.toString()
            }

            binding.offertaTempo.text = resources.getString(R.string.placeholder, tempoFa)

            binding.offertaImmagine.setOnClickListener {
                Logger.log("Showing user profile")

                immagineListener?.onGoToProfile(currentOfferta.idOfferente, this::class)
            }

            if (currentOfferta.stato == StatoOfferta.PENDING) {

                binding.offertaPulsanteAccetta.setOnClickListener {
                    MaterialAlertDialogBuilder(itemView.context, R.style.Dialog)
                        .setTitle(R.string.offerta_confermaAccetta)
                        .setPositiveButton(R.string.ok) { _, _ ->
                            Logger.log("Accepting auction bid")

                            scope.launch {
                                clickAccetta(
                                    currentOfferta.idAsta,
                                    currentOfferta.idOfferta,
                                    resources,
                                    currentOfferta
                                )
                            }
                        }
                        .setNegativeButton(R.string.annulla) { _, _ -> }
                        .show()
                }

                binding.offertaPulsanteRifiuta.setOnClickListener {
                    MaterialAlertDialogBuilder(itemView.context, R.style.Dialog)
                        .setTitle(R.string.offerta_confermaRifiuto)
                        .setPositiveButton(R.string.ok) { _, _ ->
                            Logger.log("Rejecting auction bid")

                            scope.launch {
                                clickRifiuta(
                                    currentOfferta.idAsta,
                                    currentOfferta.idOfferta,
                                    resources,
                                    currentOfferta
                                )
                            }
                        }
                        .setNegativeButton(R.string.annulla) { _, _ -> }
                        .show()
                }
            } else {
                if (currentOfferta.stato == StatoOfferta.REJECTED)
                    binding.offertaLinearLayout1.setBackgroundColor(
                        resources.getColor(
                            R.color.grigioChiaro,
                            null
                        )
                    )

                binding.offertaPulsanteAccetta.visibility = View.GONE
                binding.offertaPulsanteRifiuta.visibility = View.GONE
            }
        }
    }

    private suspend fun recuperaOfferente(currentOfferta: OffertaRicevuta): ProfiloDto {
        return profiloRepository.caricaProfiloDaAccount(currentOfferta.idOfferente)
    }

    @EventHandler
    private suspend fun clickAccetta(
        idAsta: Long,
        idOfferta: Long,
        resources: Resources,
        currentOfferta: OffertaRicevuta
    ) {
        val patchOfferta = OffertaSilenziosaDto(
            currentOfferta.idOfferta,
            null,
            null,
            null,
            null,
            StatoOfferta.ACCEPTED.name,
            null
        )

        val returned: Boolean = withContext(Dispatchers.IO) {
            silenziosaRepository.accettaOfferta(
                patchOfferta,
                idOfferta
            )
        }

        when (returned) {
            true -> {
                Snackbar.make(
                    itemView,
                    R.string.offerta_offertaAccettata,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                refreshListener?.onRefresh(idAsta, currentOfferta.tipoAsta, this::class)
            }

            false -> {
                Snackbar.make(
                    itemView,
                    R.string.offerta_offertaNonAccettata,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    @EventHandler
    private suspend fun clickRifiuta(
        idAsta: Long,
        idOfferta: Long,
        resources: Resources,
        currentOfferta: OffertaRicevuta
    ) {
        val patchOfferta = OffertaSilenziosaDto(
            currentOfferta.idOfferta,
            null,
            null,
            null,
            null,
            StatoOfferta.REJECTED.name,
            null
        )

        val returned: Boolean =
            withContext(Dispatchers.IO) {
                silenziosaRepository.rifiutaOfferta(
                    patchOfferta,
                    idOfferta
                )
            }

        when (returned) {
            true -> {
                Snackbar.make(
                    itemView,
                    R.string.offerta_offertaRifiutata,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()

                refreshListener?.onRefresh(idAsta, currentOfferta.tipoAsta, this::class)
            }

            false -> {
                Snackbar.make(
                    itemView,
                    R.string.offerta_offertaNonRifiutata,
                    Snackbar.LENGTH_SHORT
                )
                    .setBackgroundTint(resources.getColor(R.color.blu, null))
                    .setTextColor(resources.getColor(R.color.grigio, null))
                    .show()
            }
        }
    }

    fun cleanListeners() {
        immagineListener = null
        refreshListener = null
        scope.cancel()
    }
}
package com.iasdietideals24.dietideals24.utilities.viewHolders

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.AstaBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.data.AnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.dto.OffertaDto
import com.iasdietideals24.dietideals24.utilities.enumerations.StatoAsta
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.tools.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import java.math.BigDecimal

class ViewHolderAnteprimaAsta(private val binding: AstaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // Scope
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // Repository
    private val offertaInversaRepository: OffertaInversaRepository by inject(
        OffertaInversaRepository::class.java
    )
    private val offertaTempoFissoRepository: OffertaTempoFissoRepository by inject(
        OffertaTempoFissoRepository::class.java
    )

    // Logger
    private val logger: Logger by inject(Logger::class.java)

    // Listeners
    private var listenerGoToDetails: OnGoToDetails? = null

    @UIBuilder
    fun setListeners(context: Context) {
        if (context is OnGoToDetails) {
            listenerGoToDetails = context
        }
    }

    @UIBuilder
    fun bind(currentAsta: AnteprimaAsta, resources: Resources) {
        when (currentAsta.tipoAsta) {
            TipoAsta.INVERSA -> {
                binding.astaTipo.text = resources.getString(R.string.tipoAsta_astaInversa)
                binding.astaMessaggio.text =
                    resources.getString(R.string.dettagliAsta_testoOfferta2)
            }

            TipoAsta.SILENZIOSA -> {
                binding.astaTipo.text = resources.getString(R.string.tipoAsta_astaSilenziosa)
                binding.astaMessaggio.text =
                    resources.getString(R.string.dettagliAsta_testoOfferta1)
            }

            TipoAsta.TEMPO_FISSO -> {
                binding.astaTipo.text = resources.getString(R.string.tipoAsta_astaTempoFisso)
                binding.astaMessaggio.text =
                    resources.getString(R.string.dettagliAsta_testoOfferta1)
            }
        }

        if (currentAsta.stato == StatoAsta.CLOSED) {
            binding.astaDataScadenza.text = resources.getString(R.string.astaScaduta)
            binding.astaOraScadenza.visibility = View.GONE
            binding.astaScadenza.visibility = View.GONE
        } else {
            binding.astaDataScadenza.text =
                currentAsta.dataScadenza.toLocalStringShort()
            binding.astaOraScadenza.text = currentAsta.oraScadenza.toString()

        }

        if (currentAsta.foto.isNotEmpty())
            binding.astaImmagine.load(currentAsta.foto) {
                crossfade(true)
            }

        binding.astaNome.text = currentAsta.nome
        binding.astaModificaAsta.visibility = View.GONE
        binding.astaEliminaAsta.visibility = View.GONE
        binding.astaElencoOfferte.visibility = View.GONE

        binding.astaLinearLayout3.setOnClickListener {
            scope.launch {
                withContext(Dispatchers.IO) {
                    logger.scriviLog("Showing auction details")
                }
            }

            listenerGoToDetails?.onGoToDetails(currentAsta.id, currentAsta.tipoAsta, this::class)
        }

        scope.launch {
            val valoreOfferta: String = when (currentAsta.tipoAsta) {
                TipoAsta.TEMPO_FISSO -> {
                    val offerta = recuperaOfferta(currentAsta).toOfferta().offerta
                    if (offerta == BigDecimal("0.00"))
                        currentAsta.prezzo.toString()
                    else
                        offerta.toString()
                }

                TipoAsta.INVERSA -> {
                    withContext(Dispatchers.IO) {
                        val offerta = recuperaOfferta(currentAsta).toOfferta().offerta
                        if (offerta == BigDecimal("0.00"))
                            currentAsta.prezzo.toString()
                        else
                            offerta.toString()
                    }
                }

                else -> "???"
            }

            binding.astaOfferta.text =
                resources.getString(R.string.placeholder_prezzo, valoreOfferta)
        }
    }

    private suspend fun recuperaOfferta(currentAsta: AnteprimaAsta): OffertaDto {
        return if (currentAsta.tipoAsta == TipoAsta.INVERSA) {
            offertaInversaRepository.recuperaOffertaPiuBassa(currentAsta.id)
        } else {
            offertaTempoFissoRepository.recuperaOffertaPiuAlta(currentAsta.id)
        }
    }

    fun cleanListeners() {
        listenerGoToDetails = null
        scope.cancel()
    }
}
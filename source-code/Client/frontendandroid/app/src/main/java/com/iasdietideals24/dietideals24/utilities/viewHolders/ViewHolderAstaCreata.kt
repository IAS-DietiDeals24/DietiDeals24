package com.iasdietideals24.dietideals24.utilities.viewHolders

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iasdietideals24.dietideals24.R
import com.iasdietideals24.dietideals24.databinding.AstaBinding
import com.iasdietideals24.dietideals24.utilities.annotations.UIBuilder
import com.iasdietideals24.dietideals24.utilities.data.AnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.dto.OffertaDto
import com.iasdietideals24.dietideals24.utilities.enumerations.StatoAsta
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import com.iasdietideals24.dietideals24.utilities.kscripts.OnEditButton
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToBids
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.kscripts.OnRefresh
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaTempoFissoRepository
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

class ViewHolderAstaCreata(private val binding: AstaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    // Scope
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    // Repositories
    private val offertaInversaRepository: OffertaInversaRepository by inject(
        OffertaInversaRepository::class.java
    )
    private val offertaTempoFissoRepository: OffertaTempoFissoRepository by inject(
        OffertaTempoFissoRepository::class.java
    )
    private val astaInversaRepository: AstaInversaRepository by inject(AstaInversaRepository::class.java)
    private val astaSilenziosaRepository: AstaSilenziosaRepository by inject(
        AstaSilenziosaRepository::class.java
    )
    private val astaTempoFissoRepository: AstaTempoFissoRepository by inject(
        AstaTempoFissoRepository::class.java
    )

    // Logger
    private val logger: Logger by inject(Logger::class.java)

    // Listeners
    private var listenerGoToDetails: OnGoToDetails? = null
    private var listenerEditButton: OnEditButton? = null
    private var listenerRefresh: OnRefresh? = null
    private var listenerGoToBids: OnGoToBids? = null

    @UIBuilder
    fun setListeners(context: Context) {
        if (context is OnGoToDetails) {
            listenerGoToDetails = context
        }
        if (context is OnEditButton) {
            listenerEditButton = context
        }
        if (context is OnRefresh) {
            listenerRefresh = context
        }
        if (context is OnGoToBids) {
            listenerGoToBids = context
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

            if (currentAsta.foto.isNotEmpty())
                binding.astaImmagine.load(currentAsta.foto) {
                    crossfade(true)
                }
        }

        if (currentAsta.foto.isNotEmpty())
            binding.astaImmagine.load(currentAsta.foto) {
                crossfade(true)
            }

        binding.astaNome.text = currentAsta.nome

        binding.astaLinearLayout3.setOnClickListener {
            scope.launch {
                withContext(Dispatchers.IO) {
                    logger.scriviLog("Showing auction details")
                }
            }

            listenerGoToDetails?.onGoToDetails(currentAsta.id, currentAsta.tipoAsta, this::class)
        }

        binding.astaModificaAsta.setOnClickListener {
            scope.launch {
                withContext(Dispatchers.IO) {
                    logger.scriviLog("Editing auction")
                }
            }

            listenerEditButton?.onEditButton(currentAsta.id, currentAsta.tipoAsta, this::class)
        }

        binding.astaEliminaAsta.setOnClickListener {
            MaterialAlertDialogBuilder(itemView.context, R.style.Dialog)
                .setTitle(R.string.elimina_titoloConfermaElimina)
                .setMessage(R.string.elimina_testoConfermaElimina)
                .setPositiveButton(R.string.ok) { _, _ ->
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            logger.scriviLog("Deleting auction")
                            clickConferma(currentAsta)
                        }

                        listenerRefresh?.onRefresh(
                            currentAsta.id,
                            currentAsta.tipoAsta,
                            ViewHolderAstaCreata::class
                        )
                    }
                }
                .setNegativeButton(R.string.annulla) { _, _ -> }
                .show()
        }

        binding.astaElencoOfferte.setOnClickListener {
            scope.launch {
                withContext(Dispatchers.IO) {
                    logger.scriviLog("Showing auction bids")
                }
            }

            listenerGoToBids?.onGoToBids(currentAsta.id, currentAsta.tipoAsta, this::class)
        }

        scope.launch {
            val valoreOfferta = withContext(Dispatchers.IO) {
                recuperaOfferta(currentAsta).toOfferta().offerta.toString()
            }

            binding.astaOfferta.text = when (currentAsta.tipoAsta) {
                TipoAsta.TEMPO_FISSO -> resources.getString(
                    R.string.placeholder_prezzo,
                    valoreOfferta
                )

                TipoAsta.INVERSA -> if (valoreOfferta == BigDecimal("0.00").toString())
                    resources.getString(R.string.placeholder_prezzo, currentAsta.prezzo.toString())
                else
                    resources.getString(R.string.placeholder_prezzo, valoreOfferta)

                else -> resources.getString(R.string.placeholder_prezzo, "???")
            }
        }
    }

    private suspend fun clickConferma(currentAsta: AnteprimaAsta) {
        when (currentAsta.tipoAsta) {
            TipoAsta.INVERSA -> astaInversaRepository.eliminaAstaInversa(currentAsta.id)
            TipoAsta.TEMPO_FISSO -> astaTempoFissoRepository.eliminaAstaTempoFisso(currentAsta.id)
            TipoAsta.SILENZIOSA -> astaSilenziosaRepository.eliminaAstaSilenziosa(currentAsta.id)
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
        listenerEditButton = null
        listenerRefresh = null
        listenerGoToBids = null
        scope.cancel()
    }
}
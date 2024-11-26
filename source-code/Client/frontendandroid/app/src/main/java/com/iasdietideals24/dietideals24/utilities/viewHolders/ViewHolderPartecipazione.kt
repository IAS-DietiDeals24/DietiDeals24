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
import com.iasdietideals24.dietideals24.utilities.data.Offerta
import com.iasdietideals24.dietideals24.utilities.dto.OffertaDto
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import com.iasdietideals24.dietideals24.utilities.tools.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class ViewHolderPartecipazione(private val binding: AstaBinding) :
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
    private val offertaSilenziosaRepository: OffertaSilenziosaRepository by inject(
        OffertaSilenziosaRepository::class.java
    )

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
            }

            TipoAsta.SILENZIOSA -> {
                binding.astaTipo.text = resources.getString(R.string.tipoAsta_astaSilenziosa)
            }

            TipoAsta.TEMPO_FISSO -> {
                binding.astaTipo.text = resources.getString(R.string.tipoAsta_astaTempoFisso)
            }
        }

        binding.astaMessaggio.text = resources.getString(R.string.asta_messaggioPartecipazione)
        binding.astaDataScadenza.text =
            currentAsta.dataScadenza.toLocalStringShort()
        binding.astaOraScadenza.text = currentAsta.oraScadenza.toString()

        if (currentAsta.foto.isNotEmpty())
            binding.astaImmagine.load(currentAsta.foto) {
                crossfade(true)
            }

        binding.astaNome.text = currentAsta.nome
        binding.astaModificaAsta.visibility = View.GONE
        binding.astaEliminaAsta.visibility = View.GONE
        binding.astaElencoOfferte.visibility = View.GONE

        binding.astaLinearLayout3.setOnClickListener {
            Logger.log("Showing auction details")

            listenerGoToDetails?.onGoToDetails(currentAsta.id, currentAsta.tipoAsta, this::class)
        }

        scope.launch {
            val valoreOfferta: Offerta =
                withContext(Dispatchers.IO) { recuperaOffertaPersonale(currentAsta).toOfferta() }

            binding.astaOfferta.text =
                resources.getString(R.string.placeholder_prezzo, valoreOfferta.offerta)
        }
    }

    private suspend fun recuperaOffertaPersonale(currentAsta: AnteprimaAsta): OffertaDto {
        return when (currentAsta.tipoAsta) {
            TipoAsta.INVERSA -> {
                offertaInversaRepository.recuperaOffertaPersonalePiuBassaInversa(
                    currentAsta.id,
                    CurrentUser.id
                )
            }

            TipoAsta.TEMPO_FISSO -> {
                offertaTempoFissoRepository.recuperaOffertaPersonalePiuAltaTempoFisso(
                    currentAsta.id,
                    CurrentUser.id
                )
            }

            else -> {
                offertaSilenziosaRepository.recuperaOffertaPersonalePiuAltaSilenziosa(
                    currentAsta.id,
                    CurrentUser.id
                )
            }
        }
    }

    fun cleanListeners() {
        listenerGoToDetails = null
        scope.cancel()
    }
}
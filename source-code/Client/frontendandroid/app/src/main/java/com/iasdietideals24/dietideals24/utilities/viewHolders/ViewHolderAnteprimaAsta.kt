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
import com.iasdietideals24.dietideals24.utilities.kscripts.Logger
import com.iasdietideals24.dietideals24.utilities.kscripts.OnGoToDetails
import com.iasdietideals24.dietideals24.utilities.kscripts.toLocalStringShort
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaTempoFissoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

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

        binding.astaDataScadenza.text =
            currentAsta.dataScadenza.toLocalStringShort()
        binding.astaOraScadenza.text = currentAsta.oraScadenza.toString()

        if (currentAsta.foto.isNotEmpty())
            binding.astaImmagine.load(currentAsta.foto) {
                crossfade(true)
            }

        binding.astaNome.text = currentAsta.nome

        binding.astaOfferta.text = resources.getString(
            R.string.placeholder_prezzo,
            if (currentAsta.tipoAsta != TipoAsta.SILENZIOSA) {
                scope.launch {
                    val offerta: Offerta =
                        withContext(Dispatchers.IO) { recuperaOfferta(currentAsta).toOfferta() }

                    offerta.toString()
                }
            } else
                "???"
        )

        binding.astaModificaAsta.visibility = View.GONE
        binding.astaEliminaAsta.visibility = View.GONE
        binding.astaElencoOfferte.visibility = View.GONE

        binding.astaLinearLayout3.setOnClickListener {
            Logger.log("Showing auction details")

            listenerGoToDetails?.onGoToDetails(currentAsta.id, currentAsta.tipoAsta, this::class)
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
package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.iasdietideals24.dietideals24.utilities.dto.AstaDto
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.flow.Flow

class ModelPartecipazioni(
    private val inverseRepository: AstaInversaRepository,
    private val tempoFissoRepository: AstaTempoFissoRepository,
    private val silenziosaRepository: AstaSilenziosaRepository
) : ViewModel() {

    private val flowInverse by lazy {
        inverseRepository.recuperaPartecipazioniInverse(CurrentUser.id)
    }

    private val flowTempoFisso by lazy {
        tempoFissoRepository.recuperaPartecipazioniTempoFisso(CurrentUser.id)
    }

    private val flowSilenziose by lazy {
        silenziosaRepository.recuperaPartecipazioniSilenziose(CurrentUser.id)
    }

    @Suppress("UNCHECKED_CAST")
    fun getAsteInverseFlows(): Flow<PagingData<AstaDto>> {
        return flowInverse as Flow<PagingData<AstaDto>>
    }

    @Suppress("UNCHECKED_CAST")
    fun getAsteTempoFissoFlows(): Flow<PagingData<AstaDto>> {
        return flowTempoFisso as Flow<PagingData<AstaDto>>
    }

    @Suppress("UNCHECKED_CAST")
    fun getAsteSilenzioseFlows(): Flow<PagingData<AstaDto>> {
        return flowSilenziose as Flow<PagingData<AstaDto>>
    }
}
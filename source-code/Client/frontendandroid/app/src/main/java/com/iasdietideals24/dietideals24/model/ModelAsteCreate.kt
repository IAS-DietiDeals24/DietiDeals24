package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iasdietideals24.dietideals24.utilities.dto.AstaDto
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.flow.Flow

class ModelAsteCreate(
    private val inverseRepository: AstaInversaRepository,
    private val tempoFissoRepository: AstaTempoFissoRepository,
    private val silenziosaRepository: AstaSilenziosaRepository
) : ViewModel() {

    private val flowInverse by lazy {
        inverseRepository.recuperaAsteCreateInverse(CurrentUser.id).cachedIn(viewModelScope)
    }

    private val flowTempoFisso by lazy {
        tempoFissoRepository.recuperaAsteCreateTempoFisso(CurrentUser.id).cachedIn(viewModelScope)
    }

    private val flowSilenziose by lazy {
        silenziosaRepository.recuperaAsteCreateSilenziose(CurrentUser.id).cachedIn(viewModelScope)
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
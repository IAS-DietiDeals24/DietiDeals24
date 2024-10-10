package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iasdietideals24.dietideals24.utilities.dto.AstaDto
import com.iasdietideals24.dietideals24.utilities.dto.CategoriaAstaDto
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.repositories.CategoriaAstaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.merge

class ModelHome(
    private val inverseRepository: AstaInversaRepository,
    private val tempoFissoRepository: AstaTempoFissoRepository,
    private val silenziosaRepository: AstaSilenziosaRepository,
    private val categoriaAstaRepository: CategoriaAstaRepository
) : ViewModel() {

    private val _searchText = MutableStateFlow("")

    val searchText: MutableStateFlow<String> get() = _searchText

    private var _filter = MutableStateFlow("")

    val filter: MutableStateFlow<String> get() = _filter

    private val flowInverseTutte by lazy {
        inverseRepository.recuperaAsteInverse().cachedIn(viewModelScope)
    }

    private val flowTempoFissoTutte by lazy {
        tempoFissoRepository.recuperaAsteTempoFisso().cachedIn(viewModelScope)
    }

    private val flowSilenzioseTutte by lazy {
        silenziosaRepository.recuperaAsteSilenziose().cachedIn(viewModelScope)
    }

    private val flowCategorieAsta by lazy {
        categoriaAstaRepository.recuperaCategorieAsta().cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val flowInverseRicerca = searchText.flatMapLatest { _ ->
        inverseRepository.ricercaAsteInverse(searchText.value, filter.value)
            .cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val flowTempoFissoRicerca = searchText.flatMapLatest { _ ->
        tempoFissoRepository.ricercaAsteTempoFisso(searchText.value, filter.value)
            .cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val flowSilenzioseRicerca = searchText.flatMapLatest { _ ->
        silenziosaRepository.ricercaAsteSilenziose(searchText.value, filter.value)
            .cachedIn(viewModelScope)
    }

    @Suppress("UNCHECKED_CAST")
    fun getVenditoreFlows(): Flow<PagingData<AstaDto>> {
        return (
                if (searchText.value.isEmpty() && filter.value.isEmpty())
                    flowInverseTutte
                else
                    flowInverseRicerca
                ) as Flow<PagingData<AstaDto>>
    }

    @Suppress("UNCHECKED_CAST")
    fun getCompratoreFlows(): Flow<PagingData<AstaDto>> {
        return (
                if (searchText.value.isEmpty() && filter.value.isEmpty())
                    merge(flowTempoFissoTutte, flowSilenzioseTutte)
                else
                    merge(flowTempoFissoRicerca, flowSilenzioseRicerca)
                ) as Flow<PagingData<AstaDto>>
    }

    @Suppress("UNCHECKED_CAST")
    fun getFlows(): Flow<PagingData<AstaDto>> {
        return (
                if (searchText.value.isEmpty() && filter.value.isEmpty())
                    merge(flowInverseTutte, flowTempoFissoTutte, flowSilenzioseTutte)
                else
                    merge(flowInverseRicerca, flowTempoFissoRicerca, flowSilenzioseRicerca)
                ) as Flow<PagingData<AstaDto>>
    }

    fun getFlowsCategorieAsta(): Flow<PagingData<CategoriaAstaDto>> {
        return flowCategorieAsta
    }
}
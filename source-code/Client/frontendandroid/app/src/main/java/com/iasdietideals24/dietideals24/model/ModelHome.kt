package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iasdietideals24.dietideals24.utilities.dto.AstaDto
import com.iasdietideals24.dietideals24.utilities.paging.AstaInversaPagingSource
import com.iasdietideals24.dietideals24.utilities.paging.AstaSilenziosaPagingSource
import com.iasdietideals24.dietideals24.utilities.paging.AstaTempoFissoPagingSource
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaTempoFissoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.merge
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.get
import org.koin.java.KoinJavaComponent.inject

class ModelHome : ViewModel() {
    private val _searchText = MutableStateFlow("")

    val searchText: MutableStateFlow<String> get() = _searchText

    private var _filter = MutableStateFlow("")

    val filter: MutableStateFlow<String> get() = _filter

    private val pagingSourceInverseTutte: AstaInversaPagingSource by inject(
        AstaInversaPagingSource::class.java
    ) { parametersOf("", "", "", AstaInversaRepository.ApiCall.TUTTE) }

    private val pagingSourceTempoFissoTutte: AstaTempoFissoPagingSource by inject(
        AstaTempoFissoPagingSource::class.java
    ) { parametersOf("", "", "", AstaTempoFissoRepository.ApiCall.TUTTE) }

    private val pagingSourceSilenzioseTutte: AstaSilenziosaPagingSource by inject(
        AstaSilenziosaPagingSource::class.java
    ) { parametersOf("", "", "", AstaSilenziosaRepository.ApiCall.TUTTE) }

    private val flowInverseTutte = Pager(
        PagingConfig(pageSize = 20)
    ) {
        pagingSourceInverseTutte
    }.flow
        .cachedIn(viewModelScope)

    private val flowTempoFissoTutte = Pager(
        PagingConfig(pageSize = 20)
    ) {
        pagingSourceTempoFissoTutte
    }.flow
        .cachedIn(viewModelScope)

    private val flowSilenzioseTutte = Pager(
        PagingConfig(pageSize = 20)
    ) {
        pagingSourceSilenzioseTutte
    }.flow
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val flowInverseRicerca = searchText.flatMapLatest { _ ->
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            creaPagingSourceInversa()
        }.flow
            .cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val flowTempoFissoRicerca = searchText.flatMapLatest { _ ->
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            creaPagingSourceTempoFisso()
        }.flow
            .cachedIn(viewModelScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val flowSilenzioseRicerca = searchText.flatMapLatest { _ ->
        Pager(
            PagingConfig(pageSize = 20)
        ) {
            creaPagingSourceSilenziosa()
        }.flow
            .cachedIn(viewModelScope)
    }

    private fun creaPagingSourceInversa(): AstaInversaPagingSource {
        return get(AstaInversaPagingSource::class.java)
        { parametersOf("", searchText.value, filter.value, AstaInversaRepository.ApiCall.RICERCA) }
    }

    private fun creaPagingSourceTempoFisso(): AstaTempoFissoPagingSource {
        return get(AstaTempoFissoPagingSource::class.java)
        {
            parametersOf(
                "",
                searchText.value,
                filter.value,
                AstaTempoFissoRepository.ApiCall.RICERCA
            )
        }
    }

    private fun creaPagingSourceSilenziosa(): AstaSilenziosaPagingSource {
        return get(AstaSilenziosaPagingSource::class.java)
        {
            parametersOf(
                "",
                searchText.value,
                filter.value,
                AstaSilenziosaRepository.ApiCall.RICERCA
            )
        }
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
}
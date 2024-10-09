package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iasdietideals24.dietideals24.utilities.dto.AstaDto
import com.iasdietideals24.dietideals24.utilities.kscripts.CurrentUser
import com.iasdietideals24.dietideals24.utilities.paging.AstaInversaPagingSource
import com.iasdietideals24.dietideals24.utilities.paging.AstaSilenziosaPagingSource
import com.iasdietideals24.dietideals24.utilities.paging.AstaTempoFissoPagingSource
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import org.koin.core.parameter.parametersOf
import org.koin.java.KoinJavaComponent.inject

class ModelPartecipazioni : ViewModel() {
    private val pagingSourceInverse: AstaInversaPagingSource by inject(
        AstaInversaPagingSource::class.java
    ) { parametersOf(CurrentUser.id, "", "", AstaInversaRepository.ApiCall.PARTECIPAZIONI) }
    private val pagingSourceTempoFisso: AstaTempoFissoPagingSource by inject(
        AstaTempoFissoPagingSource::class.java
    ) { parametersOf(CurrentUser.id, "", "", AstaInversaRepository.ApiCall.PARTECIPAZIONI) }
    private val pagingSourceSilenziose: AstaSilenziosaPagingSource by inject(
        AstaSilenziosaPagingSource::class.java
    ) { parametersOf(CurrentUser.id, "", "", AstaInversaRepository.ApiCall.PARTECIPAZIONI) }

    private val flowInverse = Pager(
        PagingConfig(pageSize = 20)
    ) {
        pagingSourceInverse
    }.flow
        .cachedIn(viewModelScope)

    val flowTempoFisso = Pager(
        PagingConfig(pageSize = 20)
    ) {
        pagingSourceTempoFisso
    }.flow
        .cachedIn(viewModelScope)

    val flowSilenziose = Pager(
        PagingConfig(pageSize = 20)
    ) {
        pagingSourceSilenziose
    }.flow
        .cachedIn(viewModelScope)

    @Suppress("UNCHECKED_CAST")
    fun getVenditoreFlows(): Flow<PagingData<AstaDto>> {
        return flowInverse as Flow<PagingData<AstaDto>>
    }

    @Suppress("UNCHECKED_CAST")
    fun getCompratoreFlows(): Flow<PagingData<AstaDto>> {
        return merge(flowTempoFisso, flowSilenziose) as Flow<PagingData<AstaDto>>
    }
}
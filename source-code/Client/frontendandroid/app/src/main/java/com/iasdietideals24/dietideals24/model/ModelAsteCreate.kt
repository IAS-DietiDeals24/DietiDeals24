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
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.flow.Flow

class ModelAsteCreate(
    private val inverseRepository: AstaInversaRepository,
    private val tempoFissoRepository: AstaTempoFissoRepository,
    private val silenziosaRepository: AstaSilenziosaRepository
) : ViewModel() {
    private var pagingSourceInverse: AstaInversaPagingSource? = null

    private val pagerInverse by lazy {
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                pagingSourceInverse = AstaInversaPagingSource(
                    repository = inverseRepository,
                    email = CurrentUser.id,
                    api = AstaInversaRepository.ApiCall.CREATE
                )

                pagingSourceInverse!!
            }
        )
    }

    private var pagingSourceTempoFisso: AstaTempoFissoPagingSource? = null

    private val pagerTempoFisso by lazy {
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                pagingSourceTempoFisso = AstaTempoFissoPagingSource(
                    repository = tempoFissoRepository,
                    email = CurrentUser.id,
                    api = AstaTempoFissoRepository.ApiCall.CREATE
                )

                pagingSourceTempoFisso!!
            }
        )
    }

    private var pagingSourceSilenziose: AstaSilenziosaPagingSource? = null

    private val pagerSilenziose by lazy {
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                pagingSourceSilenziose = AstaSilenziosaPagingSource(
                    repository = silenziosaRepository,
                    email = CurrentUser.id,
                    api = AstaSilenziosaRepository.ApiCall.CREATE
                )

                pagingSourceSilenziose!!
            }
        )
    }

    private val flowInverse by lazy {
        pagerInverse.flow.cachedIn(viewModelScope)
    }

    private val flowTempoFisso by lazy {
        pagerTempoFisso.flow.cachedIn(viewModelScope)
    }

    private val flowSilenziose by lazy {
        pagerSilenziose.flow.cachedIn(viewModelScope)
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

    private fun invalidateInverse() {
        pagingSourceInverse?.invalidate()
    }

    private fun invalidateTempoFisso() {
        pagingSourceTempoFisso?.invalidate()
    }

    private fun invalidateSilenziose() {
        pagingSourceSilenziose?.invalidate()
    }

    fun invalidate() {
        invalidateInverse()
        invalidateTempoFisso()
        invalidateSilenziose()
    }
}
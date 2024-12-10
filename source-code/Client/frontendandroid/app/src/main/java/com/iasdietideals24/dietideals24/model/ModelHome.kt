package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iasdietideals24.dietideals24.utilities.dto.AstaDto
import com.iasdietideals24.dietideals24.utilities.enumerations.CategoriaAsta
import com.iasdietideals24.dietideals24.utilities.paging.AstaInversaPagingSource
import com.iasdietideals24.dietideals24.utilities.paging.AstaSilenziosaPagingSource
import com.iasdietideals24.dietideals24.utilities.paging.AstaTempoFissoPagingSource
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flattenMerge

class ModelHome(
    private val inverseRepository: AstaInversaRepository,
    private val tempoFissoRepository: AstaTempoFissoRepository,
    private val silenziosaRepository: AstaSilenziosaRepository
) : ViewModel() {
    private var _tipo: Int = 0

    var tipo: Int
        get() = _tipo
        set(value) {
            _tipo = value
        }

    private val _searchText = MutableStateFlow("")

    val searchText: MutableStateFlow<String> get() = _searchText

    private var _filter = MutableStateFlow("")

    val filter: MutableStateFlow<String> get() = _filter

    private var pagingSourceInverseTutte: AstaInversaPagingSource? = null

    private val pagerInverseTutte by lazy {
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                pagingSourceInverseTutte = AstaInversaPagingSource(
                    repository = inverseRepository,
                    idAccount = CurrentUser.id,
                    api = AstaInversaRepository.ApiCall.TUTTE
                )

                pagingSourceInverseTutte!!
            }
        )
    }

    private var pagingSourceTempoFissoTutte: AstaTempoFissoPagingSource? = null

    private val pagerTempoFissoTutte by lazy {
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                pagingSourceTempoFissoTutte = AstaTempoFissoPagingSource(
                    repository = tempoFissoRepository,
                    idAccount = CurrentUser.id,
                    api = AstaTempoFissoRepository.ApiCall.TUTTE
                )

                pagingSourceTempoFissoTutte!!
            }
        )
    }

    private var pagingSourceSilenzioseTutte: AstaSilenziosaPagingSource? = null

    private val pagerSilenzioseTutte by lazy {
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                pagingSourceSilenzioseTutte = AstaSilenziosaPagingSource(
                    repository = silenziosaRepository,
                    idAccount = CurrentUser.id,
                    api = AstaSilenziosaRepository.ApiCall.TUTTE
                )

                pagingSourceSilenzioseTutte!!
            }
        )
    }

    private val flowInverseTutte by lazy {
        pagerInverseTutte.flow.cachedIn(viewModelScope)
    }

    private val flowTempoFissoTutte by lazy {
        pagerTempoFissoTutte.flow.cachedIn(viewModelScope)
    }

    private val flowSilenzioseTutte by lazy {
        pagerSilenzioseTutte.flow.cachedIn(viewModelScope)
    }

    private fun invalidateInverseTutte() {
        pagingSourceInverseTutte?.invalidate()
    }

    private fun invalidateTempoFissoTutte() {
        pagingSourceTempoFissoTutte?.invalidate()
    }

    private fun invalidateSilenzioseTutte() {
        pagingSourceSilenzioseTutte?.invalidate()
    }

    private fun invalidateTutte() {
        invalidateInverseTutte()
        invalidateTempoFissoTutte()
        invalidateSilenzioseTutte()
    }

    private var pagingSourceInverseRicerca: AstaInversaPagingSource? = null

    private val pagerInverseRicerca by lazy {
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                pagingSourceInverseRicerca = AstaInversaPagingSource(
                    repository = inverseRepository,
                    idAccount = CurrentUser.id,
                    ricerca = searchText.value,
                    filtro = CategoriaAsta.fromEnumToEnumString(
                        CategoriaAsta.fromStringToEnum(
                            filter.value
                        )
                    ),
                    api = AstaInversaRepository.ApiCall.RICERCA
                )

                pagingSourceInverseRicerca!!
            }
        )
    }

    private var pagingSourceTempoFissoRicerca: AstaTempoFissoPagingSource? = null

    private val pagerTempoFissoRicerca by lazy {
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                pagingSourceTempoFissoRicerca = AstaTempoFissoPagingSource(
                    repository = tempoFissoRepository,
                    idAccount = CurrentUser.id,
                    ricerca = searchText.value,
                    filtro = CategoriaAsta.fromEnumToEnumString(
                        CategoriaAsta.fromStringToEnum(
                            filter.value
                        )
                    ),
                    api = AstaTempoFissoRepository.ApiCall.RICERCA
                )

                pagingSourceTempoFissoRicerca!!
            }
        )
    }

    private var pagingSourceSilenzioseRicerca: AstaSilenziosaPagingSource? = null

    private val pagerSilenzioseRicerca by lazy {
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                pagingSourceSilenzioseRicerca = AstaSilenziosaPagingSource(
                    repository = silenziosaRepository,
                    idAccount = CurrentUser.id,
                    ricerca = searchText.value,
                    filtro = CategoriaAsta.fromEnumToEnumString(
                        CategoriaAsta.fromStringToEnum(
                            filter.value
                        )
                    ),
                    api = AstaSilenziosaRepository.ApiCall.RICERCA
                )

                pagingSourceSilenzioseRicerca!!
            }
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val flowInverseRicerca = combine(searchText, filter) { _, _ ->
        pagerInverseRicerca.flow.cachedIn(viewModelScope)
    }.flattenMerge()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val flowTempoFissoRicerca = combine(searchText, filter) { _ ->
        pagerTempoFissoRicerca.flow.cachedIn(viewModelScope)
    }.flattenMerge()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val flowSilenzioseRicerca = combine(searchText, filter) { _ ->
        pagerSilenzioseRicerca.flow.cachedIn(viewModelScope)
    }.flattenMerge()

    private fun invalidateInverseRicerca() {
        pagingSourceInverseRicerca?.invalidate()
    }

    private fun invalidateTempoFissoRicerca() {
        pagingSourceTempoFissoRicerca?.invalidate()
    }

    private fun invalidateSilenzioseRicerca() {
        pagingSourceSilenzioseRicerca?.invalidate()
    }

    private fun invalidateRicerca() {
        invalidateInverseRicerca()
        invalidateTempoFissoRicerca()
        invalidateSilenzioseRicerca()
    }

    @Suppress("UNCHECKED_CAST")
    fun getAsteInverseFlows(): Flow<PagingData<AstaDto>> {
        return (
                if (searchText.value.isEmpty() && filter.value.isEmpty())
                    flowInverseTutte
                else
                    flowInverseRicerca
                ) as Flow<PagingData<AstaDto>>
    }

    @Suppress("UNCHECKED_CAST")
    fun getAsteTempoFissoFlows(): Flow<PagingData<AstaDto>> {
        return (
                if (searchText.value.isEmpty() && filter.value.isEmpty())
                    flowTempoFissoTutte
                else
                    flowTempoFissoRicerca
                ) as Flow<PagingData<AstaDto>>
    }

    @Suppress("UNCHECKED_CAST")
    fun getAsteSilenzioseFlows(): Flow<PagingData<AstaDto>> {
        return (
                if (searchText.value.isEmpty() && filter.value.isEmpty())
                    flowSilenzioseTutte
                else
                    flowSilenzioseRicerca
                ) as Flow<PagingData<AstaDto>>
    }

    fun invalidate() {
        invalidateTutte()
        invalidateRicerca()
    }
}
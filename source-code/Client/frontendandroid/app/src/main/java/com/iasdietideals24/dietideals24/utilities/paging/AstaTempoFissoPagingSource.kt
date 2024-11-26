package com.iasdietideals24.dietideals24.utilities.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.iasdietideals24.dietideals24.utilities.dto.AstaTempoFissoDto
import com.iasdietideals24.dietideals24.utilities.repositories.AstaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaTempoFissoRepository.ApiCall

class AstaTempoFissoPagingSource(
    private val repository: AstaTempoFissoRepository,
    private val email: String = "",
    private val ricerca: String = "",
    private val filtro: String = "",
    private val api: ApiCall = ApiCall.TUTTE
) : PagingSource<Long, AstaTempoFissoDto>() {
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, AstaTempoFissoDto> {
        val page = params.key ?: 0
        val size = params.loadSize.toLong()

        return try {
            val data = when (api) {
                ApiCall.CREATE -> repository.recuperaAsteCreateTempoFisso(email, size, 0)
                ApiCall.TUTTE -> repository.recuperaAsteTempoFisso(size, 0)
                ApiCall.RICERCA -> repository.ricercaAsteTempoFisso(ricerca, filtro, size, 0)
                ApiCall.PARTECIPAZIONI -> repository.recuperaPartecipazioniTempoFisso(
                    email,
                    size,
                    0
                )
            }

            LoadResult.Page(
                data = data.content,
                prevKey = if (page == 0L) null else page - 1,
                nextKey = if (data.isLast) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Long, AstaTempoFissoDto>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

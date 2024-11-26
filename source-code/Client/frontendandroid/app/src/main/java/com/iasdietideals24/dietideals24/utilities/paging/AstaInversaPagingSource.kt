package com.iasdietideals24.dietideals24.utilities.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.iasdietideals24.dietideals24.utilities.dto.AstaInversaDto
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository.ApiCall

class AstaInversaPagingSource(
    private val repository: AstaInversaRepository,
    private val email: String = "",
    private val ricerca: String = "",
    private val filtro: String = "",
    private val api: ApiCall = ApiCall.TUTTE
) : PagingSource<Long, AstaInversaDto>() {
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, AstaInversaDto> {
        val page = params.key ?: 0
        val size = params.loadSize.toLong()

        return try {
            val data = when (api) {
                ApiCall.CREATE -> repository.recuperaAsteCreateInverse(email, size, 0)
                ApiCall.TUTTE -> repository.recuperaAsteInverse(size, 0)
                ApiCall.RICERCA -> repository.ricercaAsteInverse(ricerca, filtro, size, 0)
                ApiCall.PARTECIPAZIONI -> repository.recuperaPartecipazioniInverse(email, size, 0)
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

    override fun getRefreshKey(state: PagingState<Long, AstaInversaDto>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

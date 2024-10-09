package com.iasdietideals24.dietideals24.utilities.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.iasdietideals24.dietideals24.utilities.dto.AstaInversaDto
import com.iasdietideals24.dietideals24.utilities.repositories.AstaInversaRepository.ApiCall
import com.iasdietideals24.dietideals24.utilities.services.AstaInversaService

class AstaInversaPagingSource(
    private val service: AstaInversaService,
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
                ApiCall.CREATE -> service.recuperaAsteCreateInverse(email, size, 0)
                ApiCall.TUTTE -> service.recuperaAsteInverse(size, 0)
                ApiCall.RICERCA -> service.ricercaAsteInverse(ricerca, filtro, size, 0)
                ApiCall.PARTECIPAZIONI -> service.recuperaPartecipazioniInverse(email, size, 0)
            }

            LoadResult.Page(
                data = data.body()!!.content,
                prevKey = if (page == 0L) null else page - 1,
                nextKey = if (data.body()!!.last) null else page + 1
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

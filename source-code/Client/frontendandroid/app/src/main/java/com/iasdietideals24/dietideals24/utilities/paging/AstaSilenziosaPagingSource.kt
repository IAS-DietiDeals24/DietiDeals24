package com.iasdietideals24.dietideals24.utilities.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.iasdietideals24.dietideals24.utilities.dto.AstaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.repositories.AstaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.AstaSilenziosaRepository.ApiCall

class AstaSilenziosaPagingSource(
    private val repository: AstaSilenziosaRepository,
    private val idAccount: Long = 0L,
    private val ricerca: String = "",
    private val filtro: String = "",
    private val api: ApiCall = ApiCall.TUTTE
) : PagingSource<Long, AstaSilenziosaDto>() {
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, AstaSilenziosaDto> {
        val page = params.key ?: 0
        val size = params.loadSize.toLong()

        return try {
            val data = when (api) {
                ApiCall.CREATE -> repository.recuperaAsteCreateSilenziose(idAccount, size, 0)
                ApiCall.TUTTE -> repository.recuperaAsteSilenziose(idAccount, size, 0)
                ApiCall.RICERCA -> repository.ricercaAsteSilenziose(ricerca, filtro, size, 0)
                ApiCall.PARTECIPAZIONI -> repository.recuperaPartecipazioniSilenziose(
                    idAccount,
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

    override fun getRefreshKey(state: PagingState<Long, AstaSilenziosaDto>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

package com.iasdietideals24.dietideals24.utilities.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.iasdietideals24.dietideals24.utilities.dto.CategoriaAstaDto
import com.iasdietideals24.dietideals24.utilities.services.CategoriaAstaService

class CategoriaAstaPagingSource(
    private val service: CategoriaAstaService,
) : PagingSource<Long, CategoriaAstaDto>() {
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, CategoriaAstaDto> {
        val page = params.key ?: 0
        val size = params.loadSize.toLong()

        return try {
            val data = service.recuperaCategorieAste(size, 0)

            LoadResult.Page(
                data = data.body()!!.content,
                prevKey = if (page == 0L) null else page - 1,
                nextKey = if (data.body()!!.last) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Long, CategoriaAstaDto>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

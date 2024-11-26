package com.iasdietideals24.dietideals24.utilities.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.iasdietideals24.dietideals24.utilities.dto.NotificaDto
import com.iasdietideals24.dietideals24.utilities.repositories.NotificaRepository

class NotificaPagingSource(
    private val repository: NotificaRepository,
    private val email: String = ""
) : PagingSource<Long, NotificaDto>() {
    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, NotificaDto> {
        val page = params.key ?: 0
        val size = params.loadSize.toLong()

        return try {
            val data = repository.recuperaNotifiche(email, size, 0)

            LoadResult.Page(
                data = data.content,
                prevKey = if (page == 0L) null else page - 1,
                nextKey = if (data.isLast) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Long, NotificaDto>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}

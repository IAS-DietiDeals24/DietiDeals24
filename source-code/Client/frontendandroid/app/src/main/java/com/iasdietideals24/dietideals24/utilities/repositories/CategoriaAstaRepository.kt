package com.iasdietideals24.dietideals24.utilities.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.iasdietideals24.dietideals24.utilities.dto.CategoriaAstaDto
import com.iasdietideals24.dietideals24.utilities.paging.CategoriaAstaPagingSource
import com.iasdietideals24.dietideals24.utilities.services.CategoriaAstaService
import kotlinx.coroutines.flow.Flow

class CategoriaAstaRepository(private val service: CategoriaAstaService) {
    fun recuperaCategorieAsta(): Flow<PagingData<CategoriaAstaDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { CategoriaAstaPagingSource(service) }
        ).flow
    }
}
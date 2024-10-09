package com.iasdietideals24.dietideals24.utilities.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.iasdietideals24.dietideals24.utilities.dto.NotificaDto
import com.iasdietideals24.dietideals24.utilities.paging.NotificaPagingSource
import com.iasdietideals24.dietideals24.utilities.services.NotificaService
import kotlinx.coroutines.flow.Flow

class NotificaRepository(private val service: NotificaService) {
    fun recuperaNotifiche(email: String): Flow<PagingData<NotificaDto>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { NotificaPagingSource(service, email) }
        ).flow
    }
}
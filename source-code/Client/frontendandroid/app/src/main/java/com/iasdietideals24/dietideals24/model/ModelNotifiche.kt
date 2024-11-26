package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iasdietideals24.dietideals24.utilities.dto.NotificaDto
import com.iasdietideals24.dietideals24.utilities.paging.NotificaPagingSource
import com.iasdietideals24.dietideals24.utilities.repositories.NotificaRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.flow.Flow

class ModelNotifiche(
    private val notificaRepository: NotificaRepository
) : ViewModel() {

    private var pagingSourceNotifiche: NotificaPagingSource? = null

    private val pagerNotifiche by lazy {
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                pagingSourceNotifiche = NotificaPagingSource(
                    repository = notificaRepository,
                    email = CurrentUser.id
                )

                pagingSourceNotifiche!!
            }
        )
    }

    private val flowNotifiche by lazy {
        pagerNotifiche.flow.cachedIn(viewModelScope)
    }

    fun getNotificheFlows(): Flow<PagingData<NotificaDto>> {
        return flowNotifiche
    }

    private fun invalidateNotifiche() {
        pagingSourceNotifiche?.invalidate()
    }

    fun invalidate() {
        invalidateNotifiche()
    }
}
package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iasdietideals24.dietideals24.utilities.dto.NotificaDto
import com.iasdietideals24.dietideals24.utilities.paging.NotificaPagingSource
import kotlinx.coroutines.flow.Flow

class ModelNotifiche(
    private val pagingSourceNotifiche: NotificaPagingSource
) : ViewModel() {
    private val flow = Pager(
        PagingConfig(pageSize = 20)
    ) {
        pagingSourceNotifiche
    }.flow
        .cachedIn(viewModelScope)

    fun getFlow(): Flow<PagingData<NotificaDto>> {
        return flow
    }
}
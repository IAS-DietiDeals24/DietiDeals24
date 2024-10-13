package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iasdietideals24.dietideals24.utilities.dto.NotificaDto
import com.iasdietideals24.dietideals24.utilities.repositories.NotificaRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.flow.Flow

class ModelNotifiche(
    private val notificaRepository: NotificaRepository
) : ViewModel() {

    private val flow by lazy {
        notificaRepository.recuperaNotifiche(CurrentUser.id).cachedIn(viewModelScope)
    }

    fun getFlowNotifiche(): Flow<PagingData<NotificaDto>> {
        return flow
    }
}
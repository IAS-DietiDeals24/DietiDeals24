package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.NotificaDto
import com.iasdietideals24.dietideals24.utilities.services.NotificaService
import com.iasdietideals24.dietideals24.utilities.tools.Page

class NotificaRepository(private val service: NotificaService) {
    suspend fun recuperaNotifiche(idAccount: Long, size: Long, page: Long): Page<NotificaDto> {
        return service.recuperaNotifiche(idAccount, size, page).body() ?: Page<NotificaDto>()
    }
}
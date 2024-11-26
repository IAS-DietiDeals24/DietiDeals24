package com.iasdietideals24.dietideals24.utilities.repositories

import com.iasdietideals24.dietideals24.utilities.dto.NotificaDto
import com.iasdietideals24.dietideals24.utilities.services.NotificaService
import com.iasdietideals24.dietideals24.utilities.tools.Page

class NotificaRepository(private val service: NotificaService) {
    suspend fun recuperaNotifiche(email: String, size: Long, page: Long): Page<NotificaDto> {
        return service.recuperaNotifiche(email, size, page).body() ?: Page<NotificaDto>()
    }
}
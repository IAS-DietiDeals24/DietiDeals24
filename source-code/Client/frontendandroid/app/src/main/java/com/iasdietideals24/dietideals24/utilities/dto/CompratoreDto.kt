package com.iasdietideals24.dietideals24.utilities.dto

import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto
import com.iasdietideals24.dietideals24.utilities.dto.shallows.NotificaShallowDto
import com.iasdietideals24.dietideals24.utilities.dto.shallows.OffertaShallowDto
import com.iasdietideals24.dietideals24.utilities.dto.shallows.ProfiloShallowDto
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@Getter
@Setter
@NoArgsConstructor
class CompratoreDto {
    private val email: String? = null

    private val password: String? = null

    private val facebookId: String? = null

    private val profiloShallow: ProfiloShallowDto? = null

    private val notificheInviateShallow: Set<NotificaShallowDto>? = null

    private val notificheRicevuteShallow: Set<NotificaShallowDto>? = null

    private val astePosseduteShallow: Set<AstaShallowDto>? = null

    private val offerteCollegateShallow: Set<OffertaShallowDto>? = null
}

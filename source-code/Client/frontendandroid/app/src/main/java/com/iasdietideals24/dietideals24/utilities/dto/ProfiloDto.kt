package com.iasdietideals24.dietideals24.utilities.dto

import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import java.time.LocalDate

@Getter
@Setter
@NoArgsConstructor
class ProfiloDto {
    private val nomeUtente: String? = null

    private val profilePicture: ByteArray? = null

    private val nome: String? = null

    private val cognome: String? = null

    private val dataNascita: LocalDate? = null

    private val areaGeografica: String? = null

    private val biografia: String? = null

    private val linkPersonale: String? = null

    private val linkInstagram: String? = null

    private val linkFacebook: String? = null

    private val linkGitHub: String? = null

    private val linkX: String? = null

    private val accountsShallow: Set<AccountShallowDto>? = null
}

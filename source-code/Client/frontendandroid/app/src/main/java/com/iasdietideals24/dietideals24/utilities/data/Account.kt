package com.iasdietideals24.dietideals24.utilities.data

import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount

data class Account(
    val idFacebook: String = "",
    val email: String = "",
    val password: String = "",
    val tipoAccount: TipoAccount = TipoAccount.OSPITE
)

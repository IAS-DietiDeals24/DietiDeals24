package com.iasdietideals24.dietideals24.utilities.classes.data

import com.iasdietideals24.dietideals24.utilities.classes.TipoAccount

data class Account(
    val facebookId: String = "",
    val email: String = "",
    val password: String = "",
    val tipoAccount: TipoAccount = TipoAccount.OSPITE
)

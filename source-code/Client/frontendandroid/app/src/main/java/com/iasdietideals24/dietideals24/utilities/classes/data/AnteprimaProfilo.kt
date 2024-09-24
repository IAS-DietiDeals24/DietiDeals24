package com.iasdietideals24.dietideals24.utilities.classes.data

import com.iasdietideals24.dietideals24.utilities.classes.TipoAccount

data class AnteprimaProfilo(
    val nome: String = "",
    val tipoAccount: TipoAccount = TipoAccount.OSPITE,
    val immagineProfilo: ByteArray = ByteArray(0)
)
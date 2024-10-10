package com.iasdietideals24.dietideals24.utilities.data

import com.iasdietideals24.dietideals24.utilities.enumerations.CategoriaAsta


data class DettagliAsta(
    val anteprimaAsta: AnteprimaAsta = AnteprimaAsta(),
    val categoria: CategoriaAsta = CategoriaAsta.ND,
    val idCreatore: String = "",
    val nomeCreatore: String = "",
    val descrizione: String = ""
)
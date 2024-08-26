package com.iasdietideals24.dietideals24.utilities.classes.data


data class DettagliAsta(
    val anteprimaAsta: AnteprimaAsta = AnteprimaAsta(),
    val categoria: String = "",
    val idCreatore: Long = 0L,
    val nomeCreatore: String = "",
    val descrizione: String = ""
)
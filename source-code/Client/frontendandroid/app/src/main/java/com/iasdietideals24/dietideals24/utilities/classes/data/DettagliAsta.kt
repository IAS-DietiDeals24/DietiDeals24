package com.iasdietideals24.dietideals24.utilities.classes.data


data class DettagliAsta(
    val _datiAnteprimaAsta: DatiAnteprimaAsta,
    val _categoria: String = "",
    val _idCreatore: Long = 0L,
    val _nomeCreatore: String = "",
    val _descrizione: String = ""
)
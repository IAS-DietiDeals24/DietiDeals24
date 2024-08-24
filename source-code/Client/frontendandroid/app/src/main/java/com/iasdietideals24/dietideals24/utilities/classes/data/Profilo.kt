package com.iasdietideals24.dietideals24.utilities.classes.data

import java.time.LocalDate

data class Profilo(
    val _idAccountCollegati: Pair<Long, Long> = Pair(0L, 0L),
    val _idProfilo: Long = 0L,
    val _tipoAccount: String = "",
    val _nomeUtente: String = "",
    val _immagineProfilo: ByteArray = ByteArray(0),
    val _nome: String = "",
    val _cognome: String = "",
    val _email: String = "",
    val _dataNascita: LocalDate = LocalDate.MIN,
    val _genere: String = "",
    val _areaGeografica: String = "",
    val _biografia: String = "",
    val _linkInstagram: String = "",
    val _linkFacebook: String = "",
    val _linkGitHub: String = "",
    val _linkX: String = "",
    val _linkPersonale: String = ""
)
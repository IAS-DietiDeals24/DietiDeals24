package com.iasdietideals24.dietideals24.utilities.classes.data

import java.time.LocalDate

data class Profilo(
    val idAccountCollegati: Pair<Long, Long> = Pair(0L, 0L),
    val idProfilo: Long = 0L,
    val tipoAccount: String = "",
    val nomeUtente: String = "",
    val immagineProfilo: ByteArray = ByteArray(0),
    val nome: String = "",
    val cognome: String = "",
    val email: String = "",
    val dataNascita: LocalDate = LocalDate.MIN,
    val genere: String = "",
    val areaGeografica: String = "",
    val biografia: String = "",
    val linkInstagram: String = "",
    val linkFacebook: String = "",
    val linkGitHub: String = "",
    val linkX: String = "",
    val linkPersonale: String = ""
)
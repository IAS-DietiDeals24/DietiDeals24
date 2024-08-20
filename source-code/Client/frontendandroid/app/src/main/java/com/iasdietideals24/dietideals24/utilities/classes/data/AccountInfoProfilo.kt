package com.iasdietideals24.dietideals24.utilities.classes.data

import java.sql.Date

data class AccountInfoProfilo(
    private val _accountInfo: AccountInfo? = AccountInfo(),
    private var _nomeUtente: String? = "",
    private var _nome: String? = "",
    private var _cognome: String? = "",
    private var _dataNascita: Date? = Date(0),
    private var _immagineProfilo: ByteArray? = ByteArray(0),
    private var _biografia: String? = "",
    private var _areaGeografica: String? = "",
    private var _genere: String? = "",
    private var _linkPersonale: String? = "",
    private var _linkInstagram: String? = "",
    private var _linkFacebook: String? = "",
    private var _linkGitHub: String? = "",
    private var _linkX: String? = ""
)

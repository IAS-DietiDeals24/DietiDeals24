package com.iasdietideals24.dietideals24.utilities.tools

import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount

object CurrentUser {
    private var _id: String = "andrylook14@gmail.com"
    private var _tipoAccount: TipoAccount = TipoAccount.COMPRATORE
    private var _accessToken: String = ""

    var id: String
        get() = _id
        set(value) {
            _id = value
        }

    var tipoAccount: TipoAccount
        get() = _tipoAccount
        set(value) {
            _tipoAccount = value
        }

    var accessToken: String
        get() = _accessToken
        set(value) {
            _accessToken = value
        }
}
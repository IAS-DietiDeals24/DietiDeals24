package com.iasdietideals24.dietideals24.utilities.tools

import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount

object CurrentUser {
    private var _id: Long = 0L
    private var _tipoAccount: TipoAccount = TipoAccount.OSPITE
    private var _jwt: String = ""

    var id: Long
        get() = _id
        set(value) {
            _id = value
        }

    var tipoAccount: TipoAccount
        get() = _tipoAccount
        set(value) {
            _tipoAccount = value
        }

    var jwt: String
        get() = _jwt
        set(value) {
            _jwt = value
        }
}
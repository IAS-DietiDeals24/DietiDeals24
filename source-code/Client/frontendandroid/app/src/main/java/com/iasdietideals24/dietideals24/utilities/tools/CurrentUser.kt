package com.iasdietideals24.dietideals24.utilities.tools

import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount

object CurrentUser {
    private var _id: Long = 0L
    private var _tipoAccount: TipoAccount = TipoAccount.OSPITE
    private var _rToken: String = ""
    private var _aToken: String = ""

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

    var rToken: String
        get() = _rToken
        set(value) {
            _rToken = value
        }

    var aToken: String
        get() = _aToken
        set(value) {
            _aToken = value
        }
}
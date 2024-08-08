package com.iasdietideals24.dietideals24.model

import com.iasdietideals24.dietideals24.utilities.annotations.Validation
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneAccountNonEsistente


class ModelControllerAccesso {

    private var _email: String = ""

    private var _password: String = ""

    private var _tipoAccount: String = ""

    var email: String
        get() = _email
        set(valore) {
            _email = valore
        }

    var password: String
        get() = _password
        set(valore) {
            _password = valore
        }

    var tipoAccount: String
        get() = _tipoAccount
        set(valore) {
            _tipoAccount = valore
        }

    @Validation
    @Throws(EccezioneAccountNonEsistente::class)
    fun validate() {
        email()
        password()
    }

    @Validation
    @Throws(EccezioneAccountNonEsistente::class)
    private fun email() {
        if (email == "") throw EccezioneAccountNonEsistente("Email non compilata.")
        if (!email.contains(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")))
            throw EccezioneAccountNonEsistente("Email non valida.")
    }

    @Validation
    @Throws(EccezioneAccountNonEsistente::class)
    private fun password() {
        if (password == "") throw EccezioneAccountNonEsistente("Password non compilata.")
    }
}
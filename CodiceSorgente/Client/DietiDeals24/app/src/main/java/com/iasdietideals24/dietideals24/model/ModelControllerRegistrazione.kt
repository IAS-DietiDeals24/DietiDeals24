package com.iasdietideals24.dietideals24.model

import com.iasdietideals24.dietideals24.utilities.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.EccezioneEmailNonValida
import com.iasdietideals24.dietideals24.utilities.EccezioneEmailUsata
import com.iasdietideals24.dietideals24.utilities.EccezionePasswordNonSicura
import com.iasdietideals24.dietideals24.utilities.Validation
import retrofit2.http.GET
import retrofit2.http.Path

class ModelControllerRegistrazione {

    private var _email: String = ""

    private var _password: String = ""

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


    @Validation
    @Throws(
        EccezioneCampiNonCompilati::class, EccezioneEmailNonValida::class,
        EccezioneEmailUsata::class, EccezionePasswordNonSicura::class
    )
    fun validate() {
        email()
        password()
    }

    @Validation
    @Throws(
        EccezioneCampiNonCompilati::class, EccezioneEmailNonValida::class,
        EccezioneEmailUsata::class
    )
    private fun email() {
        if (email.isEmpty()) throw EccezioneCampiNonCompilati("Email non compilata.")
        if (!email.contains(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")))
            throw EccezioneEmailNonValida("Email non valida.")
        if (existsEmail(email)) throw EccezioneEmailUsata("Email già usata.")
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class, EccezionePasswordNonSicura::class)
    private fun password() {
        if (password == "") throw EccezioneCampiNonCompilati("Password non compilata.")
        if (password.length < 8 ||
            !password.contains(Regex("[A-Z]")) ||
            !password.contains(Regex("[0-9]")) ||
            !password.contains(Regex("[!@#\$%^{}&*]"))
        )
            throw EccezionePasswordNonSicura("Password non sicura.")
    }


    @GET("account?email={email}")
    private fun existsEmail(@Path("email") email: String): Boolean {
        TODO()
    }
}
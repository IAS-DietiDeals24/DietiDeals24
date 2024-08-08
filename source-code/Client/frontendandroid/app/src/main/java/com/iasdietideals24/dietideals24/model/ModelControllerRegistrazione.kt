package com.iasdietideals24.dietideals24.model

import com.iasdietideals24.dietideals24.utilities.APIController
import com.iasdietideals24.dietideals24.utilities.annotations.Validation
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneAPI
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneEmailNonValida
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneEmailUsata
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezionePasswordNonSicura
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModelControllerRegistrazione {

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

        val returned = esisteEmail()
        if (returned == null) throw EccezioneAPI("Errore durante la chiamata API.")
        else if (returned == true) throw EccezioneEmailUsata("Email già usata.")
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

    private fun esisteEmail(): Boolean? {
        var returned: Boolean? = null

        val call = APIController.instance.esisteEmail(email, tipoAccount)

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    returned = response.body()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                throw t;
            }
        })

        return returned
    }
}
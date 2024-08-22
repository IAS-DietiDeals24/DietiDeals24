package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iasdietideals24.dietideals24.utilities.annotations.Validation
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneAccountNonEsistente


class ModelAccesso : ViewModel() {

    private val _email: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val email: MutableLiveData<String>
        get() = _email

    private val _password: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val password: MutableLiveData<String>
        get() = _password

    private val _tipoAccount: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val tipoAccount: MutableLiveData<String>
        get() = _tipoAccount

    @Validation
    @Throws(EccezioneAccountNonEsistente::class)
    fun validate() {
        email()
        password()
    }

    @Validation
    @Throws(EccezioneAccountNonEsistente::class)
    private fun email() {
        if (email.value?.isEmpty() == true)
            throw EccezioneAccountNonEsistente("Email non compilata.")
        if (email.value?.contains(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) == false)
            throw EccezioneAccountNonEsistente("Email non valida.")
    }

    @Validation
    @Throws(EccezioneAccountNonEsistente::class)
    private fun password() {
        if (password.value?.isEmpty() == true)
            throw EccezioneAccountNonEsistente("Password non compilata.")
    }
}
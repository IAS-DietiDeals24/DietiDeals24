package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iasdietideals24.dietideals24.utilities.annotations.Validation
import com.iasdietideals24.dietideals24.utilities.classes.APIController
import com.iasdietideals24.dietideals24.utilities.classes.data.AccountInfo
import com.iasdietideals24.dietideals24.utilities.classes.data.AccountInfoProfilo
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneAPI
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneEmailNonValida
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneEmailUsata
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezionePasswordNonSicura
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Date

class ModelRegistrazione : ViewModel() {

    private val _facebookAccountID: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val facebookAccountID: MutableLiveData<String>
        get() = _facebookAccountID

    private val _email: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val email: MutableLiveData<String>
        get() = _email

    private val _password: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val password: MutableLiveData<String>
        get() = _password

    private val _tipoAccount: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val tipoAccount: MutableLiveData<String>
        get() = _tipoAccount

    private val _nomeUtente: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val nomeUtente: MutableLiveData<String>
        get() = _nomeUtente

    private val _nome: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val nome: MutableLiveData<String>
        get() = _nome

    private val _cognome: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val cognome: MutableLiveData<String>
        get() = _cognome

    private val _dataNascita: MutableLiveData<Date> by lazy {
        MutableLiveData<Date>()
    }

    val dataNascita: MutableLiveData<Date>
        get() = _dataNascita

    private val _immagineProfilo: MutableLiveData<ByteArray> by lazy {
        MutableLiveData<ByteArray>()
    }

    val immagineProfilo: MutableLiveData<ByteArray>
        get() = _immagineProfilo


    private val _biografia: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val biografia: MutableLiveData<String>
        get() = _biografia


    private val _areaGeografica: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val areaGeografica: MutableLiveData<String>
        get() = _areaGeografica

    private val _genere: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val genere: MutableLiveData<String>
        get() = _genere

    private val _linkPersonale: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val linkPersonale: MutableLiveData<String>
        get() = _linkPersonale

    private val _linkInstagram: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val linkInstagram: MutableLiveData<String>
        get() = _linkInstagram

    private val _linkFacebook: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val linkFacebook: MutableLiveData<String>
        get() = _linkFacebook

    private val _linkGitHub: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val linkGitHub: MutableLiveData<String>
        get() = _linkGitHub

    private val _linkX: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val linkX: MutableLiveData<String>
        get() = _linkX

    fun clear() {
        _facebookAccountID.value = ""
        _email.value = ""
        _password.value = ""
        _tipoAccount.value = ""
        _nomeUtente.value = ""
        _nome.value = ""
        _cognome.value = ""
        _dataNascita.value = Date(0)
        _immagineProfilo.value = ByteArray(0)
        _biografia.value = ""
        _areaGeografica.value = ""
        _genere.value = ""
        _linkPersonale.value = ""
        _linkInstagram.value = ""
        _linkFacebook.value = ""
        _linkGitHub.value = ""
        _linkX.value = ""
    }

    fun toAccountInfo(): AccountInfo {
        return AccountInfo(
            _facebookAccountID.value,
            _email.value,
            _password.value,
            _tipoAccount.value
        )
    }

    fun toAccountProfileInfo(): AccountInfoProfilo {
        return AccountInfoProfilo(
            AccountInfo(
                _facebookAccountID.value,
                _email.value,
                _password.value,
                _tipoAccount.value
            ),
            _nomeUtente.value,
            _nome.value,
            _cognome.value,
            _dataNascita.value,
            _immagineProfilo.value,
            _biografia.value,
            _areaGeografica.value,
            _genere.value,
            _linkPersonale.value,
            _linkInstagram.value,
            _linkFacebook.value,
            _linkGitHub.value,
            _linkX.value
        )
    }

    @Validation
    @Throws(
        EccezioneCampiNonCompilati::class, EccezioneEmailNonValida::class,
        EccezioneEmailUsata::class, EccezionePasswordNonSicura::class
    )
    fun validateAccount() {
        email()
        password()
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class)
    fun validateProfile() {
        nomeUtente()
        nome()
        cognome()
        dataNascita()
    }

    @Validation
    @Throws(
        EccezioneCampiNonCompilati::class, EccezioneEmailNonValida::class,
        EccezioneEmailUsata::class
    )
    private fun email() {
        if (email.value?.isEmpty() == true)
            throw EccezioneCampiNonCompilati("Email non compilata.")
        if (email.value?.contains(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) == false)
            throw EccezioneEmailNonValida("Email non valida.")

        val returned = esisteEmail()
        if (returned == null)
            throw EccezioneAPI("Errore durante la chiamata API.")
        else if (returned == true)
            throw EccezioneEmailUsata("Email già usata.")
    }

    @Throws(EccezioneCampiNonCompilati::class, EccezionePasswordNonSicura::class)
    private fun password() {
        if (password.value?.isEmpty() == true)
            throw EccezioneCampiNonCompilati("Password non compilata.")
        if (password.value?.length!! < 8 ||
            password.value?.contains(Regex("[A-Z]")) == false ||
            password.value?.contains(Regex("[0-9]")) == false ||
            password.value?.contains(Regex("[!@#\$%^{}&*]")) == false
        )
            throw EccezionePasswordNonSicura("Password non sicura.")
    }

    private fun esisteEmail(): Boolean? {
        var returned: Boolean? = null

        val call =
            APIController.instance.esisteEmail(
                email.value!!,
                tipoAccount.value!!
            )

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    returned = response.body()
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                throw t
            }
        })

        return returned
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class)
    private fun nomeUtente() {
        if (nomeUtente.value?.isEmpty() == true)
            throw EccezioneCampiNonCompilati("Nome utente non compilato.")
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class)
    private fun nome() {
        if (nome.value?.isEmpty() == true)
            throw EccezioneCampiNonCompilati("Nome non compilato.")
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class)
    private fun cognome() {
        if (cognome.value?.isEmpty() == true)
            throw EccezioneCampiNonCompilati("Cognome non compilato.")
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class)
    private fun dataNascita() {
        if (dataNascita.value == null)
            throw EccezioneCampiNonCompilati("Data di nascita non compilata.")
    }
}
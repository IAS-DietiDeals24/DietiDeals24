package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iasdietideals24.dietideals24.utilities.annotations.Validation
import com.iasdietideals24.dietideals24.utilities.classes.APIController
import com.iasdietideals24.dietideals24.utilities.classes.data.Profilo
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneAPI
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneEmailNonValida
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneEmailUsata
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class ModelProfilo : ViewModel() {

    private val _idProfilo: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>(0L)
    }

    val idProfilo: MutableLiveData<Long>
        get() = _idProfilo

    private val _tipoAccount: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val tipoAccount: MutableLiveData<String>
        get() = _tipoAccount

    private val _nomeUtente: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val nomeUtente: MutableLiveData<String>
        get() = _nomeUtente

    private val _immagineProfilo: MutableLiveData<ByteArray> by lazy {
        MutableLiveData<ByteArray>(ByteArray(0))
    }

    val immagineProfilo: MutableLiveData<ByteArray>
        get() = _immagineProfilo

    private val _nome: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val nome: MutableLiveData<String>
        get() = _nome

    private val _cognome: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val cognome: MutableLiveData<String>
        get() = _cognome

    private val _email: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val email: MutableLiveData<String>
        get() = _email

    private val _dataNascita: MutableLiveData<LocalDate> by lazy {
        MutableLiveData<LocalDate>(LocalDate.MIN)
    }

    val dataNascita: MutableLiveData<LocalDate>
        get() = _dataNascita


    private val _genere: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val genere: MutableLiveData<String>
        get() = _genere

    private val _areaGeografica: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val areaGeografica: MutableLiveData<String>
        get() = _areaGeografica

    private val _biografia: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val biografia: MutableLiveData<String>
        get() = _biografia

    private val _linkInstagram: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val linkInstagram: MutableLiveData<String>
        get() = _linkInstagram

    private val _linkFacebook: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val linkFacebook: MutableLiveData<String>
        get() = _linkFacebook

    private val _linkGitHub: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val linkGitHub: MutableLiveData<String>
        get() = _linkGitHub

    private val _linkX: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val linkX: MutableLiveData<String>
        get() = _linkX

    private val _linkPersonale: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val linkPersonale: MutableLiveData<String>
        get() = _linkPersonale

    fun toProfilo(): Profilo {
        return Profilo(
            Pair(0L, 0L),
            idProfilo.value!!,
            tipoAccount.value!!,
            nomeUtente.value!!,
            immagineProfilo.value!!,
            nome.value!!,
            cognome.value!!,
            email.value!!,
            dataNascita.value!!,
            genere.value!!,
            areaGeografica.value!!,
            biografia.value!!,
            linkInstagram.value!!,
            linkFacebook.value!!,
            linkGitHub.value!!,
            linkX.value!!,
            linkPersonale.value!!
        )
    }

    fun clear() {
        _email.value = ""
        _tipoAccount.value = ""
        _nomeUtente.value = ""
        _nome.value = ""
        _cognome.value = ""
        _dataNascita.value = LocalDate.MIN
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

    @Validation
    @Throws(
        EccezioneCampiNonCompilati::class,
        EccezioneEmailNonValida::class,
        EccezioneEmailUsata::class
    )
    fun validate() {
        nome()
        cognome()
        email()
        dataNascita()
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
    private fun dataNascita() {
        if (dataNascita.value == LocalDate.MIN)
            throw EccezioneCampiNonCompilati("Data di nascita non compilata.")
    }
}
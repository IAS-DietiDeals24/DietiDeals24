package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iasdietideals24.dietideals24.utilities.annotations.Validation
import com.iasdietideals24.dietideals24.utilities.dto.VenditoreDto
import com.iasdietideals24.dietideals24.utilities.dto.exceptional.PutProfiloDto
import com.iasdietideals24.dietideals24.utilities.dto.shallows.ProfiloShallowDto
import com.iasdietideals24.dietideals24.utilities.dto.utilities.AnagraficaProfiloDto
import com.iasdietideals24.dietideals24.utilities.dto.utilities.LinksProfiloDto
import com.iasdietideals24.dietideals24.utilities.dto.utilities.TokensAccountDto
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAccount
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneNomeUtenteUsato
import com.iasdietideals24.dietideals24.utilities.repositories.ProfiloRepository
import java.time.LocalDate

class ModelRegistrazione(private val profiloRepository: ProfiloRepository) : ViewModel() {

    private val _idAccount: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>(0)
    }

    val idAccount: MutableLiveData<Long>
        get() = _idAccount

    private val _facebookAccountID: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    private val facebookAccountID: MutableLiveData<String>
        get() = _facebookAccountID

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

    private val _tipoAccount: MutableLiveData<TipoAccount> by lazy {
        MutableLiveData<TipoAccount>(TipoAccount.OSPITE)
    }

    val tipoAccount: MutableLiveData<TipoAccount>
        get() = _tipoAccount

    private val _nomeUtente: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val nomeUtente: MutableLiveData<String>
        get() = _nomeUtente

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

    private val _dataNascita: MutableLiveData<LocalDate> by lazy {
        MutableLiveData<LocalDate>(LocalDate.MIN)
    }

    val dataNascita: MutableLiveData<LocalDate>
        get() = _dataNascita

    private val _immagineProfilo: MutableLiveData<ByteArray> by lazy {
        MutableLiveData<ByteArray>(ByteArray(0))
    }

    val immagineProfilo: MutableLiveData<ByteArray>
        get() = _immagineProfilo


    private val _biografia: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val biografia: MutableLiveData<String>
        get() = _biografia


    private val _areaGeografica: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val areaGeografica: MutableLiveData<String>
        get() = _areaGeografica

    private val _genere: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val genere: MutableLiveData<String>
        get() = _genere

    private val _linkPersonale: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val linkPersonale: MutableLiveData<String>
        get() = _linkPersonale

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

    fun toPutAccountCompratore(): PutProfiloDto {
        return PutProfiloDto(
            nomeUtente.value,
            immagineProfilo.value,
            AnagraficaProfiloDto(
                nome.value,
                cognome.value,
                dataNascita.value,
                areaGeografica.value,
                genere.value,
                biografia.value
            ),
            LinksProfiloDto(
                linkPersonale.value,
                linkInstagram.value,
                linkFacebook.value,
                linkGitHub.value,
                linkX.value
            ),
            email.value,
            password.value,
            TokensAccountDto(
                facebookAccountID.value,
                "",
                "",
                ""
            ),
            "Compratore"
        )
    }

    fun toAccountVenditore(): VenditoreDto {
        return VenditoreDto(
            0L,
            email.value!!,
            password.value!!,
            TokensAccountDto(
                facebookAccountID.value!!,
                "",
                "",
                ""
            ),
            ProfiloShallowDto(nomeUtente.value)
        )
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class)
    suspend fun validateProfile() {
        nomeUtente()
        nome()
        cognome()
        dataNascita()
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class)
    private suspend fun nomeUtente() {
        if (nomeUtente.value?.isEmpty() == true)
            throw EccezioneCampiNonCompilati("Nome utente non compilato.")
        if (esisteNomeUtente())
            throw EccezioneNomeUtenteUsato("Nome utente già usato.")
    }

    private suspend fun esisteNomeUtente(): Boolean {
        return profiloRepository.caricaProfilo(nomeUtente.value!!).nomeUtente.isNotEmpty()
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
        if (dataNascita.value == LocalDate.MIN)
            throw EccezioneCampiNonCompilati("Data di nascita non compilata.")
    }
}
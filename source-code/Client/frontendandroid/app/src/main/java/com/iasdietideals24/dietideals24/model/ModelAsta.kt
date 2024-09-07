package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iasdietideals24.dietideals24.utilities.annotations.Validation
import com.iasdietideals24.dietideals24.utilities.classes.data.Asta
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

class ModelAsta : ViewModel() {

    private val _idAsta: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>(0L)
    }

    val idAsta: MutableLiveData<Long>
        get() = _idAsta

    private val _idCreatore: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val idCreatore: MutableLiveData<String>
        get() = _idCreatore

    private val _nomeCreatore: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val nomeCreatore: MutableLiveData<String>
        get() = _nomeCreatore

    private val _tipo: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val tipo: MutableLiveData<String>
        get() = _tipo

    private val _dataFine: MutableLiveData<LocalDate> by lazy {
        MutableLiveData<LocalDate>(LocalDate.MIN)
    }

    val dataFine: MutableLiveData<LocalDate>
        get() = _dataFine

    private val _oraFine: MutableLiveData<LocalTime> by lazy {
        MutableLiveData<LocalTime>(LocalTime.MIN)
    }

    val oraFine: MutableLiveData<LocalTime>
        get() = _oraFine

    private val _prezzo: MutableLiveData<BigDecimal> by lazy {
        MutableLiveData<BigDecimal>(BigDecimal(0.0))
    }

    val prezzo: MutableLiveData<BigDecimal>
        get() = _prezzo

    private val _immagine: MutableLiveData<ByteArray> by lazy {
        MutableLiveData<ByteArray>(ByteArray(0))
    }

    val immagine: MutableLiveData<ByteArray>
        get() = _immagine

    private val _nome: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val nome: MutableLiveData<String>
        get() = _nome

    private val _categoria: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val categoria: MutableLiveData<String>
        get() = _categoria

    private val _descrizione: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val descrizione: MutableLiveData<String>
        get() = _descrizione

    fun clear() {
        _idAsta.value = 0L
        _idCreatore.value = ""
        _nomeCreatore.value = ""
        _tipo.value = ""
        _dataFine.value = LocalDate.MIN
        _oraFine.value = LocalTime.MIN
        _prezzo.value = BigDecimal(0.0)
        _immagine.value = ByteArray(0)
        _nome.value = ""
        _categoria.value = ""
        _descrizione.value = ""
    }

    fun toAsta(): Asta {
        return Asta(
            idAsta.value!!,
            idCreatore.value!!,
            tipo.value!!,
            dataFine.value!!,
            oraFine.value!!,
            prezzo.value!!,
            immagine.value!!,
            nome.value!!,
            categoria.value!!,
            descrizione.value!!
        )
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class)
    fun validate() {
        dataFine()
        oraFine()
        nome()
        categoria()
        descrizione()
        if (_tipo.value != "Silenziosa")
            prezzo()
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class)
    private fun dataFine() {
        if (dataFine.value == LocalDate.MIN) {
            throw EccezioneCampiNonCompilati("Data di fine non compilata.")
        }
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class)
    private fun oraFine() {
        if (oraFine.value == null) {
            throw EccezioneCampiNonCompilati("Ora di fine non compilata.")
        }
    }


    @Validation
    @Throws(EccezioneCampiNonCompilati::class)
    private fun nome() {
        if (nome.value?.isEmpty() == true) {
            throw EccezioneCampiNonCompilati("Nome non compilato.")
        }
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class)
    private fun categoria() {
        if (categoria.value?.isEmpty() == true) {
            throw EccezioneCampiNonCompilati("Categoria non compilata.")
        }
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class)
    private fun descrizione() {
        if (descrizione.value?.isEmpty() == true) {
            throw EccezioneCampiNonCompilati("Descrizione non compilata.")
        }
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class)
    private fun prezzo() {
        if (prezzo.value?.toDouble() == 0.0) {
            throw EccezioneCampiNonCompilati("Prezzo non compilato.")
        }
        if (prezzo.toString().toDouble() < 0.0 || !prezzo.toString()
                .matches(Regex("^\\d+\\.\\d{2}\$"))
        ) {
            throw EccezioneCampiNonCompilati("Prezzo non valido")
        }
    }
}

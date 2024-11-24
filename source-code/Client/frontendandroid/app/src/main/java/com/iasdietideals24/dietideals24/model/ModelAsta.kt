package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.iasdietideals24.dietideals24.utilities.annotations.Validation
import com.iasdietideals24.dietideals24.utilities.dto.AstaInversaDto
import com.iasdietideals24.dietideals24.utilities.dto.AstaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.dto.AstaTempoFissoDto
import com.iasdietideals24.dietideals24.utilities.dto.OffertaDto
import com.iasdietideals24.dietideals24.utilities.dto.shallows.AccountShallowDto
import com.iasdietideals24.dietideals24.utilities.dto.shallows.CategoriaAstaShallowDto
import com.iasdietideals24.dietideals24.utilities.enumerations.CategoriaAsta
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneDataPassata
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

class ModelAsta(
    private val inverseRepository: OffertaInversaRepository,
    private val tempoFissoRepository: OffertaTempoFissoRepository,
    private val silenziosaRepository: OffertaSilenziosaRepository,
) : ViewModel() {

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

    private val _tipo: MutableLiveData<TipoAsta?> by lazy {
        MutableLiveData<TipoAsta?>(null)
    }

    val tipo: MutableLiveData<TipoAsta?>
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
        MutableLiveData<BigDecimal>(BigDecimal("0.0"))
    }

    val prezzo: MutableLiveData<BigDecimal>
        get() = _prezzo

    private val _immagine: MutableLiveData<ByteArray?> by lazy {
        MutableLiveData<ByteArray?>(ByteArray(0))
    }

    val immagine: MutableLiveData<ByteArray?>
        get() = _immagine

    private val _nome: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val nome: MutableLiveData<String>
        get() = _nome

    private val _categoria: MutableLiveData<CategoriaAsta> by lazy {
        MutableLiveData<CategoriaAsta>(CategoriaAsta.ND)
    }

    val categoria: MutableLiveData<CategoriaAsta>
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
        _tipo.value = TipoAsta.TEMPO_FISSO
        _dataFine.value = LocalDate.MIN
        _oraFine.value = LocalTime.MIN
        _prezzo.value = BigDecimal("0.0")
        _immagine.value = ByteArray(0)
        _nome.value = ""
        _categoria.value = CategoriaAsta.ND
        _descrizione.value = ""
    }

    fun toAstaInversa(): AstaInversaDto {
        return AstaInversaDto(
            idAsta.value!!,
            CategoriaAstaShallowDto(
                CategoriaAsta.fromEnumToString(categoria.value!!),
            ),
            nome.value!!,
            descrizione.value!!,
            dataFine.value!!,
            oraFine.value!!,
            immagine.value ?: ByteArray(0),
            setOf(),
            AccountShallowDto(
                CurrentUser.id,
                "Compratore"
            ),
            setOf(),
            prezzo.value!!
        )
    }

    fun toAstaTempoFisso(): AstaTempoFissoDto {
        return AstaTempoFissoDto(
            idAsta.value!!,
            CategoriaAstaShallowDto(
                CategoriaAsta.fromEnumToString(categoria.value!!),
            ),
            nome.value!!,
            descrizione.value!!,
            dataFine.value!!,
            oraFine.value!!,
            immagine.value ?: ByteArray(0),
            setOf(),
            AccountShallowDto(
                CurrentUser.id,
                "Venditore"
            ),
            setOf(),
            prezzo.value!!
        )
    }

    fun toAstaSilenziosa(): AstaSilenziosaDto {
        return AstaSilenziosaDto(
            idAsta.value!!,
            CategoriaAstaShallowDto(
                CategoriaAsta.fromEnumToString(categoria.value!!),
            ),
            nome.value!!,
            descrizione.value!!,
            dataFine.value!!,
            oraFine.value!!,
            immagine.value ?: ByteArray(0),
            setOf(),
            AccountShallowDto(
                CurrentUser.id,
                "Venditore"
            ),
            setOf()
        )
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class, EccezioneDataPassata::class)
    fun validate() {
        dataFine()
        oraFine()
        nome()
        categoria()
        descrizione()
        if (_tipo.value != TipoAsta.SILENZIOSA)
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
    @Throws(EccezioneCampiNonCompilati::class, EccezioneDataPassata::class)
    private fun oraFine() {
        if (oraFine.value == null) {
            throw EccezioneCampiNonCompilati("Ora di fine non compilata.")
        } else if (dataFine.value!! == LocalDate.now() && oraFine.value!! <= LocalTime.now()) {
            throw EccezioneDataPassata("La data e ora inserite sono nel passato.")
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
        if (categoria.value == CategoriaAsta.ND) {
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
        if (prezzo.value?.toDouble()!! < 0.0 || !prezzo.value.toString()
                .matches(Regex("^\\d+(\\.\\d{1,2})?$"))
        ) {
            throw EccezioneCampiNonCompilati("Prezzo non valido")
        }
    }

    private val flowInverse by lazy {
        inverseRepository.recuperaOfferteInverse(idAsta.value!!).cachedIn(viewModelScope)
    }

    private val flowTempoFisso by lazy {
        tempoFissoRepository.recuperaOfferteTempoFisso(idAsta.value!!).cachedIn(viewModelScope)
    }

    private val flowSilenziose by lazy {
        silenziosaRepository.recuperaOfferteSilenziose(idAsta.value!!).cachedIn(viewModelScope)
    }

    @Suppress("UNCHECKED_CAST")
    fun getInverseFlows(): Flow<PagingData<OffertaDto>> {
        return flowInverse as Flow<PagingData<OffertaDto>>
    }

    @Suppress("UNCHECKED_CAST")
    fun getTempoFissoFlows(): Flow<PagingData<OffertaDto>> {
        return flowTempoFisso as Flow<PagingData<OffertaDto>>
    }

    @Suppress("UNCHECKED_CAST")
    fun getSilenzioseFlows(): Flow<PagingData<OffertaDto>> {
        return flowSilenziose as Flow<PagingData<OffertaDto>>
    }
}

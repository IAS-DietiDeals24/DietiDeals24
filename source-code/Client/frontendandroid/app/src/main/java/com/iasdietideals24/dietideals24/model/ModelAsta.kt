package com.iasdietideals24.dietideals24.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
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
import com.iasdietideals24.dietideals24.utilities.enumerations.StatoAsta
import com.iasdietideals24.dietideals24.utilities.enumerations.TipoAsta
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati
import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneDataPassata
import com.iasdietideals24.dietideals24.utilities.paging.OffertaInversaPagingSource
import com.iasdietideals24.dietideals24.utilities.paging.OffertaSilenziosaPagingSource
import com.iasdietideals24.dietideals24.utilities.paging.OffertaTempoFissoPagingSource
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaInversaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaSilenziosaRepository
import com.iasdietideals24.dietideals24.utilities.repositories.OffertaTempoFissoRepository
import com.iasdietideals24.dietideals24.utilities.tools.CurrentUser
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

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

    private val _stato: MutableLiveData<StatoAsta> by lazy {
        MutableLiveData<StatoAsta>(StatoAsta.ACTIVE)
    }

    val stato: MutableLiveData<StatoAsta>
        get() = _stato

    private val _idCreatore: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>(0L)
    }

    val idCreatore: MutableLiveData<Long>
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
        MutableLiveData<BigDecimal>(BigDecimal("0.00"))
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
        _stato.value = StatoAsta.ACTIVE
        _idCreatore.value = 0L
        _nomeCreatore.value = ""
        _tipo.value = TipoAsta.TEMPO_FISSO
        _dataFine.value = LocalDate.MIN
        _oraFine.value = LocalTime.MIN
        _prezzo.value = BigDecimal("0.00")
        _immagine.value = ByteArray(0)
        _nome.value = ""
        _categoria.value = CategoriaAsta.ND
        _descrizione.value = ""
    }

    fun toAstaInversa(patching: Boolean = false): AstaInversaDto {
        val local =
            ZonedDateTime.of(this.dataFine.value!!, this.oraFine.value!!, ZoneId.systemDefault())
        val utc = local.withZoneSameInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS)
        val dataFine = utc.toLocalDate()
        val oraFine = utc.toLocalTime()

        return AstaInversaDto(
            idAsta.value!!,
            stato.value!!.name,
            CategoriaAstaShallowDto(
                categoria.value!!.name,
            ),
            nome.value!!,
            descrizione.value!!,
            dataFine,
            oraFine,
            immagine.value ?: ByteArray(0),
            if (patching) null else setOf(),
            if (patching) null else AccountShallowDto(
                CurrentUser.id,
                "Compratore"
            ),
            if (patching) null else setOf(),
            prezzo.value!!
        )
    }

    fun toAstaTempoFisso(patching: Boolean = false): AstaTempoFissoDto {
        val local =
            ZonedDateTime.of(this.dataFine.value!!, this.oraFine.value!!, ZoneId.systemDefault())
        val utc = local.withZoneSameInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS)
        val dataFine = utc.toLocalDate()
        val oraFine = utc.toLocalTime()

        return AstaTempoFissoDto(
            idAsta.value!!,
            stato.value!!.name,
            CategoriaAstaShallowDto(
                categoria.value!!.name,
            ),
            nome.value!!,
            descrizione.value!!,
            dataFine,
            oraFine,
            immagine.value ?: ByteArray(0),
            if (patching) null else setOf(),
            if (patching) null else AccountShallowDto(
                CurrentUser.id,
                "Venditore"
            ),
            if (patching) null else setOf(),
            prezzo.value!!
        )
    }

    fun toAstaSilenziosa(patching: Boolean = false): AstaSilenziosaDto {
        val local =
            ZonedDateTime.of(this.dataFine.value!!, this.oraFine.value!!, ZoneId.systemDefault())
        val utc = local.withZoneSameInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS)
        val dataFine = utc.toLocalDate()
        val oraFine = utc.toLocalTime()

        return AstaSilenziosaDto(
            idAsta.value!!,
            stato.value!!.name,
            CategoriaAstaShallowDto(
                categoria.value!!.name,
            ),
            nome.value!!,
            descrizione.value!!,
            dataFine,
            oraFine,
            immagine.value ?: ByteArray(0),
            if (patching) null else setOf(),
            if (patching) null else AccountShallowDto(
                CurrentUser.id,
                "Venditore"
            ),
            if (patching) null else setOf()
        )
    }

    @Validation
    @Throws(EccezioneCampiNonCompilati::class, EccezioneDataPassata::class)
    fun validate() {
        dataFine()
        oraFine()
        nome()
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

    private var pagingSourceInverse: OffertaInversaPagingSource? = null

    private val pagerInverse by lazy {
        Pager(
            config = PagingConfig(
                pageSize = 4,
                initialLoadSize = 4,
                prefetchDistance = 2,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                pagingSourceInverse = OffertaInversaPagingSource(
                    repository = inverseRepository,
                    idAsta = idAsta.value!!
                )

                pagingSourceInverse!!
            }
        )
    }

    private var pagingSourceTempoFisso: OffertaTempoFissoPagingSource? = null

    private val pagerTempoFisso by lazy {
        Pager(
            config = PagingConfig(
                pageSize = 4,
                initialLoadSize = 4,
                prefetchDistance = 2,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                pagingSourceTempoFisso = OffertaTempoFissoPagingSource(
                    repository = tempoFissoRepository,
                    idAsta = idAsta.value!!
                )

                pagingSourceTempoFisso!!
            }
        )
    }

    private var pagingSourceSilenziose: OffertaSilenziosaPagingSource? = null

    private val pagerSilenziose by lazy {
        Pager(
            config = PagingConfig(
                pageSize = 4,
                initialLoadSize = 4,
                prefetchDistance = 2,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                pagingSourceSilenziose = OffertaSilenziosaPagingSource(
                    repository = silenziosaRepository,
                    idAsta = idAsta.value!!
                )

                pagingSourceSilenziose!!
            }
        )
    }
    private val flowInverse by lazy {
        pagerInverse.flow.cachedIn(viewModelScope)
    }

    private val flowTempoFisso by lazy {
        pagerTempoFisso.flow.cachedIn(viewModelScope)
    }

    private val flowSilenziose by lazy {
        pagerSilenziose.flow.cachedIn(viewModelScope)
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

    private fun invalidateInverse() {
        pagingSourceInverse?.invalidate()
    }

    private fun invalidateTempoFisso() {
        pagingSourceTempoFisso?.invalidate()
    }

    private fun invalidateSilenziose() {
        pagingSourceSilenziose?.invalidate()
    }

    fun invalidate() {
        invalidateInverse()
        invalidateTempoFisso()
        invalidateSilenziose()
    }
}

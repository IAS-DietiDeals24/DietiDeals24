package com.iasdietideals24.dietideals24.utilities.classes

import androidx.recyclerview.widget.RecyclerView
import com.iasdietideals24.dietideals24.utilities.classes.data.AnteprimaAsta
import com.iasdietideals24.dietideals24.utilities.classes.data.Notifica
import com.iasdietideals24.dietideals24.utilities.classes.data.OffertaRicevuta
import com.iasdietideals24.dietideals24.utilities.dto.AstaInversaDto
import com.iasdietideals24.dietideals24.utilities.dto.AstaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.dto.AstaTempoFissoDto
import com.iasdietideals24.dietideals24.utilities.dto.NotificaDto
import com.iasdietideals24.dietideals24.utilities.dto.OffertaInversaDto
import com.iasdietideals24.dietideals24.utilities.dto.OffertaSilenziosaDto
import com.iasdietideals24.dietideals24.utilities.dto.OffertaTempoFissoDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.reflect.full.createInstance

/**
 * Formatta la [LocalDate] in una [String] seguendo le impostazioni di cultura correnti.
 * @return La [String] nel formato predefinito per la cultura.
 */
fun LocalDate.toLocalStringShort(): String {
    return this.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
}

/**
 * Utilizza la [LocalDate] per contare il numero di millisecondi passati dal tempo di epoca Unix
 * (1970-01-01T00:00:00Z).
 * @return Un [Long] che indica il numero di millisecondi passati dalla epoca.
 */
fun LocalDate.toMillis(): Long {
    return this.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

/**
 * Costruisce una [LocalDate] utilizzando il valore di questo oggetto [Long] per contare il numero
 * di millisecondi passati dal tempo di epoca Unix (1970-01-01T00:00:00Z).
 * @return La [LocalDate] distante tal numero di millisecondi dalla epoca.
 * @throws IllegalArgumentException Se il [Long] sul quale la funzione è stata chiamata è negativo.
 */
@Throws(IllegalArgumentException::class)
fun Long.toLocalDate(): LocalDate {
    require(this >= 0) { "Il valore deve essere maggiore o uguale a zero." }

    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}

/**
 * Converte un [Set] di oggetti di tipo [T] (ove [T] sia istanza di [AstaInversaDto],
 * [AstaSilenziosaDto] o [AstaTempoFissoDto]) in un [Array] di oggetti di tipo [AnteprimaAsta].
 * Se [T] non è istanza di una di queste classi, viene restituito un [Array] vuoto.
 * Il [Set] di partenza non viene modificato.
 * @return Un [Array] di oggetti di tipo [AnteprimaAsta].
 */
inline fun <reified T> Set<T>.toArrayOfAnteprimaAsta(): Array<AnteprimaAsta> {
    val result = arrayOf<AnteprimaAsta>()

    when {
        T::class is AstaInversaDto -> {
            this.map {
                val auction = it as AstaInversaDto
                result.plus(auction.toAnteprimaAsta())
            }
        }

        T::class is AstaSilenziosaDto -> {
            this.map {
                val auction = it as AstaSilenziosaDto
                result.plus(auction.toAnteprimaAsta())
            }
        }

        T::class is AstaTempoFissoDto -> {
            this.map {
                val auction = it as AstaTempoFissoDto
                result.plus(auction.toAnteprimaAsta())
            }
        }
    }

    return result
}

/**
 * Converte un [Set] di oggetti di tipo [NotificaDto] in un [Array] di oggetti di tipo [Notifica].
 * Il [Set] di partenza non viene modificato.
 * @return Un [Array] di oggetti di tipo [Notifica].
 */
fun Set<NotificaDto>.toArrayOfNotifica(): Array<Notifica> {
    val result = arrayOf<Notifica>()

    this.map {
        result.plus(it.toNotifica())
    }

    return result
}

/**
 * Converte un [Set] di oggetti di tipo [T] (ove [T] sia istanza di [OffertaInversaDto],
 * [OffertaSilenziosaDto] o [OffertaTempoFissoDto]) in un [Array] di oggetti di tipo [OffertaRicevuta].
 * Se [T] non è istanza di una di queste classi, viene restituito un [Array] vuoto.
 * Il [Set] di partenza non viene modificato.
 * @return Un [Array] di oggetti di tipo [OffertaRicevuta].
 */
inline fun <reified T> Set<T>.toArrayOfOffertaRicevuta(): Array<OffertaRicevuta> {
    val result = arrayOf<OffertaRicevuta>()

    when {
        T::class is OffertaInversaDto -> {
            this.map {
                val bid = it as OffertaInversaDto
                result.plus(bid.toOffertaRicevuta())
            }
        }

        T::class is OffertaSilenziosaDto -> {
            this.map {
                val bid = it as OffertaSilenziosaDto
                result.plus(bid.toOffertaRicevuta())
            }
        }

        T::class is OffertaTempoFissoDto -> {
            this.map {
                val bid = it as OffertaTempoFissoDto
                result.plus(bid.toOffertaRicevuta())
            }
        }
    }

    return result
}

/**
 * Effettua una chiamata REST e ne restituisce la risposta JSON deserializzata in un oggetto.
 * @param call Wrapper con la funzione da chiamare.
 * @return Una classe generica [Dto] che contiene il risultato della chiamata REST.
 */
inline fun <reified Dto : Any> RecyclerView.ViewHolder.chiamaAPI(call: Call<Dto>): Dto {
    var ret: Dto? = null

    call.enqueue(object : Callback<Dto> {
        override fun onResponse(call: Call<Dto>, response: Response<Dto>) {
            if (response.isSuccessful) {
                ret = response.body()
            }
        }

        override fun onFailure(call: Call<Dto>, t: Throwable) {
            // Non fare nulla
        }
    })

    return ret ?: Dto::class.createInstance()
}
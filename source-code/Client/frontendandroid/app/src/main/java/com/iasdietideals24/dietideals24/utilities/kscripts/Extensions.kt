package com.iasdietideals24.dietideals24.utilities.kscripts

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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
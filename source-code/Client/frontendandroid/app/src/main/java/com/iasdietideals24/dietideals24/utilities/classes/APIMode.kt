package com.iasdietideals24.dietideals24.utilities.classes

import androidx.annotation.Keep

/**
 * Indica la modalità di esecuzione dell'applicazione.
 */
@Keep
enum class APIMode {
    /**
     * Modalità debug. Chiama una versione dummy delle API che resituisce risultati predefiniti per
     * il bene della semplicità e la facilitazione del processo di creazione e debug del codice.
     */
    DEBUG,

    /**
     * Modalità release. Chiama la versione autentica delle API.
     */
    RELEASE
}
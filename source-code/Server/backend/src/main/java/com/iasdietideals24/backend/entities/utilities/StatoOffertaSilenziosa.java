package com.iasdietideals24.backend.entities.utilities;

/**
 * L'enumeration "StatoOffertaSilenziosa" rappresenta i diversi stati che può assumere l'offerta silenziosa.
 * I possibili stati sono: PENDING, ACCEPTED, REJECTED.
 */

public enum StatoOffertaSilenziosa {
    /**
     * Indica che l'offerta non è stata né rifiutata né accettata dal proprietario dell'asta silenziosa
     */
    PENDING,

    /**
     * Indica che l'offerta è stata accettata dal proprietario dell'asta silenziosa
     */
    ACCEPTED,

    /**
     * Indica che l'offerta è stata rifiutata dal proprietario dell'asta silenziosa
     */
    REJECTED
}

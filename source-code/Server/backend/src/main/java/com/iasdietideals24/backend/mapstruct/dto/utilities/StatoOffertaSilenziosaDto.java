package com.iasdietideals24.backend.mapstruct.dto.utilities;

/**
 * L'enumeration "statoOffertaSilenziosaDto" rappresenta i diversi stati che può assumere l'offerta silenziosa.
 * I possibili stati sono: PENDING, ACCEPTED, REJECTED.
 */

public enum StatoOffertaSilenziosaDto {
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

package com.iasdietideals24.backend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class OffertaDiVenditore extends Offerta {
    private Venditore venditoreCollegato;
}

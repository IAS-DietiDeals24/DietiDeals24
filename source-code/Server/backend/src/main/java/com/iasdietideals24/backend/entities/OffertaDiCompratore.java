package com.iasdietideals24.backend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class OffertaDiCompratore extends Offerta {
    private Compratore compratoreCollegato;
}

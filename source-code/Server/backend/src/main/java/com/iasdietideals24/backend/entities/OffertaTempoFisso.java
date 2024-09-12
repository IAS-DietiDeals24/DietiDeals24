package com.iasdietideals24.backend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OffertaTempoFisso extends OffertaDiCompratore {
    private AstaSilenziosa astaRiferimento;
}

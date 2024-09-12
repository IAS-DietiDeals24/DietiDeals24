package com.iasdietideals24.backend.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OffertaSilenziosa extends OffertaDiCompratore {
    private Boolean isAccettata;

    private AstaSilenziosa astaRiferimento;
}

package com.iasdietideals24.backend.datautil;

import com.iasdietideals24.backend.entities.AstaTempoFisso;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.OffertaTempoFisso;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class TestDataOffertaTempoFisso {

    private TestDataOffertaTempoFisso() {
    }

    public static OffertaTempoFisso createOffertaTempoFissoA(final Compratore compratoreCollegato, final AstaTempoFisso astaRiferimento) {
        return new OffertaTempoFisso(LocalDate.of(2021, 10, 2),
                LocalTime.of(7, 15),
                BigDecimal.valueOf(12.30),
                compratoreCollegato,
                astaRiferimento);
    }

    public static OffertaTempoFisso createOffertaTempoFissoB(final Compratore compratoreCollegato, final AstaTempoFisso astaRiferimento) {
        return new OffertaTempoFisso(LocalDate.of(2021, 1, 17),
                LocalTime.of(14, 30),
                BigDecimal.valueOf(15.00),
                compratoreCollegato,
                astaRiferimento);
    }

    public static OffertaTempoFisso createOffertaTempoFissoC(final Compratore compratoreCollegato, final AstaTempoFisso astaRiferimento) {
        return new OffertaTempoFisso(LocalDate.of(2024, 12, 12),
                LocalTime.of(21, 10),
                BigDecimal.valueOf(33.00),
                compratoreCollegato,
                astaRiferimento);
    }
}

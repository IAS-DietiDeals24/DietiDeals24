package com.iasdietideals24.backend.datautil;

import com.iasdietideals24.backend.entities.AstaInversa;
import com.iasdietideals24.backend.entities.OffertaInversa;
import com.iasdietideals24.backend.entities.Venditore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class TestDataOffertaInversa {

    private TestDataOffertaInversa() {
    }

    public static OffertaInversa createOffertaInversaA(final Venditore venditoreCollegato, final AstaInversa astaRiferimento) {
        return new OffertaInversa(LocalDate.of(2024, 11, 12),
                LocalTime.of(15, 10),
                BigDecimal.valueOf(50.00),
                venditoreCollegato,
                astaRiferimento);
    }

    public static OffertaInversa createOffertaInversaB(final Venditore venditoreCollegato, final AstaInversa astaRiferimento) {
        return new OffertaInversa(LocalDate.of(2022, 1, 15),
                LocalTime.of(18, 10),
                BigDecimal.valueOf(20.00),
                venditoreCollegato,
                astaRiferimento);
    }

    public static OffertaInversa createOffertaInversaC(final Venditore venditoreCollegato, final AstaInversa astaRiferimento) {
        return new OffertaInversa(LocalDate.of(2023, 2, 22),
                LocalTime.of(9, 10),
                BigDecimal.valueOf(13.40),
                venditoreCollegato,
                astaRiferimento);
    }
}

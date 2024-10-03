package com.iasdietideals24.backend.datautil;

import com.iasdietideals24.backend.entities.AstaInversa;
import com.iasdietideals24.backend.entities.CategoriaAsta;
import com.iasdietideals24.backend.entities.Compratore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public final class TestDataAstaInversa {

    private TestDataAstaInversa() {
    }

    public static AstaInversa createAstaInversaA(final CategoriaAsta categoriaAsta, final Compratore proprietario) {
        return new AstaInversa(categoriaAsta,
                "Dragon Age: Origins Xbox 360",
                "Edizione Xbox 360 del videogioco Dragon Age: Origins. Ci giocava mio marito.",
                LocalDate.of(2024, 6, 19),
                LocalTime.of(18, 44),
                null,
                proprietario,
                BigDecimal.valueOf(1.00));
    }

    public static AstaInversa createAstaInversaB(final CategoriaAsta categoriaAsta, final Compratore proprietario) {
        return new AstaInversa(categoriaAsta,
                "Borsa Carpisa",
                "Borsa Carpisa in trama coccodrillo e metalleria oro.",
                LocalDate.of(2023, 6, 19),
                LocalTime.of(18, 44),
                new byte[]{-126, 19, 27},
                proprietario,
                BigDecimal.valueOf(2.00));
    }

    public static AstaInversa createAstaInversaC(final CategoriaAsta categoriaAsta, final Compratore proprietario) {
        return new AstaInversa(categoriaAsta,
                "Portafogli Carpisa",
                "Portafogli Carpisa capiente con cerniera.",
                LocalDate.of(2022, 9, 10),
                LocalTime.of(17, 44),
                new byte[]{-100, -20, -14},
                proprietario,
                BigDecimal.valueOf(3.00));
    }
}

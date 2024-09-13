package com.iasdietideals24.backend.datautil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.iasdietideals24.backend.entities.AstaSilenziosa;
import com.iasdietideals24.backend.entities.OffertaSilenziosa;
import com.iasdietideals24.backend.entities.Compratore;

public class TestDataOffertaSilenziosa {
    
    private TestDataOffertaSilenziosa(){
    }

    public static OffertaSilenziosa createOffertaSilenziosaA(final Compratore compratoreCollegato, final AstaSilenziosa astaRiferimento) {
        return new OffertaSilenziosa(LocalDate.of(2021, 8, 10),
                                    LocalTime.of(8, 10),
                                    BigDecimal.valueOf(9.00),
                                    compratoreCollegato,
                                    null,
                                    astaRiferimento);
    }

    public static OffertaSilenziosa createOffertaSilenziosaB(final Compratore compratoreCollegato, final Boolean isAccettata, final AstaSilenziosa astaRiferimento) {
        return new OffertaSilenziosa(LocalDate.of(2022, 1, 15),
                                    LocalTime.of(18, 10),
                                    BigDecimal.valueOf(20.00),
                                    compratoreCollegato,
                                    isAccettata,
                                    astaRiferimento);
    }

    public static OffertaSilenziosa createOffertaSilenziosaC(final Compratore compratoreCollegato, final Boolean isAccettata, final AstaSilenziosa astaRiferimento) {
        return new OffertaSilenziosa(LocalDate.of(2021, 8, 22),
                                    LocalTime.of(23, 10),
                                    BigDecimal.valueOf(18.00),
                                    compratoreCollegato,
                                    isAccettata,
                                    astaRiferimento);
    }
}

package com.iasdietideals24.backend.datautil;

import com.iasdietideals24.backend.entities.AstaTempoFisso;
import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.entities.utilities.CategoriaAsta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public final class TestDataAstaTempoFisso {

    private TestDataAstaTempoFisso() {
    }

    public static AstaTempoFisso createAstaTempoFissoA(final Venditore proprietario) {
        return new AstaTempoFisso(CategoriaAsta.COMICS_AND_MANGAS,
                "Jujutsu Kaisen vol. 11 di Gege Akutami",
                "Volume 11 del manga Jujutsu Kaisen in ottime condizioni. Leggermente rovinato agli angoli.",
                LocalDate.of(2024, 6, 19),
                LocalTime.of(18, 22),
                null,
                proprietario,
                BigDecimal.valueOf(5));
    }

    public static AstaTempoFisso createAstaTempoFissoB(final Venditore proprietario) {
        return new AstaTempoFisso(CategoriaAsta.VIDEOGAMES_AND_CONSOLES,
                "Shadow of the Tomb Raider PS4",
                "Shadow of the Tomb Raider per PS4, come nuovo.",
                LocalDate.of(2020, 8, 27),
                LocalTime.of(6, 59),
                new byte[]{-91, 60, 83},
                proprietario,
                BigDecimal.valueOf(8.30));
    }

    public static AstaTempoFisso createAstaTempoFissoC(final Venditore proprietario) {
        return new AstaTempoFisso(CategoriaAsta.CLOTHINGS_AND_WEARABLES,
                "Parrucca bionda con frangia",
                "Parrucca capelli biondi per aspiranti Raffaella Carrà.",
                LocalDate.of(2024, 2, 12),
                LocalTime.of(10, 30),
                new byte[]{-99, -75, 45},
                proprietario,
                BigDecimal.valueOf(2.30));
    }
}

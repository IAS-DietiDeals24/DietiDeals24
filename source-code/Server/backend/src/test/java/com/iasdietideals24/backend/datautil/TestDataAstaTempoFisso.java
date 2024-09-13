package com.iasdietideals24.backend.datautil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.LinkedHashSet;

import com.iasdietideals24.backend.entities.AstaTempoFisso;
import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.entities.OffertaTempoFisso;

public final class TestDataAstaTempoFisso {
    
    private TestDataAstaTempoFisso(){
    }

    public static AstaTempoFisso createAstaTempoFissoA(final Venditore proprietario) {
        return new AstaTempoFisso("Manga",
                                "Jujutsu Kaisen vol. 11 di Gege Akutami", 
                                "Volume 11 del manga Jujutsu Kaisen in ottime condizioni. Leggermente rovinato agli angoli.",
                                LocalDate.of(2024, 6, 19),
                                LocalTime.of(18, 22),
                                null,
                                null,
                                proprietario,
                                BigDecimal.valueOf(5),
                                null);
    }

    public static AstaTempoFisso createAstaTempoFissoB(final Set<Notifica> notificheAssociate, final Venditore proprietario, final Set<OffertaTempoFisso> offerteRicevute) {
        Set<byte[]> immagini = new LinkedHashSet<byte[]>();
        immagini.add(new byte[] {-82, -18, 31});
        immagini.add(new byte[] {12, 14, 83});
        immagini.add(new byte[] {-91, 60, 83});

        return new AstaTempoFisso("Videogiochi",
                                "Shadow of the Tomb Raider PS4", 
                                "Shadow of the Tomb Raider per PS4, come nuovo.",
                                LocalDate.of(2020, 8, 27),
                                LocalTime.of(6, 59),
                                immagini,
                                notificheAssociate,
                                proprietario,
                                BigDecimal.valueOf(8.30),
                                offerteRicevute);
    }

    public static AstaTempoFisso createAstaTempoFissoC(final Set<Notifica> notificheAssociate, final Venditore proprietario, final Set<OffertaTempoFisso> offerteRicevute) {
        Set<byte[]> immagini = new LinkedHashSet<byte[]>();
        immagini.add(new byte[] {-89, 8, 68});
        immagini.add(new byte[] {-52, 100, 104});
        immagini.add(new byte[] {-99, -75, 45});

        return new AstaTempoFisso("Moda",
                                "Parrucca bionda con frangia", 
                                "Parrucca capelli biondi per aspiranti Raffaella Carrà.",
                                LocalDate.of(2024, 2, 12),
                                LocalTime.of(10, 30),
                                immagini,
                                notificheAssociate,
                                proprietario,
                                BigDecimal.valueOf(2.30),
                                offerteRicevute);
    }
}

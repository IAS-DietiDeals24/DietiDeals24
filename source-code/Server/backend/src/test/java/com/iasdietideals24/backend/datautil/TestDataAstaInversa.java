package com.iasdietideals24.backend.datautil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.LinkedHashSet;

import com.iasdietideals24.backend.entities.AstaInversa;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.entities.OffertaInversa;

public final class TestDataAstaInversa {
    
    private TestDataAstaInversa(){
    }

    public static AstaInversa createAstaInversaA(final Compratore proprietario) {
        return new AstaInversa("Videogiochi",
                                "Dragon Age: Origins Xbox 360", 
                                "Edizione Xbox 360 del videogioco Dragon Age: Origins. Ci giocava mio marito.",
                                LocalDate.of(2024, 6, 19),
                                LocalTime.of(18, 44),
                                null,
                                null,
                                proprietario,
                                BigDecimal.valueOf(1.00),
                                null);
    }

    public static AstaInversa createAstaInversaB(final Set<Notifica> notificheAssociate, final Compratore proprietario, final Set<OffertaInversa> offerteRicevute) {
        Set<byte[]> immagini = new LinkedHashSet<byte[]>();
        immagini.add(new byte[] {-76, -73, -54});
        immagini.add(new byte[] {-3, 33, 88});
        immagini.add(new byte[] {-126, 19, 27});

        return new AstaInversa("Moda",
                                "Borsa Carpisa", 
                                "Borsa Carpisa in trama coccodrillo e metalleria oro.",
                                LocalDate.of(2023, 6, 19),
                                LocalTime.of(18, 44),
                                immagini,
                                notificheAssociate,
                                proprietario,
                                BigDecimal.valueOf(2.00),
                                offerteRicevute);
    }

    public static AstaInversa createAstaInversaC(final Set<Notifica> notificheAssociate, final Compratore proprietario, final Set<OffertaInversa> offerteRicevute) {
        Set<byte[]> immagini = new LinkedHashSet<byte[]>();
        immagini.add(new byte[] {23, 81, 120});
        immagini.add(new byte[] {24, 29, 85});
        immagini.add(new byte[] {-100, -20, -14});

        return new AstaInversa("Moda",
                                "Portafogli Carpisa", 
                                "Portafogli Carpisa capiente con cerniera.",
                                LocalDate.of(2022, 9, 10),
                                LocalTime.of(17, 44),
                                immagini,
                                notificheAssociate,
                                proprietario,
                                BigDecimal.valueOf(3.00),
                                offerteRicevute);
    }
}

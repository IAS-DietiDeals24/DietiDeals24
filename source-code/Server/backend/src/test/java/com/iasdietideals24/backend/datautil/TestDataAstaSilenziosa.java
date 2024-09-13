package com.iasdietideals24.backend.datautil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.LinkedHashSet;

import com.iasdietideals24.backend.entities.AstaSilenziosa;
import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.entities.OffertaSilenziosa;

public final class TestDataAstaSilenziosa {
    
    private TestDataAstaSilenziosa(){
    }

    public static AstaSilenziosa createAstaSilenziosaA(final Venditore proprietario) {
        return new AstaSilenziosa("Automobili",
                                "Porsche 911", 
                                "Porsche 911 di mio padre. Vendo perché ora abbiamo una Lamborghini Aventador.",
                                LocalDate.of(2025, 1, 11),
                                LocalTime.of(10, 38),
                                null,
                                proprietario);
    }

    public static AstaSilenziosa createAstaSilenziosaB(final Venditore proprietario) {
        return new AstaSilenziosa("Manga",
                                "Jujutsu Kaisen vol. 4 di Gege Akutami", 
                                "Volume 4 del manga Jujutsu Kaisen in ottime condizioni. Leggermente rovinato agli angoli.",
                                LocalDate.of(2024, 12, 24),
                                LocalTime.of(18, 22),
                                new byte[] {-93, -32, 91},
                                proprietario);
    }

    public static AstaSilenziosa createAstaSilenziosaC(final Venditore proprietario) {
        return new AstaSilenziosa("Musica",
                                "Reputation di Taylor Swift", 
                                "CD dell'album Reputation di Taylor Swift semi nuovo. Poster mancante.",
                                LocalDate.of(2024, 6, 19),
                                LocalTime.of(18, 35),
                                new byte[] {-69, 18, 51},
                                proprietario);
    }
}

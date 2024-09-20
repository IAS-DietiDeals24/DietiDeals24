package com.iasdietideals24.backend.datautil;

import java.time.LocalDate;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;

public class TestDataProfilo {
    
    private TestDataProfilo(){
    }

    public static Profilo createProfiloA(final Account account) {
        return new Profilo("pip.baud",
                            new byte[] {-99, -29, 54},
                            "Pippo",
                            "Baudo",
                            LocalDate.of(1936, 6, 7),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            account);
    }

    public static Profilo createProfiloB(final Account account) {
        return new Profilo("EleNoire",
                            new byte[] {-125, -52, 69},
                            "Elenoire",
                            "Ferruzzi",
                            LocalDate.of(1976, 6, 24),
                            "Firenze (FI)",
                            "Grande amante della moda, sopratutto delle borse di Carpisa.",
                            "https://www.elenoireferruzzi.com/",
                            "https://www.facebook.com/",
                            "https://www.instagram.com/",
                            "https://github.com/",
                            "https://x.com/",
                            account);
    }

    public static Profilo createProfiloC(final Account account) {
        return new Profilo("simone.scisciola03",
                            new byte[] {59, 108, 123},
                            "Simone",
                            "Scisciola",
                            LocalDate.of(1995, 8, 1),
                            "Napoli (NA)",
                            "Venditore principalmente di oggettistica a tema manga.",
                            "https://www.simonescisciola03.com/",
                            null,
                            null,
                            null,
                            null,
                            account);
    }

    public static Profilo createProfiloCompratoreA() throws InvalidParameterException {
        return new Profilo("pip.baud",
                            new byte[] {-99, -29, 54},
                            "Pippo",
                            "Baudo",
                            LocalDate.of(1936, 6, 7),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            "pippo.baudo@gmail.com",
                            "buonasera",
                            "Compratore");
    }

    public static Profilo createProfiloCompratoreB() throws InvalidParameterException {
        return new Profilo("EleNoire",
                            new byte[] {-125, -52, 69},
                            "Elenoire",
                            "Ferruzzi",
                            LocalDate.of(1976, 6, 24),
                            "Firenze (FI)",
                            "Grande amante della moda, sopratutto delle borse di Carpisa.",
                            "https://www.elenoireferruzzi.com/",
                            "https://www.facebook.com/",
                            "https://www.instagram.com/",
                            "https://github.com/",
                            "https://x.com/",
                            "elenoire.ferruzzi@gmail.com",
                            "ciaoMamma",
                            "Compratore");
    }

    public static Profilo createProfiloCompratoreC() throws InvalidParameterException {
        return new Profilo("simone.scisciola03",
                            new byte[] {59, 108, 123},
                            "Simone",
                            "Scisciola",
                            LocalDate.of(1995, 8, 1),
                            "Napoli (NA)",
                            "Venditore principalmente di oggettistica a tema manga.",
                            "https://www.simonescisciola03.com/",
                            null,
                            null,
                            null,
                            null,
                            "alessandro.siani@gmail.com",
                            "grandeMezzo",
                            "Compratore");
    }

    public static Profilo createProfiloVenditoreA() throws InvalidParameterException {
        return new Profilo("pip.baud",
                            new byte[] {-99, -29, 54},
                            "Pippo",
                            "Baudo",
                            LocalDate.of(1936, 6, 7),
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            "scis@gmail.com",
                            "myCars",
                            "Venditore");
    }

    public static Profilo createProfiloVenditoreB() throws InvalidParameterException {
        return new Profilo("EleNoire",
                            new byte[] {-125, -52, 69},
                            "Elenoire",
                            "Ferruzzi",
                            LocalDate.of(1976, 6, 24),
                            "Firenze (FI)",
                            "Grande amante della moda, sopratutto delle borse di Carpisa.",
                            "https://www.elenoireferruzzi.com/",
                            "https://www.facebook.com/",
                            "https://www.instagram.com/",
                            "https://github.com/",
                            "https://x.com/",
                            "andrex@gmail.com",
                            "forzaNapoli",
                            "Venditore");
    }

    public static Profilo createProfiloVenditoreC() throws InvalidParameterException {
        return new Profilo("simone.scisciola03",
                            new byte[] {59, 108, 123},
                            "Simone",
                            "Scisciola",
                            LocalDate.of(1995, 8, 1),
                            "Napoli (NA)",
                            "Venditore principalmente di oggettistica a tema manga.",
                            "https://www.simonescisciola03.com/",
                            null,
                            null,
                            null,
                            null,
                            "bluelily@gmail.com",
                            "forzaFedericoII",
                            "Venditore");
    }
}

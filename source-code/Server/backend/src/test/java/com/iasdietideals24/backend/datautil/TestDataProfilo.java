package com.iasdietideals24.backend.datautil;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.entities.utilities.AnagraficaProfilo;
import com.iasdietideals24.backend.entities.utilities.LinksProfilo;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;

import java.time.LocalDate;

public class TestDataProfilo {

    private TestDataProfilo() {
    }

    public static Profilo createProfiloA(final Account account) {
        AnagraficaProfilo anagraficaProfilo = new AnagraficaProfilo(
                "Pippo",
                "Baudo",
                LocalDate.of(1936, 6, 7),
                null,
                null);

        return new Profilo("pip.baud",
                new byte[]{-99, -29, 54},
                anagraficaProfilo,
                null,
                account);
    }

    public static Profilo createProfiloB(final Account account) {
        AnagraficaProfilo anagraficaProfilo = new AnagraficaProfilo(
                "Elenoire",
                "Ferruzzi",
                LocalDate.of(1976, 6, 24),
                "Firenze (FI)",
                "Grande amante della moda, sopratutto delle borse di Carpisa.");

        LinksProfilo linksProfilo = new LinksProfilo(
                "https://www.elenoireferruzzi.com/",
                "https://www.facebook.com/",
                "https://www.instagram.com/",
                "https://github.com/",
                "https://x.com/");

        return new Profilo("EleNoire",
                new byte[]{-125, -52, 69},
                anagraficaProfilo,
                linksProfilo,
                account);
    }

    public static Profilo createProfiloC(final Account account) {
        AnagraficaProfilo anagraficaProfilo = new AnagraficaProfilo(
                "Simone",
                "Scisciola",
                LocalDate.of(1995, 8, 1),
                "Napoli (NA)",
                "Venditore principalmente di oggettistica a tema manga.");

        LinksProfilo linksProfilo = new LinksProfilo(
                "https://www.simonescisciola03.com/",
                null,
                null,
                null,
                null);

        return new Profilo("simone.scisciola03",
                new byte[]{59, 108, 123},
                anagraficaProfilo,
                linksProfilo,
                account);
    }

    public static Profilo createProfiloCompratoreA() throws InvalidTypeException {
        AnagraficaProfilo anagraficaProfilo = new AnagraficaProfilo(
                "Pippo",
                "Baudo",
                LocalDate.of(1936, 6, 7),
                null,
                null);

        return new Profilo("pip.baud",
                new byte[]{-99, -29, 54},
                anagraficaProfilo,
                null,
                "pippo.baudo@gmail.com",
                "buonasera",
                null,
                "Compratore");
    }

    public static Profilo createProfiloCompratoreB() throws InvalidTypeException {
        AnagraficaProfilo anagraficaProfilo = new AnagraficaProfilo(
                "Elenoire",
                "Ferruzzi",
                LocalDate.of(1976, 6, 24),
                "Firenze (FI)",
                "Grande amante della moda, sopratutto delle borse di Carpisa.");

        LinksProfilo linksProfilo = new LinksProfilo(
                "https://www.elenoireferruzzi.com/",
                "https://www.facebook.com/",
                "https://www.instagram.com/",
                "https://github.com/",
                "https://x.com/");

        return new Profilo("EleNoire",
                new byte[]{-125, -52, 69},
                anagraficaProfilo,
                linksProfilo,
                "elenoire.ferruzzi@gmail.com",
                "ciaoMamma",
                null,
                "Compratore");
    }

    public static Profilo createProfiloCompratoreC() throws InvalidTypeException {
        AnagraficaProfilo anagraficaProfilo = new AnagraficaProfilo(
                "Simone",
                "Scisciola",
                LocalDate.of(1995, 8, 1),
                "Napoli (NA)",
                "Venditore principalmente di oggettistica a tema manga.");

        LinksProfilo linksProfilo = new LinksProfilo(
                "https://www.simonescisciola03.com/",
                null,
                null,
                null,
                null);

        return new Profilo("simone.scisciola03",
                new byte[]{59, 108, 123},
                anagraficaProfilo,
                linksProfilo,
                "alessandro.siani@gmail.com",
                "grandeMezzo",
                null,
                "Compratore");
    }

    public static Profilo createProfiloVenditoreA() throws InvalidTypeException {
        AnagraficaProfilo anagraficaProfilo = new AnagraficaProfilo(
                "Pippo",
                "Baudo",
                LocalDate.of(1936, 6, 7),
                null,
                null);

        return new Profilo("pip.baud",
                new byte[]{-99, -29, 54},
                anagraficaProfilo,
                null,
                "scis@gmail.com",
                "myCars",
                null,
                "Venditore");
    }

    public static Profilo createProfiloVenditoreB() throws InvalidTypeException {
        AnagraficaProfilo anagraficaProfilo = new AnagraficaProfilo(
                "Elenoire",
                "Ferruzzi",
                LocalDate.of(1976, 6, 24),
                "Firenze (FI)",
                "Grande amante della moda, sopratutto delle borse di Carpisa.");

        LinksProfilo linksProfilo = new LinksProfilo(
                "https://www.elenoireferruzzi.com/",
                "https://www.facebook.com/",
                "https://www.instagram.com/",
                "https://github.com/",
                "https://x.com/");

        return new Profilo("EleNoire",
                new byte[]{-125, -52, 69},
                anagraficaProfilo,
                linksProfilo,
                "andrex@gmail.com",
                "forzaNapoli",
                null,
                "Venditore");
    }

    public static Profilo createProfiloVenditoreC() throws InvalidTypeException {
        AnagraficaProfilo anagraficaProfilo = new AnagraficaProfilo(
                "Simone",
                "Scisciola",
                LocalDate.of(1995, 8, 1),
                "Napoli (NA)",
                "Venditore principalmente di oggettistica a tema manga.");

        LinksProfilo linksProfilo = new LinksProfilo(
                "https://www.simonescisciola03.com/",
                null,
                null,
                null,
                null);

        return new Profilo("simone.scisciola03",
                new byte[]{59, 108, 123},
                anagraficaProfilo,
                linksProfilo,
                "bluelily@gmail.com",
                "forzaFedericoII",
                null,
                "Venditore");
    }
}

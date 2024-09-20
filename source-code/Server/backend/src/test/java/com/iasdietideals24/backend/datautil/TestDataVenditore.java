package com.iasdietideals24.backend.datautil;

import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.entities.Venditore;

public class TestDataVenditore {

    private TestDataVenditore() {
    }

    public static Venditore createVenditoreA(final Profilo profilo) {
        return new Venditore("scis@gmail.com",
                "myCars",
                profilo);
    }

    public static Venditore createVenditoreB(final Profilo profilo) {
        return new Venditore("andrex@gmail.com",
                "forzaNapoli",
                profilo);
    }

    public static Venditore createVenditoreC(final Profilo profilo) {
        return new Venditore("bluelily@gmail.com",
                "forzaFedericoII",
                profilo);
    }
}

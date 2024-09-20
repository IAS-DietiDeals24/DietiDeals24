package com.iasdietideals24.backend.datautil;

import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.Profilo;

public class TestDataCompratore {

    private TestDataCompratore() {
    }

    public static Compratore createCompratoreA(final Profilo profilo) {
        return new Compratore("pippo.baudo@gmail.com",
                "buonasera",
                profilo);
    }

    public static Compratore createCompratoreB(final Profilo profilo) {
        return new Compratore("elenoire.ferruzzi@gmail.com",
                "ciaoMamma",
                profilo);
    }

    public static Compratore createCompratoreC(final Profilo profilo) {
        return new Compratore("alessandro.siani@gmail.com",
                "grandeMezzo",
                profilo);
    }
}

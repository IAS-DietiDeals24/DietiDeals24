package com.iasdietideals24.backend.datautil;

import java.util.Set;

import com.iasdietideals24.backend.entities.AstaDiVenditore;
import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.entities.OffertaDiVenditore;
import com.iasdietideals24.backend.entities.Profilo;

public class TestDataVenditore {
    
    private TestDataVenditore(){
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

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
                                profilo,
                                null,
                                null,
                                null,
                                null);
    }

    public static Venditore createVenditoreB(final Profilo profilo, final Set<Notifica> notificheInviate, final Set<Notifica> notificheRicevute, final Set<AstaDiVenditore> astePossedute, final Set<OffertaDiVenditore> offerteCollegate) {
        return new Venditore("andrex@gmail.com",
                                "forzaNapoli",
                                profilo,
                                notificheInviate,
                                notificheRicevute,
                                astePossedute,
                                offerteCollegate);
    }

    public static Venditore createVenditoreC(final Profilo profilo, final Set<Notifica> notificheInviate, final Set<Notifica> notificheRicevute, final Set<AstaDiVenditore> astePossedute, final Set<OffertaDiVenditore> offerteCollegate) {
        return new Venditore("bluelily@gmail.com",
                                "forzaFedericoII",
                                profilo,
                                notificheInviate,
                                notificheRicevute,
                                astePossedute,
                                offerteCollegate);
    }
}

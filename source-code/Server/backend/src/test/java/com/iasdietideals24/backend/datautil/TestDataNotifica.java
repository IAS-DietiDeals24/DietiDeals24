package com.iasdietideals24.backend.datautil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.Asta;
import com.iasdietideals24.backend.entities.Notifica;

public class TestDataNotifica {

    private TestDataNotifica(){
    }

    public static Notifica createNotificaA(final Account mittente, final Account destinatario, final Asta astaAssociata) {
        return new Notifica(LocalDate.of(2024, 11, 10),
                            LocalTime.of(15, 20),
                            "ha offerto 50,00 € alla tua asta silenziosa \"Jujutsu Kaisen vol. 4 di Gege Akutami\"",
                            mittente,
                            destinatario,
                            astaAssociata);
    }

    public static Notifica createNotificaB(final Account mittente, final Account destinatario, final Asta astaAssociata) {
        return new Notifica(LocalDate.of(2020, 1, 13),
                            LocalTime.of(17, 40),
                            "ha offerto 30,00 € alla tua asta a tempo fisso \"Shadow of the Tomb Raider PS4\"",
                            mittente,
                            destinatario,
                            astaAssociata);
    }

    public static Notifica createNotificaC(final Account mittente, final Account destinatario, final Asta astaAssociata) {
        return new Notifica(LocalDate.of(2023, 4, 20),
                            LocalTime.of(11, 40),
                            "ha offerto 8,30 € alla tua asta inversa \"Borsa Carpisa\"",
                            mittente,
                            destinatario,
                            astaAssociata);
    }
}

package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.entities.AstaInversa;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.exceptions.ParameterNotValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@Slf4j
public class AstaInversaController {

    public static void main(String[] args) throws ParameterNotValidException {
        Profilo pr = new Profilo("pip.baud",
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

        Compratore p = new Compratore("elenoire.ferruzzi@gmail.com",
                "ciaoMamma",
                pr);

        AstaInversa a = new AstaInversa("Videogiochi",
                "Dragon Age: Origins Xbox 360",
                "Edizione Xbox 360 del videogioco Dragon Age: Origins. Ci giocava mio marito.",
                LocalDate.of(2024, 6, 19),
                LocalTime.of(18, 44),
                null,
                p,
                BigDecimal.valueOf(1.00));

        log.info(pr.toString());
        log.info(p.toString());
        log.info(a.toString());
    }

}

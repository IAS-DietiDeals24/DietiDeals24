package com.iasdietideals24.backend.scheduled;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.entities.utilities.StatoAsta;
import com.iasdietideals24.backend.repositories.AstaRepository;
import com.iasdietideals24.backend.services.AstaInversaService;
import com.iasdietideals24.backend.services.AstaSilenziosaService;
import com.iasdietideals24.backend.services.AstaTempoFissoService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
public class AstaScheduler {

    private final AstaRepository astaRepository;
    private final AstaInversaService astaInversaService;
    private final AstaSilenziosaService astaSilenziosaService;
    private final AstaTempoFissoService astaTempoFissoService;

    public AstaScheduler(AstaRepository astaRepository,
                         AstaInversaService astaInversaService,
                         AstaSilenziosaService astaSilenziosaService,
                         AstaTempoFissoService astaTempoFissoService) {
        this.astaRepository = astaRepository;
        this.astaInversaService = astaInversaService;
        this.astaSilenziosaService = astaSilenziosaService;
        this.astaTempoFissoService = astaTempoFissoService;
    }

    @Scheduled(fixedDelay = 30000) // Ogni 30s questo metodo viene chiamato
    @Transactional
    public void updateExpiredAste() {

        log.debug("Verifico se ci sono nuove aste scadute...");

        List<Asta> expiredAste = astaRepository
                .findByDataScadenzaIsAfterAndOraScadenzaIsAfterAndStatoIs(LocalDate.now(), LocalTime.now(), StatoAsta.ACTIVE, Pageable.unpaged())
                .toList();

        if (!expiredAste.isEmpty()) {
            for (Asta expiredAsta : expiredAste ) {

                if (expiredAsta instanceof AstaInversa expiredAstaInversa)
                    astaInversaService.closeAstaInversa(expiredAstaInversa);
                else if (expiredAsta instanceof AstaSilenziosa expiredAstaSilenziosa)
                    astaSilenziosaService.closeAstaSilenziosa(expiredAstaSilenziosa);
                else if (expiredAsta instanceof AstaTempoFisso expiredAstaTempoFisso)
                    astaTempoFissoService.closeAstaTempoFisso(expiredAstaTempoFisso);
            }

            log.debug("Aste scadute aggiornate.");
        } else {
            log.debug("Non ci sono nuove aste scadute.");
        }
    }
}

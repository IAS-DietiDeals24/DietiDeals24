package com.iasdietideals24.backend.scheduled;

import com.iasdietideals24.backend.entities.Asta;
import com.iasdietideals24.backend.entities.utilities.StatoAsta;
import com.iasdietideals24.backend.repositories.AstaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
public class AstaWatcher {

    private final AstaRepository astaRepository;

    public AstaWatcher(final AstaRepository astaRepository) {
        this.astaRepository = astaRepository;
    }

    @Scheduled(fixedDelay = 5000)
    public void manageExpiredAste() {

        List<Asta> expiredAste = astaRepository
                .findByDataScadenzaIsAfterAndOraScadenzaIsAfterAndStatoIs(LocalDate.now(), LocalTime.now(), StatoAsta.ACTIVE, Pageable.unpaged())
                .toList();

        if (!expiredAste.isEmpty()) {
            for (Asta expiredAsta : expiredAste ) {
                closeAsta(expiredAsta);
            }
        }
    }

    private void closeAsta(Asta expiredAsta) {

        log.info("{}", LocalDateTime.now());
        log.info("Asta scaduta: {}", expiredAsta.getIdAsta());

    }
}

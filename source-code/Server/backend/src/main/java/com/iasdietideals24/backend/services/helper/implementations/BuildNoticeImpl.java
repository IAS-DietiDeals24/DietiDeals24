package com.iasdietideals24.backend.services.helper.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.repositories.*;
import com.iasdietideals24.backend.services.helper.BuildNotice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class BuildNoticeImpl implements BuildNotice {

    public static final String LOG_COSTRUZIONE_NOTIFICA = "Costruisco la notifica...";
    public static final String NUOVA_OFFERTA = "Hai ricevuto una nuova offerta per la tua asta";
    public static final String OFFERTA_SILENZIOSA_RIFIUTATA = "La tua offerta silenziosa è stata rifiutata";
    public static final String OFFERTA_SILENZIOSA_ACCETTATA = "La tua offerta silenziosa è stata accettata";

    private final NotificaRepository notificaRepository;
    private final AstaInversaRepository astaInversaRepository;

    public BuildNoticeImpl(NotificaRepository notificaRepository,
                           AstaInversaRepository astaInversaRepository) {
        this.notificaRepository = notificaRepository;
        this.astaInversaRepository = astaInversaRepository;
    }

    private void createNotificaAstaEliminata(Long idAsta) {

        Optional<AstaInversa> foundAsta = astaInversaRepository.findById(idAsta);

        if (foundAsta.isPresent()) {

            AstaInversa astaInversa = foundAsta.get();

            Set<OffertaInversa> offerteCollegate = astaInversa.getOfferteRicevute();
            if (!offerteCollegate.isEmpty()) {

                log.debug(LOG_COSTRUZIONE_NOTIFICA);

                Notifica notifica = new Notifica(
                        LocalDate.now(),
                        LocalTime.now(),
                        "L'asta a cui hai partecipato è stata eliminata.",
                        astaInversa.getProprietario(),
                        offerteCollegate.iterator().next().getVenditoreCollegato(),
                        astaInversa
                );

                for (OffertaInversa offertaInversa : offerteCollegate) {
                    Venditore venditore = offertaInversa.getVenditoreCollegato();
                    notifica.addDestinatario(venditore);
                    venditore.addNotificaRicevuta(notifica);
                }

                sendNotifica(notifica);
            }
        }
    }

    @Override
    public void notifyNuovaOfferta(OffertaInversa offertaInversa) {

        if (offertaInversa != null) {

            AstaInversa astaInversa = offertaInversa.getAstaRiferimento();

            log.debug(LOG_COSTRUZIONE_NOTIFICA);

            Notifica notifica = new Notifica(
                    LocalDate.now(),
                    LocalTime.now(),
                    NUOVA_OFFERTA,
                    offertaInversa.getVenditoreCollegato(),
                    astaInversa.getProprietario(),
                    astaInversa
            );

            sendNotifica(notifica);
        }
    }

    @Override
    public void notifyNuovaOfferta(OffertaSilenziosa offertaSilenziosa) {

        if (offertaSilenziosa != null) {

            AstaSilenziosa astaSilenziosa = offertaSilenziosa.getAstaRiferimento();

            log.debug(LOG_COSTRUZIONE_NOTIFICA);

            Notifica notifica = new Notifica(
                    LocalDate.now(),
                    LocalTime.now(),
                    NUOVA_OFFERTA,
                    offertaSilenziosa.getCompratoreCollegato(),
                    astaSilenziosa.getProprietario(),
                    astaSilenziosa
            );

            sendNotifica(notifica);
        }
    }

    @Override
    public void notifyNuovaOfferta(OffertaTempoFisso offertaTempoFisso) {

        if (offertaTempoFisso != null) {

            AstaTempoFisso astaTempoFisso = offertaTempoFisso.getAstaRiferimento();

            log.debug(LOG_COSTRUZIONE_NOTIFICA);

            Notifica notifica = new Notifica(
                    LocalDate.now(),
                    LocalTime.now(),
                    NUOVA_OFFERTA,
                    offertaTempoFisso.getCompratoreCollegato(),
                    astaTempoFisso.getProprietario(),
                    astaTempoFisso
            );

            sendNotifica(notifica);
        }
    }

    @Override
    public void notifyOffertaSilenziosaRifiutata(OffertaSilenziosa offertaSilenziosa) {

        if (offertaSilenziosa != null) {

            AstaSilenziosa astaSilenziosa = offertaSilenziosa.getAstaRiferimento();

            log.debug(LOG_COSTRUZIONE_NOTIFICA);

            Notifica notifica = new Notifica(
                    LocalDate.now(),
                    LocalTime.now(),
                    OFFERTA_SILENZIOSA_RIFIUTATA,
                    astaSilenziosa.getProprietario(),
                    offertaSilenziosa.getCompratoreCollegato(),
                    astaSilenziosa
            );

            sendNotifica(notifica);
        }
    }

    @Override
    public void notifyOffertaSilenziosaRifiutata(Set<OffertaSilenziosa> offerteSilenziose) {

        if (offerteSilenziose != null) {

            Iterator<OffertaSilenziosa> itrOfferteSilenziose = offerteSilenziose.iterator();

            if (itrOfferteSilenziose.hasNext()) {
                OffertaSilenziosa offertaSilenziosa = itrOfferteSilenziose.next();
                AstaSilenziosa astaSilenziosa = offertaSilenziosa.getAstaRiferimento();
                Account destinatario = offertaSilenziosa.getCompratoreCollegato();

                log.debug(LOG_COSTRUZIONE_NOTIFICA);

                Notifica notifica = new Notifica(
                        LocalDate.now(),
                        LocalTime.now(),
                        OFFERTA_SILENZIOSA_RIFIUTATA,
                        astaSilenziosa.getProprietario(),
                        destinatario,
                        astaSilenziosa
                );

                while(itrOfferteSilenziose.hasNext()) {
                    notifica.addDestinatario(itrOfferteSilenziose.next().getCompratoreCollegato());
                }

                sendNotifica(notifica);
            }
        }
    }

    @Override
    public void notifyOffertaSilenziosaAccettata(OffertaSilenziosa offertaSilenziosa) {

        if (offertaSilenziosa != null) {

            AstaSilenziosa astaSilenziosa = offertaSilenziosa.getAstaRiferimento();

            log.debug(LOG_COSTRUZIONE_NOTIFICA);

            Notifica notifica = new Notifica(
                    LocalDate.now(),
                    LocalTime.now(),
                    OFFERTA_SILENZIOSA_ACCETTATA,
                    astaSilenziosa.getProprietario(),
                    offertaSilenziosa.getCompratoreCollegato(),
                    astaSilenziosa
            );

            sendNotifica(notifica);
        }
    }

    private void sendNotifica(Notifica notifica) {

        log.trace("notifica: {}", notifica);
        log.debug("Notifica costruita correttamente. Salvo la notifica nel database...");

        Notifica savedNotifica = notificaRepository.save(notifica);

        log.trace("savedNotifica: {}", savedNotifica);
        log.debug("Notifica salvata correttamente nel database con id notifica {}...", savedNotifica.getIdNotifica());
    }
}

package com.iasdietideals24.backend.services.helper.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.repositories.*;
import com.iasdietideals24.backend.services.helper.BuildNotice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Set;

@Slf4j
@Component
public class BuildNoticeImpl implements BuildNotice {

    public static final String LOG_COSTRUZIONE_NOTIFICA = "Costruisco la notifica...";

    public static final String NUOVA_OFFERTA = "Hai ricevuto una nuova offerta per la tua asta";
    public static final String OFFERTA_SILENZIOSA_RIFIUTATA = "La tua offerta silenziosa è stata rifiutata";
    public static final String OFFERTA_SILENZIOSA_ACCETTATA = "La tua offerta silenziosa è stata accettata";
    public static final String ASTA_SCADUTA = "La tua asta è scaduta";
    public static final String ASTA_PERSA = "Hai perso l'asta";
    public static final String ASTA_VINTA = "Hai vinto l'asta";

    private final NotificaRepository notificaRepository;

    public BuildNoticeImpl(NotificaRepository notificaRepository) {
        this.notificaRepository = notificaRepository;
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

    @Override
    public void notifyAstaInversaScaduta(AstaInversa astaInversa) {

        if (astaInversa != null) {

            log.debug(LOG_COSTRUZIONE_NOTIFICA);

            Notifica notifica = new Notifica(
                    LocalDate.now(),
                    LocalTime.now(),
                    ASTA_SCADUTA,
                    astaInversa.getProprietario(),
                    astaInversa.getProprietario(),
                    astaInversa
            );

            sendNotifica(notifica);
        }
    }

    @Override
    public void notifyAstaSilenziosaScaduta(AstaSilenziosa astaSilenziosa) {

        if (astaSilenziosa != null) {

            log.debug(LOG_COSTRUZIONE_NOTIFICA);

            Notifica notifica = new Notifica(
                    LocalDate.now(),
                    LocalTime.now(),
                    ASTA_SCADUTA,
                    astaSilenziosa.getProprietario(),
                    astaSilenziosa.getProprietario(),
                    astaSilenziosa
            );

            sendNotifica(notifica);
        }
    }

    @Override
    public void notifyAstaTempoFissoScaduta(AstaTempoFisso astaTempoFisso) {

        if (astaTempoFisso != null) {

            log.debug(LOG_COSTRUZIONE_NOTIFICA);

            Notifica notifica = new Notifica(
                    LocalDate.now(),
                    LocalTime.now(),
                    ASTA_SCADUTA,
                    astaTempoFisso.getProprietario(),
                    astaTempoFisso.getProprietario(),
                    astaTempoFisso
            );

            sendNotifica(notifica);
        }
    }

    @Override
    public void notifyOffertaInversaVincitrice(OffertaInversa offertaVincitrice) {

        if (offertaVincitrice != null) {

            AstaInversa astaInversa = offertaVincitrice.getAstaRiferimento();

            log.debug(LOG_COSTRUZIONE_NOTIFICA);

            Notifica notifica = new Notifica(
                    LocalDate.now(),
                    LocalTime.now(),
                    ASTA_VINTA,
                    astaInversa.getProprietario(),
                    offertaVincitrice.getVenditoreCollegato(),
                    astaInversa
            );

            sendNotifica(notifica);
        }
    }

    @Override
    public void notifyOffertaInversaPerdente(Set<OffertaInversa> offertePerdeti) {

        if (offertePerdeti != null) {

            Iterator<OffertaInversa> itrOffertePerdenti = offertePerdeti.iterator();

            if (itrOffertePerdenti.hasNext()) {
                OffertaInversa offertaPerdente = itrOffertePerdenti.next();
                AstaInversa astaInversa = offertaPerdente.getAstaRiferimento();
                Account destinatario = offertaPerdente.getVenditoreCollegato();

                log.debug(LOG_COSTRUZIONE_NOTIFICA);

                Notifica notifica = new Notifica(
                        LocalDate.now(),
                        LocalTime.now(),
                        ASTA_PERSA,
                        astaInversa.getProprietario(),
                        destinatario,
                        astaInversa
                );

                while(itrOffertePerdenti.hasNext()) {
                    notifica.addDestinatario(itrOffertePerdenti.next().getVenditoreCollegato());
                }

                sendNotifica(notifica);
            }
        }
    }

    @Override
    public void notifyOffertaTempoFissoVincitrice(OffertaTempoFisso offertaVincitrice) {

        if (offertaVincitrice != null) {

            AstaTempoFisso astaTempoFisso = offertaVincitrice.getAstaRiferimento();

            log.debug(LOG_COSTRUZIONE_NOTIFICA);

            Notifica notifica = new Notifica(
                    LocalDate.now(),
                    LocalTime.now(),
                    ASTA_VINTA,
                    astaTempoFisso.getProprietario(),
                    offertaVincitrice.getCompratoreCollegato(),
                    astaTempoFisso
            );

            sendNotifica(notifica);
        }
    }

    @Override
    public void notifyOffertaTempoFissoPerdente(Set<OffertaTempoFisso> offertePerdeti) {

        if (offertePerdeti != null) {

            Iterator<OffertaTempoFisso> itrOffertePerdenti = offertePerdeti.iterator();

            if (itrOffertePerdenti.hasNext()) {
                OffertaTempoFisso offertaPerdente = itrOffertePerdenti.next();
                AstaTempoFisso astaTempoFisso = offertaPerdente.getAstaRiferimento();
                Account destinatario = offertaPerdente.getCompratoreCollegato();

                log.debug(LOG_COSTRUZIONE_NOTIFICA);

                Notifica notifica = new Notifica(
                        LocalDate.now(),
                        LocalTime.now(),
                        ASTA_PERSA,
                        astaTempoFisso.getProprietario(),
                        destinatario,
                        astaTempoFisso
                );

                while(itrOffertePerdenti.hasNext()) {
                    notifica.addDestinatario(itrOffertePerdenti.next().getCompratoreCollegato());
                }

                sendNotifica(notifica);
            }
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

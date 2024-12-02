package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Offerta;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaDto;
import com.iasdietideals24.backend.services.OffertaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@Service
public class OffertaServiceImpl implements OffertaService {

    @Override
    public void checkFieldsValid(OffertaDto offertaDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di offerta...");

        checkDataInvioValid(offertaDto.getDataInvio());
        checkOraInvioValid(offertaDto.getDataInvio(), offertaDto.getOraInvio());
        checkValoreValid(offertaDto.getValore());

        log.debug("Integrità dei dati di offerta verificata.");
    }

    private void checkDataInvioValid(LocalDate dataInvio) throws InvalidParameterException {

        log.trace("Controllo che 'dataInvio' sia valido...");

        if (dataInvio == null)
            throw new InvalidParameterException("La data di invio non può essere null!");
        else if (dataInvio.isAfter(LocalDate.now()))
            throw new InvalidParameterException("La data di invio non può essere successiva alla data odierna!");

        log.trace("'dataInvio' valido.");
    }

    private void checkOraInvioValid(LocalDate dataInvio, LocalTime oraInvio) throws InvalidParameterException {

        log.trace("Controllo che 'oraInvio' sia valido...");

        if (oraInvio == null)
            throw new InvalidParameterException("L'ora di invio non può essere null!");
        else if (dataInvio.isEqual(LocalDate.now()) && oraInvio.isAfter(LocalTime.now()))
            throw new InvalidParameterException("L'ora di invio non può essere successiva all'ora attuale!");

        log.trace("'oraInvio' valido.");
    }

    private void checkValoreValid(BigDecimal valore) throws InvalidParameterException {

        log.trace("Controllo che 'valore' sia valido...");

        if (valore == null)
            throw new InvalidParameterException("Il valore non può essere null!");
        else if (valore.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidParameterException("Il valore deve essere positivo!");

        log.trace("'valore' valido.");
    }

    @Override
    public void convertRelations(OffertaDto offertaDto, Offerta offerta) throws InvalidParameterException {

        log.debug("Recupero le associazioni di offerta...");

        // Non ci sono relazioni

        log.debug("Associazioni di offerta recuperate.");
    }

    @Override
    public void updatePresentFields(OffertaDto updatedOffertaDto, Offerta existingOfferta) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di offerta richieste...");

        ifPresentUpdateDataInvio(updatedOffertaDto.getDataInvio(), existingOfferta);
        ifPresentUpdateOraInvio(updatedOffertaDto.getOraInvio(), existingOfferta);
        ifPresentUpdateValore(updatedOffertaDto.getValore(), existingOfferta);

        log.debug("Modifiche di offerta effettuate correttamente.");
    }

    private void ifPresentUpdateDataInvio(LocalDate updatedDataInvio, Offerta existingOfferta) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'dataInvio'...");

        if (updatedDataInvio != null) {
            checkDataInvioValid(updatedDataInvio);
            existingOfferta.setDataInvio(updatedDataInvio);
        }

        log.trace("'dataInvio' modificato correttamente.");
    }

    private void ifPresentUpdateOraInvio(LocalTime updatedOraInvio, Offerta existingOfferta) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'oraInvio'...");

        if (updatedOraInvio != null) {
            checkOraInvioValid(existingOfferta.getDataInvio(), updatedOraInvio);
            existingOfferta.setOraInvio(updatedOraInvio);
        }

        log.trace("'oraInvio' modificato correttamente.");
    }

    private void ifPresentUpdateValore(BigDecimal updatedValore, Offerta existingOfferta) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'valore'...");

        if (updatedValore != null) {
            checkValoreValid(updatedValore);
            existingOfferta.setValore(updatedValore);
        }

        log.trace("'valore' modificato correttamente.");
    }
}

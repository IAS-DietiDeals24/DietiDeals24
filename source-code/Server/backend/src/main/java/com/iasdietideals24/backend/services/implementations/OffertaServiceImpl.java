package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Offerta;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaDto;
import com.iasdietideals24.backend.services.OffertaService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class OffertaServiceImpl implements OffertaService {
    @Override
    public void checkFieldsValid(OffertaDto offertaDto) throws InvalidParameterException {
        checkDataInvioValid(offertaDto.getDataInvio());
        checkOraInvioValid(offertaDto.getOraInvio());
        checkValoreValid(offertaDto.getValore());
    }

    private void checkDataInvioValid(LocalDate dataInvio) throws InvalidParameterException {
        if (dataInvio == null)
            throw new InvalidParameterException("La data di invio non può essere null!");
        else if (dataInvio.isAfter(LocalDate.now()))
            throw new InvalidParameterException("La data di invio non può essere successiva alla data odierna!");
    }

    private void checkOraInvioValid(LocalTime oraInvio) throws InvalidParameterException {
        if (oraInvio == null)
            throw new InvalidParameterException("L'ora di invio non può essere null!");
        else if (oraInvio.isAfter(LocalTime.now()))
            throw new InvalidParameterException("L'ora di invio non può essere successiva all'ora attuale!");
    }

    private void checkValoreValid(BigDecimal valore) throws InvalidParameterException {
        if (valore == null)
            throw new InvalidParameterException("Il valore non può essere null!");
        else if (valore.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidParameterException("Il valore deve essere positivo!");
    }

    @Override
    public void convertRelations(OffertaDto offertaDto, Offerta offerta) throws InvalidParameterException {
        // Non ci sono relazioni
    }

    @Override
    public void updatePresentFields(OffertaDto updatedOffertaDto, Offerta existingOfferta) throws InvalidParameterException {
        ifPresentUpdateDataInvio(updatedOffertaDto.getDataInvio(), existingOfferta);
        ifPresentUpdateOraInvio(updatedOffertaDto.getOraInvio(), existingOfferta);
        ifPresentUpdateValore(updatedOffertaDto.getValore(), existingOfferta);
    }

    private void ifPresentUpdateDataInvio(LocalDate updatedDataInvio, Offerta existingOfferta) throws InvalidParameterException {
        if (updatedDataInvio != null) {
            checkDataInvioValid(updatedDataInvio);
            existingOfferta.setDataInvio(updatedDataInvio);
        }
    }

    private void ifPresentUpdateOraInvio(LocalTime updatedOraInvio, Offerta existingOfferta) throws InvalidParameterException {
        if (updatedOraInvio != null) {
            checkOraInvioValid(updatedOraInvio);
            existingOfferta.setOraInvio(updatedOraInvio);
        }
    }

    private void ifPresentUpdateValore(BigDecimal updatedValore, Offerta existingOfferta) throws InvalidParameterException {
        if (updatedValore != null) {
            checkValoreValid(updatedValore);
            existingOfferta.setValore(updatedValore);
        }
    }
}

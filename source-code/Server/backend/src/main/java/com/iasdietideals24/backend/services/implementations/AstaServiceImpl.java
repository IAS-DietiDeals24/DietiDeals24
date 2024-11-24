package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Asta;
import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AstaDto;
import com.iasdietideals24.backend.mapstruct.dto.CategoriaAstaDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.CategoriaAstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.CategoriaAstaMapper;
import com.iasdietideals24.backend.services.AstaService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Service
public class AstaServiceImpl implements AstaService {

    private final RelationsConverter relationsConverter;

    protected AstaServiceImpl(RelationsConverter relationsConverter) {
        this.relationsConverter = relationsConverter;
    }

    @Override
    public void checkFieldsValid(AstaDto astaDto) throws InvalidParameterException {
        checkNomeValid(astaDto.getNome());
        checkDescrizioneValid(astaDto.getDescrizione());
        checkDataScadenzaValid(astaDto.getDataScadenza());
        checkOraScadenzaValid(astaDto.getDataScadenza(), astaDto.getOraScadenza());
        checkCategoriaValid(astaDto.getCategoriaShallow());
    }

    private void checkCategoriaValid(CategoriaAstaShallowDto categoria) throws InvalidParameterException {
        if (categoria == null)
            throw new InvalidParameterException("La categoria non può essere null!");
    }

    private void checkNomeValid(String nome) throws InvalidParameterException {
        if (nome == null)
            throw new InvalidParameterException("Il nome non può essere null!");
        else if (nome.isBlank())
            throw new InvalidParameterException("Il nome non può essere vuoto!");
    }

    private void checkDescrizioneValid(String descrizione) throws InvalidParameterException {
        if (descrizione == null)
            throw new InvalidParameterException("La descrizione non può essere null!");
        else if (descrizione.isBlank())
            throw new InvalidParameterException("La descrizione non può essere vuoto!");
    }

    private void checkDataScadenzaValid(LocalDate dataScadenza) throws InvalidParameterException {
        if (dataScadenza == null)
            throw new InvalidParameterException("La data di scadenza non può essere null!");
        else if (dataScadenza.isBefore(LocalDate.now()))
            throw new InvalidParameterException("La data di scadenza non può essere precedente alla data odierna!");
    }

    private void checkOraScadenzaValid(LocalDate dataScadenza, LocalTime oraScadenza) throws InvalidParameterException {
        if (oraScadenza == null)
            throw new InvalidParameterException("L'ora di scadenza non può essere null!");
        else if (dataScadenza.isEqual(LocalDate.now()) && oraScadenza.isBefore(LocalTime.now()))
            throw new InvalidParameterException("L'ora di scadenza non può essere precedente all'ora attuale!");
    }

    @Override
    public void convertRelations(AstaDto astaDto, Asta asta) throws InvalidParameterException {
        convertNotificheAssociateShallow(astaDto.getNotificheAssociateShallow(), asta);
    }

    private void convertNotificheAssociateShallow(Set<NotificaShallowDto> notificheAssociateShallow, Asta asta) throws IdNotFoundException {
        if (notificheAssociateShallow != null) {
            for (NotificaShallowDto notificaShallowDto : notificheAssociateShallow) {

                Notifica convertedNotifica = relationsConverter.convertNotificaShallowRelation(notificaShallowDto);

                if (convertedNotifica != null) {
                    asta.addNotificaAssociata(convertedNotifica);
                    convertedNotifica.setAstaAssociata(asta);
                }
            }
        }
    }

    @Override
    public void updatePresentFields(AstaDto updatedAstaDto, Asta existingAsta) throws InvalidParameterException {
        ifPresentUpdateNome(updatedAstaDto.getNome(), existingAsta);
        ifPresentUpdateDescrizione(updatedAstaDto.getDescrizione(), existingAsta);
        ifPresentUpdateDataScadenza(updatedAstaDto.getDataScadenza(), existingAsta);
        ifPresentUpdateOraScadenza(updatedAstaDto.getOraScadenza(), existingAsta);
        ifPresentUpdateImmagine(updatedAstaDto.getImmagine(), existingAsta);

        // Non è possibile modificare l'associazione "categoria", "notificheAssociate" tramite la risorsa "aste"
    }

    private void ifPresentUpdateNome(String updatedNome, Asta existingAsta) throws InvalidParameterException {
        if (updatedNome != null) {
            checkNomeValid(updatedNome);
            existingAsta.setNome(updatedNome);
        }
    }

    private void ifPresentUpdateDescrizione(String updatedDescrizione, Asta existingAsta) throws InvalidParameterException {
        if (updatedDescrizione != null) {
            checkDescrizioneValid(updatedDescrizione);
            existingAsta.setDescrizione(updatedDescrizione);
        }
    }

    private void ifPresentUpdateDataScadenza(LocalDate updatedDataScadenza, Asta existingAsta) throws InvalidParameterException {
        if (updatedDataScadenza != null) {
            checkDataScadenzaValid(updatedDataScadenza);
            existingAsta.setDataScadenza(updatedDataScadenza);
        }
    }

    private void ifPresentUpdateOraScadenza(LocalTime updatedOraScadenza, Asta existingAsta) throws InvalidParameterException {
        if (updatedOraScadenza != null) {
            checkOraScadenzaValid(existingAsta.getDataScadenza(), updatedOraScadenza);
            existingAsta.setOraScadenza(updatedOraScadenza);
        }
    }

    private void ifPresentUpdateImmagine(byte[] updatedImmagine, Asta existingAsta) {
        if (updatedImmagine != null) {
            existingAsta.setImmagine(updatedImmagine);
        }
    }
}

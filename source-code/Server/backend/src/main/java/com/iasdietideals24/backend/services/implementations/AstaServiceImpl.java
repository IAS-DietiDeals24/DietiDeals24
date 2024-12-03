package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Asta;
import com.iasdietideals24.backend.entities.CategoriaAsta;
import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.AstaDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.CategoriaAstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.backend.services.AstaService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Slf4j
@Service
public class AstaServiceImpl implements AstaService {

    private final RelationsConverter relationsConverter;

    protected AstaServiceImpl(RelationsConverter relationsConverter) {
        this.relationsConverter = relationsConverter;
    }

    @Override
    public void checkFieldsValid(AstaDto astaDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di asta...");

        checkNomeValid(astaDto.getNome());
        checkDescrizioneValid(astaDto.getDescrizione());
        checkDataScadenzaValid(astaDto.getDataScadenza());
        checkOraScadenzaValid(astaDto.getDataScadenza(), astaDto.getOraScadenza());
        checkCategoriaValid(astaDto.getCategoriaShallow());

        log.debug("Integrità dei dati di asta verificata.");
    }

    private void checkCategoriaValid(CategoriaAstaShallowDto categoria) throws InvalidParameterException {

        log.trace("Controllo che 'categoria' sia valido...");

        if (categoria == null)
            throw new InvalidParameterException("La categoria non può essere null!");

        log.trace("'categoria' valido.");
    }

    private void checkNomeValid(String nome) throws InvalidParameterException {

        log.trace("Controllo che 'nome' sia valido...");

        if (nome == null)
            throw new InvalidParameterException("Il nome non può essere null!");
        else if (nome.isBlank())
            throw new InvalidParameterException("Il nome non può essere vuoto!");

        log.trace("'nome' valido.");
    }

    private void checkDescrizioneValid(String descrizione) throws InvalidParameterException {

        log.trace("Controllo che 'descrizione' sia valido...");

        if (descrizione == null)
            throw new InvalidParameterException("La descrizione non può essere null!");
        else if (descrizione.isBlank())
            throw new InvalidParameterException("La descrizione non può essere vuoto!");

        log.trace("'descrizione' valido.");
    }

    private void checkDataScadenzaValid(LocalDate dataScadenza) throws InvalidParameterException {

        log.trace("Controllo che 'dataScadenza' sia valido...");

        if (dataScadenza == null)
            throw new InvalidParameterException("La data di scadenza non può essere null!");
        else if (dataScadenza.isBefore(LocalDate.now()))
            throw new InvalidParameterException("La data di scadenza non può essere precedente alla data odierna!");

        log.trace("'dataScadenza' valido.");
    }

    private void checkOraScadenzaValid(LocalDate dataScadenza, LocalTime oraScadenza) throws InvalidParameterException {

        log.trace("Controllo che 'oraScadenza' sia valido...");

        if (oraScadenza == null)
            throw new InvalidParameterException("L'ora di scadenza non può essere null!");
        else if (dataScadenza.isEqual(LocalDate.now()) && oraScadenza.isBefore(LocalTime.now()))
            throw new InvalidParameterException("L'ora di scadenza non può essere precedente all'ora attuale!");

        log.trace("'oraScadenza' valido.");
    }

    @Override
    public void convertRelations(AstaDto astaDto, Asta asta) throws InvalidParameterException {

        log.debug("Recupero le associazioni di asta...");

        convertCategoriaAstaShallow(astaDto.getCategoriaShallow(), asta);
        convertNotificheAssociateShallow(astaDto.getNotificheAssociateShallow(), asta);

        log.debug("Associazioni di asta recuperate.");
    }

    private void convertNotificheAssociateShallow(Set<NotificaShallowDto> notificheAssociateShallow, Asta asta) throws IdNotFoundException {

        log.trace("Converto l'associazione 'notificheAssociate'...");

        asta.getNotificheAssociate().clear();

        if (notificheAssociateShallow != null) {
            for (NotificaShallowDto notificaShallowDto : notificheAssociateShallow) {

                Notifica convertedNotifica = relationsConverter.convertNotificaShallowRelation(notificaShallowDto);

                if (convertedNotifica != null) {
                    asta.addNotificaAssociata(convertedNotifica);
                    convertedNotifica.setAstaAssociata(asta);
                }
            }
        }

        log.trace("'notificheAssociate' convertita correttamente.");
    }

    private void convertCategoriaAstaShallow(CategoriaAstaShallowDto categoriaAstaShallow, Asta asta) throws IdNotFoundException {

        log.trace("Converto l'associazione 'categoriaAsta'...");

        CategoriaAsta convertedCategoriaAsta = relationsConverter.convertCategoriaAstaShallowRelation(categoriaAstaShallow);

        if (convertedCategoriaAsta != null) {
            asta.setCategoria(convertedCategoriaAsta);
            convertedCategoriaAsta.addAstaAssegnata(asta);
        }

        log.trace("'categoriaAsta' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(AstaDto updatedAstaDto, Asta existingAsta) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di asta richieste...");

        ifPresentUpdateNome(updatedAstaDto.getNome(), existingAsta);
        ifPresentUpdateDescrizione(updatedAstaDto.getDescrizione(), existingAsta);
        ifPresentUpdateDataScadenza(updatedAstaDto.getDataScadenza(), existingAsta);
        ifPresentUpdateOraScadenza(updatedAstaDto.getOraScadenza(), existingAsta);
        ifPresentUpdateImmagine(updatedAstaDto.getImmagine(), existingAsta);

        log.debug("Modifiche di asta effettuate correttamente.");

        // Non è possibile modificare l'associazione "categoria", "notificheAssociate" tramite la risorsa "aste"
    }

    private void ifPresentUpdateNome(String updatedNome, Asta existingAsta) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'nome'...");

        if (updatedNome != null) {
            checkNomeValid(updatedNome);
            existingAsta.setNome(updatedNome);
        }

        log.trace("'nome' modificato correttamente.");
    }

    private void ifPresentUpdateDescrizione(String updatedDescrizione, Asta existingAsta) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'descrizione'...");

        if (updatedDescrizione != null) {
            checkDescrizioneValid(updatedDescrizione);
            existingAsta.setDescrizione(updatedDescrizione);
        }

        log.trace("'descrizione' modificato correttamente.");
    }

    private void ifPresentUpdateDataScadenza(LocalDate updatedDataScadenza, Asta existingAsta) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'dataScadenza'...");

        if (updatedDataScadenza != null) {
            checkDataScadenzaValid(updatedDataScadenza);
            existingAsta.setDataScadenza(updatedDataScadenza);
        }

        log.trace("'dataScadenza' modificato correttamente.");
    }

    private void ifPresentUpdateOraScadenza(LocalTime updatedOraScadenza, Asta existingAsta) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'oraScadenza'...");

        if (updatedOraScadenza != null) {
            checkOraScadenzaValid(existingAsta.getDataScadenza(), updatedOraScadenza);
            existingAsta.setOraScadenza(updatedOraScadenza);
        }

        log.trace("'oraScadenza' modificato correttamente.");
    }

    private void ifPresentUpdateImmagine(byte[] updatedImmagine, Asta existingAsta) {

        log.trace("Effettuo la modifica di 'immagine'...");

        if (updatedImmagine != null) {
            existingAsta.setImmagine(updatedImmagine);
        }

        log.trace("'immagine' modificato correttamente.");
    }
}
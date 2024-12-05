package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.AstaDiCompratore;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.AstaDiCompratoreDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.services.AstaDiCompratoreService;
import com.iasdietideals24.backend.services.AstaService;
import com.iasdietideals24.backend.services.helper.RelationsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AstaDiCompratoreServiceImpl implements AstaDiCompratoreService {

    private final AstaService astaService;
    private final RelationsConverter relationsConverter;

    protected AstaDiCompratoreServiceImpl(AstaService astaService, RelationsConverter relationsConverter) {
        this.astaService = astaService;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public void checkFieldsValid(AstaDiCompratoreDto astaDiCompratoreDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di asta di compratore...");

        astaService.checkFieldsValid(astaDiCompratoreDto);
        checkProprietarioValid(astaDiCompratoreDto.getProprietarioShallow());

        log.debug("Integrità dei dati di asta di compratore verificata.");
    }

    private void checkProprietarioValid(AccountShallowDto proprietarioShallow) {

        log.trace("Controllo che 'proprietario' sia valido...");

        if (proprietarioShallow == null) {
            throw new UpdateRuntimeException("Il proprietario non può essere null!");
        }

        log.trace("'proprietario' valido.");
    }

    @Override
    public void convertRelations(AstaDiCompratoreDto astaDiCompratoreDto, AstaDiCompratore astaDiCompratore) throws InvalidParameterException {

        log.debug("Recupero le associazioni di asta di compratore...");

        astaService.convertRelations(astaDiCompratoreDto, astaDiCompratore);
        convertProprietarioShallow(astaDiCompratoreDto.getProprietarioShallow(), astaDiCompratore);

        log.debug("Associazioni di asta di compratore recuperate.");
    }

    private void convertProprietarioShallow(AccountShallowDto proprietarioShallow, AstaDiCompratore astaDiCompratore) throws IdNotFoundException, InvalidTypeException {

        log.trace("Converto l'associazione 'proprietario'...");

        Account convertedAccount = relationsConverter.convertAccountShallowRelation(proprietarioShallow);

        if (convertedAccount != null) {
            if (convertedAccount instanceof Compratore convertedCompratore) {
                astaDiCompratore.setProprietario(convertedCompratore);
                convertedCompratore.addAstaPosseduta(astaDiCompratore);
            } else {
                throw new InvalidTypeException("Un compratore può possedere solo aste di compratori!");
            }
        }

        log.trace("'proprietario' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(AstaDiCompratoreDto updatedAstaDiCompratoreDto, AstaDiCompratore existingAstaDiCompratore) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di asta di compratore richieste...");

        astaService.updatePresentFields(updatedAstaDiCompratoreDto, existingAstaDiCompratore);

        log.debug("Modifiche di asta di compratore effettuate correttamente.");

        // Non è possibile modificare l'associazione "proprietario" tramite la risorsa "aste/di-compratori"
    }
}
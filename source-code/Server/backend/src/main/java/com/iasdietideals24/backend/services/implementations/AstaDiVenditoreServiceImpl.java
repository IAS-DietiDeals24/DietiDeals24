package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.AstaDiVenditore;
import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.AstaDiVenditoreDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.services.AstaDiVenditoreService;
import com.iasdietideals24.backend.services.AstaService;
import com.iasdietideals24.backend.services.helper.RelationsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AstaDiVenditoreServiceImpl implements AstaDiVenditoreService {

    private final AstaService astaService;
    private final RelationsConverter relationsConverter;

    protected AstaDiVenditoreServiceImpl(AstaService astaService, RelationsConverter relationsConverter) {
        this.astaService = astaService;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public void checkFieldsValid(AstaDiVenditoreDto astaDiVenditoreDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di asta di venditore...");

        astaService.checkFieldsValid(astaDiVenditoreDto);
        checkProprietarioValid(astaDiVenditoreDto.getProprietarioShallow());

        log.debug("Integrità dei dati di asta di venditore verificata.");
    }

    private void checkProprietarioValid(AccountShallowDto proprietarioShallow) {

        log.trace("Controllo che 'proprietario' sia valido...");

        if (proprietarioShallow == null) {
            throw new UpdateRuntimeException("Il proprietario non può essere null!");
        }

        log.trace("'proprietario' valido.");
    }

    @Override
    public void convertRelations(AstaDiVenditoreDto astaDiVenditoreDto, AstaDiVenditore astaDiVenditore) throws InvalidParameterException {

        log.debug("Recupero le associazioni di asta di venditore...");

        astaService.convertRelations(astaDiVenditoreDto, astaDiVenditore);
        convertProprietarioShallow(astaDiVenditoreDto.getProprietarioShallow(), astaDiVenditore);

        log.debug("Associazioni di asta di venditore recuperate.");
    }

    private void convertProprietarioShallow(AccountShallowDto proprietarioShallow, AstaDiVenditore astaDiVenditore) throws IdNotFoundException, InvalidTypeException {

        log.trace("Converto l'associazione 'proprietario'...");

        Account convertedAccount = relationsConverter.convertAccountShallowRelation(proprietarioShallow);

        if (convertedAccount != null) {
            if (convertedAccount instanceof Venditore convertedVenditore) {
                astaDiVenditore.setProprietario(convertedVenditore);
                convertedVenditore.addAstaPosseduta(astaDiVenditore);
            } else {
                throw new InvalidTypeException("Un venditore può possedere solo aste di venditori!");
            }
        }

        log.trace("'proprietario' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(AstaDiVenditoreDto updatedAstaDiVenditoreDto, AstaDiVenditore existingAstaDiVenditore) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di asta di venditore richieste...");

        astaService.updatePresentFields(updatedAstaDiVenditoreDto, existingAstaDiVenditore);

        log.debug("Modifiche di asta di venditore effettuate correttamente.");

        // Non è possibile modificare l'associazione "proprietario" tramite la risorsa "aste/di-venditori"
    }
}
package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.exceptions.IllegalDeleteRequestException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.CompratoreDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.CompratoreMapper;
import com.iasdietideals24.backend.repositories.CompratoreRepository;
import com.iasdietideals24.backend.services.AccountService;
import com.iasdietideals24.backend.services.CompratoreService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class CompratoreServiceImpl implements CompratoreService {

    private final AccountService accountService;
    private final CompratoreMapper compratoreMapper;
    private final CompratoreRepository compratoreRepository;
    private final RelationsConverter relationsConverter;

    public CompratoreServiceImpl(AccountService accountService, CompratoreMapper compratoreMapper, CompratoreRepository compratoreRepository, RelationsConverter relationsConverter) {
        this.accountService = accountService;
        this.compratoreMapper = compratoreMapper;
        this.compratoreRepository = compratoreRepository;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public CompratoreDto create(CompratoreDto nuovoCompratoreDto) throws InvalidParameterException {

        // Verifichiamo l'integrità dei dati
        checkFieldsValid(nuovoCompratoreDto);

        // Convertiamo a entità
        Compratore nuovoCompratore = compratoreMapper.toEntity(nuovoCompratoreDto);

        // Recuperiamo le associazioni
        convertRelations(nuovoCompratoreDto, nuovoCompratore);

        // Registriamo l'entità
        Compratore savedCompratore = compratoreRepository.save(nuovoCompratore);

        return compratoreMapper.toDto(savedCompratore);
    }

    @Override
    public Page<CompratoreDto> findAll(Pageable pageable) {

        // Recuperiamo tutte le entità
        Page<Compratore> foundCompratori = compratoreRepository.findAll(pageable);

        return foundCompratori.map(compratoreMapper::toDto);
    }

    @Override
    public Optional<CompratoreDto> findOne(Long idAccount) {

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<Compratore> foundCompratore = compratoreRepository.findById(idAccount);

        return foundCompratore.map(compratoreMapper::toDto);
    }

    @Override
    public Page<CompratoreDto> findByTokensIdFacebook(String token, Pageable pageable) {

        // Recuperiamo l'entità con l'id passato per parametro
        Page<Compratore> foundCompratore = compratoreRepository.findByTokensIdFacebook(token, pageable);

        return foundCompratore.map(compratoreMapper::toDto);
    }

    @Override
    public Page<CompratoreDto> findByEmail(String email, Pageable pageable) {

        // Recuperiamo l'entità con l'id passato per parametro
        Page<Compratore> foundCompratore = compratoreRepository.findByEmail(email, pageable);

        return foundCompratore.map(compratoreMapper::toDto);
    }

    @Override
    public Page<CompratoreDto> findByEmailAndPassword(String email, String password, Pageable pageable) {

        // Recuperiamo l'entità con l'id e password passati per parametro
        Page<Compratore> foundCompratore = compratoreRepository.findByEmailAndPassword(email, password, pageable);

        return foundCompratore.map(compratoreMapper::toDto);
    }

    @Override
    public boolean isExists(Long idAccount) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return compratoreRepository.existsById(idAccount);
    }

    @Override
    public CompratoreDto fullUpdate(Long idAccount, CompratoreDto updatedCompratoreDto) throws InvalidParameterException {

        updatedCompratoreDto.setIdAccount(idAccount);

        if (!compratoreRepository.existsById(idAccount))
            throw new UpdateRuntimeException("L'id account '" + idAccount + "' non corrisponde a nessun account esistente!");
        else {
            // L'implementazione di fullUpdate e create sono identiche, dato che utilizziamo lo stesso metodo "save" della repository
            return this.create(updatedCompratoreDto);
        }
    }

    @Override
    public CompratoreDto partialUpdate(Long idAccount, CompratoreDto updatedCompratoreDto) throws InvalidParameterException {

        // Recuperiamo l'entità con l'id passato per parametro
        updatedCompratoreDto.setIdAccount(idAccount);

        Optional<Compratore> foundCompratore = compratoreRepository.findById(idAccount);
        if (foundCompratore.isEmpty())
            throw new UpdateRuntimeException("L'id account '" + idAccount + "' non corrisponde a nessun compratore esistente!");
        else {
            // Recuperiamo l'entità dal wrapping Optional
            Compratore existingCompratore = foundCompratore.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedCompratoreDto, existingCompratore);

            return compratoreMapper.toDto(compratoreRepository.save(existingCompratore));
        }
    }

    @Override
    public void delete(Long idAccount) throws InvalidParameterException {

        log.debug("SERVICE: Verifico che l'account non sia l'ultimo account del profilo al quale è associato...");

        Optional<Compratore> existingCompratore = compratoreRepository.findById(idAccount);
        if (existingCompratore.isPresent() && accountService.isLastAccountOfProfilo(existingCompratore.get())) {
            throw new IllegalDeleteRequestException("Non puoi eliminare l'unico account associato al profilo!");
        }

        log.debug("SERVICE: L'account non è l'ultimo account del profilo al quale è associato. Procedo con l'eliminazione...");
        
        // Eliminiamo l'entità con l'id passato per parametro
        compratoreRepository.deleteById(idAccount);

        log.debug("SERVICE: Account eliminato");
    }

    @Override
    public void checkFieldsValid(CompratoreDto compratoreDto) throws InvalidParameterException {
        accountService.checkFieldsValid(compratoreDto);
    }

    @Override
    public void convertRelations(CompratoreDto compratoreDto, Compratore compratore) throws InvalidParameterException {
        accountService.convertRelations(compratoreDto, compratore);
        convertAstePosseduteShallow(compratoreDto.getAstePosseduteShallow(), compratore);
        convertOfferteCollegateShallow(compratoreDto.getOfferteCollegateShallow(), compratore);
    }

    private void convertAstePosseduteShallow(Set<AstaShallowDto> astePosseduteShallowDto, Compratore compratore) throws InvalidParameterException {
        if (astePosseduteShallowDto != null) {
            for (AstaShallowDto astaShallowDto : astePosseduteShallowDto) {

                Asta convertedAsta = relationsConverter.convertAstaShallowRelation(astaShallowDto);

                if (convertedAsta != null) {
                    if (convertedAsta instanceof AstaDiCompratore convertedAstaDiCompratore) {
                        compratore.addAstaPosseduta(convertedAstaDiCompratore);
                        convertedAstaDiCompratore.setProprietario(compratore);
                    } else {
                        throw new InvalidTypeException("Un compratore può possedere solo aste di compratori!");
                    }
                }
            }
        }
    }

    private void convertOfferteCollegateShallow(Set<OffertaShallowDto> offerteCollegateShallowDto, Compratore compratore) throws InvalidParameterException {
        if (offerteCollegateShallowDto != null) {
            for (OffertaShallowDto offertaShallowDto : offerteCollegateShallowDto) {

                Offerta convertedOfferta = relationsConverter.convertOffertaShallowRelation(offertaShallowDto);

                if (convertedOfferta != null) {
                    if (convertedOfferta instanceof OffertaDiCompratore convertedOffertaDiCompratore) {
                        compratore.addOffertaCollegata(convertedOffertaDiCompratore);
                        convertedOffertaDiCompratore.setCompratoreCollegato(compratore);
                    } else {
                        throw new InvalidTypeException("Un compratore può essere collegato solo ad offerte di compratori!");
                    }
                }
            }
        }
    }

    @Override
    public void updatePresentFields(CompratoreDto updatedCompratoreDto, Compratore existingCompratore) throws InvalidParameterException {
        accountService.updatePresentFields(updatedCompratoreDto, existingCompratore);

        // Non è possibile modificare le associazioni "astePossedute", "offerteCollegate" tramite la risorsa "accounts/compratori"
    }
}

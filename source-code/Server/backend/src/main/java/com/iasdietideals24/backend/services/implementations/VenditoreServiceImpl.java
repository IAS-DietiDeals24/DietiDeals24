package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.exceptions.IllegalDeleteRequestException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.VenditoreDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.VenditoreMapper;
import com.iasdietideals24.backend.repositories.VenditoreRepository;
import com.iasdietideals24.backend.services.AccountService;
import com.iasdietideals24.backend.services.VenditoreService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class VenditoreServiceImpl implements VenditoreService {

    private final AccountService accountService;
    private final VenditoreMapper venditoreMapper;
    private final VenditoreRepository venditoreRepository;
    private final RelationsConverter relationsConverter;

    public VenditoreServiceImpl(AccountService accountService, VenditoreMapper venditoreMapper, VenditoreRepository venditoreRepository, RelationsConverter relationsConverter) {
        this.accountService = accountService;
        this.venditoreMapper = venditoreMapper;
        this.venditoreRepository = venditoreRepository;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public VenditoreDto create(VenditoreDto nuovoVenditoreDto) throws InvalidParameterException {

        // Verifichiamo l'integrità dei dati
        checkFieldsValid(nuovoVenditoreDto);

        // Convertiamo a entità
        Venditore nuovoVenditore = venditoreMapper.toEntity(nuovoVenditoreDto);

        // Recuperiamo le associazioni
        convertRelations(nuovoVenditoreDto, nuovoVenditore);

        // Registriamo l'entità
        Venditore savedVenditore = venditoreRepository.save(nuovoVenditore);

        return venditoreMapper.toDto(savedVenditore);
    }

    @Override
    public Page<VenditoreDto> findAll(Pageable pageable) {

        // Recuperiamo tutte le entità
        Page<Venditore> foundVenditori = venditoreRepository.findAll(pageable);

        return foundVenditori.map(venditoreMapper::toDto);
    }

    @Override
    public Optional<VenditoreDto> findOne(Long idAccount) {

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<Venditore> foundVenditore = venditoreRepository.findById(idAccount);

        return foundVenditore.map(venditoreMapper::toDto);
    }

    @Override
    public Page<VenditoreDto> findByTokensIdFacebook(String token, Pageable pageable) {

        // Recuperiamo l'entità con l'id passato per parametro
        Page<Venditore> foundVenditore = venditoreRepository.findByTokensIdFacebook(token, pageable);

        return foundVenditore.map(venditoreMapper::toDto);
    }

    @Override
    public Page<VenditoreDto> findByEmail(String email, Pageable pageable) {

        // Recuperiamo l'entità con l'id passato per parametro
        Page<Venditore> foundVenditore = venditoreRepository.findByEmail(email, pageable);

        return foundVenditore.map(venditoreMapper::toDto);
    }

    @Override
    public Page<VenditoreDto> findByEmailAndPassword(String email, String password, Pageable pageable) {

        // Recuperiamo l'entità con l'id e password passati per parametro
        Page<Venditore> foundVenditore = venditoreRepository.findByEmailAndPassword(email, password, pageable);

        return foundVenditore.map(venditoreMapper::toDto);
    }

    @Override
    public boolean isExists(Long idAccount) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return venditoreRepository.existsById(idAccount);
    }

    @Override
    public VenditoreDto fullUpdate(Long idAccount, VenditoreDto updatedVenditoreDto) throws InvalidParameterException {

        updatedVenditoreDto.setIdAccount(idAccount);

        if (!venditoreRepository.existsById(idAccount))
            throw new UpdateRuntimeException("L'id account '" + idAccount + "' non corrisponde a nessun account esistente!");
        else {
            // L'implementazione di fullUpdate e create sono identiche, dato che utilizziamo lo stesso metodo "save" della repository
            return this.create(updatedVenditoreDto);
        }
    }

    @Override
    public VenditoreDto partialUpdate(Long idAccount, VenditoreDto updatedVenditoreDto) throws InvalidParameterException {

        // Recuperiamo l'entità con l'id passato per parametro
        updatedVenditoreDto.setIdAccount(idAccount);

        Optional<Venditore> foundVenditore = venditoreRepository.findById(idAccount);
        if (foundVenditore.isEmpty())
            throw new UpdateRuntimeException("L'id account '" + idAccount + "' non corrisponde a nessun venditore esistente!");
        else {
            // Recuperiamo l'entità dal wrapping Optional
            Venditore existingVenditore = foundVenditore.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedVenditoreDto, existingVenditore);

            return venditoreMapper.toDto(venditoreRepository.save(existingVenditore));
        }
    }

    @Override
    public void delete(Long idAccount) throws InvalidParameterException {

        Optional<Venditore> existingVenditore = venditoreRepository.findById(idAccount);
        if (existingVenditore.isPresent() && accountService.isLastAccountOfProfilo(existingVenditore.get())) {
            throw new IllegalDeleteRequestException("Non puoi eliminare l'unico account associato al profilo!");
        }

        // Eliminiamo l'entità con l'id passato per parametro
        venditoreRepository.deleteById(idAccount);
    }

    @Override
    public void checkFieldsValid(VenditoreDto venditoreDto) throws InvalidParameterException {
        accountService.checkFieldsValid(venditoreDto);
    }

    @Override
    public void convertRelations(VenditoreDto venditoreDto, Venditore venditore) throws InvalidParameterException {
        accountService.convertRelations(venditoreDto, venditore);
        convertAstePosseduteShallow(venditoreDto.getAstePosseduteShallow(), venditore);
        convertOfferteCollegateShallow(venditoreDto.getOfferteCollegateShallow(), venditore);
    }

    private void convertAstePosseduteShallow(Set<AstaShallowDto> astePosseduteShallowDto, Venditore venditore) throws InvalidParameterException {
        if (astePosseduteShallowDto != null) {
            for (AstaShallowDto astaShallowDto : astePosseduteShallowDto) {

                Asta convertedAsta = relationsConverter.convertAstaShallowRelation(astaShallowDto);

                if (convertedAsta != null) {
                    if (convertedAsta instanceof AstaDiVenditore convertedAstaDiVenditore) {
                        venditore.addAstaPosseduta(convertedAstaDiVenditore);
                        convertedAstaDiVenditore.setProprietario(venditore);
                    } else {
                        throw new InvalidTypeException("Un venditore può possedere solo aste di venditori!");
                    }
                }
            }
        }
    }

    private void convertOfferteCollegateShallow(Set<OffertaShallowDto> offerteCollegateShallowDto, Venditore venditore) throws InvalidParameterException {
        if (offerteCollegateShallowDto != null) {
            for (OffertaShallowDto offertaShallowDto : offerteCollegateShallowDto) {

                Offerta convertedOfferta = relationsConverter.convertOffertaShallowRelation(offertaShallowDto);

                if (convertedOfferta != null) {
                    if (convertedOfferta instanceof OffertaDiVenditore convertedOffertaDiVenditore) {
                        venditore.addOffertaCollegata(convertedOffertaDiVenditore);
                        convertedOffertaDiVenditore.setVenditoreCollegato(venditore);
                    } else {
                        throw new InvalidTypeException("Un venditore può essere collegato solo ad offerte di venditori!");
                    }
                }
            }
        }
    }

    @Override
    public void updatePresentFields(VenditoreDto updatedVenditoreDto, Venditore existingVenditore) throws InvalidParameterException {
        accountService.updatePresentFields(updatedVenditoreDto, existingVenditore);

        // Non è possibile modificare le associazioni "astePossedute", "offerteCollegate" tramite la risorsa "accounts/venditori"
    }
}
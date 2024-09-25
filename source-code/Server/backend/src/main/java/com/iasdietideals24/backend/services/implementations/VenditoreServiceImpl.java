package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.IllegalDeleteRequestException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.VenditoreDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.AstaMapper;
import com.iasdietideals24.backend.mapstruct.mappers.VenditoreMapper;
import com.iasdietideals24.backend.mapstruct.mappers.OffertaMapper;
import com.iasdietideals24.backend.repositories.AstaDiVenditoreRepository;
import com.iasdietideals24.backend.repositories.VenditoreRepository;
import com.iasdietideals24.backend.repositories.OffertaDiVenditoreRepository;
import com.iasdietideals24.backend.services.AccountService;
import com.iasdietideals24.backend.services.VenditoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class VenditoreServiceImpl implements VenditoreService {

    private final AccountService accountService;

    private final VenditoreMapper venditoreMapper;
    private final AstaMapper astaMapper;
    private final OffertaMapper offertaMapper;

    private final VenditoreRepository venditoreRepository;
    private final AstaDiVenditoreRepository astaDiVenditoreRepository;
    private final OffertaDiVenditoreRepository offertaDiVenditoreRepository;

    public VenditoreServiceImpl(AccountService accountService,
                                VenditoreMapper venditoreMapper,
                                AstaMapper astaMapper,
                                OffertaMapper offertaMapper,
                                VenditoreRepository venditoreRepository,
                                AstaDiVenditoreRepository astaDiVenditoreRepository,
                                OffertaDiVenditoreRepository offertaDiVenditoreRepository) {

        this.accountService = accountService;

        this.venditoreMapper = venditoreMapper;
        this.astaMapper = astaMapper;
        this.offertaMapper = offertaMapper;
        this.venditoreRepository = venditoreRepository;

        this.astaDiVenditoreRepository = astaDiVenditoreRepository;
        this.offertaDiVenditoreRepository = offertaDiVenditoreRepository;
    }

    @Override
    public VenditoreDto create(String email, VenditoreDto nuovoVenditoreDto) throws InvalidParameterException {

        //Verifichiamo l'integrità dei dati
        nuovoVenditoreDto.setEmail(email);
        checkFieldsValid(nuovoVenditoreDto);

        // Convertiamo a entità
        Venditore nuovoVenditore = venditoreMapper.toEntity(nuovoVenditoreDto);

        // Recuperiamo le associazioni
        convertRelations(nuovoVenditoreDto, nuovoVenditore);

        //Registriamo l'entità
        Venditore savedVenditore = venditoreRepository.save(nuovoVenditore);

        return venditoreMapper.toDto(savedVenditore);
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

    void convertAstePosseduteShallow(Set<AstaShallowDto> astePosseduteShallowDto, Venditore nuovoVenditore) throws InvalidParameterException {
        if (astePosseduteShallowDto != null) {
            for (Asta asta : astaMapper.toEntity(astePosseduteShallowDto)) {
                Optional<AstaDiVenditore> foundAsta = astaDiVenditoreRepository.findById(asta.getIdAsta());
                if (foundAsta.isEmpty())
                    throw new IdNotFoundException("L'id asta \"" + asta.getIdAsta() + "\" non corrisponde a nessuna asta esistente!");
                else {
                    nuovoVenditore.addAstaPosseduta(foundAsta.get());
                    foundAsta.get().setProprietario(nuovoVenditore);
                }
            }
        }
    }

    void convertOfferteCollegateShallow(Set<OffertaShallowDto> offerteCollegateShallowDto, Venditore nuovoVenditore) throws InvalidParameterException {
        if (offerteCollegateShallowDto != null) {
            for (Offerta offerta : offertaMapper.toEntity(offerteCollegateShallowDto)) {
                Optional<OffertaDiVenditore> foundOfferta = offertaDiVenditoreRepository.findById(offerta.getIdOfferta());
                if (foundOfferta.isEmpty())
                    throw new IdNotFoundException("L'id offerta \"" + offerta.getIdOfferta() + "\" non corrisponde a nessuna offerta esistente!");
                else {
                    nuovoVenditore.addOffertaCollegata(foundOfferta.get());
                    foundOfferta.get().setVenditoreCollegato(nuovoVenditore);
                }
            }
        }
    }

    @Override
    public Page<VenditoreDto> findAll(Pageable pageable) {

        // Recuperiamo tutte le entità
        Page<Venditore> foundVenditori = venditoreRepository.findAll(pageable);

        return foundVenditori.map(venditoreMapper::toDto);
    }

    @Override
    public Optional<VenditoreDto> findOne(String email) {

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<Venditore> foundVenditore = venditoreRepository.findById(email);

        return foundVenditore.map(venditoreMapper::toDto);
    }

    @Override
    public boolean isExists(String email) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return venditoreRepository.existsById(email);
    }

    @Override
    public VenditoreDto fullUpdate(String email, VenditoreDto updatedVenditoreDto) throws InvalidParameterException {

        // L'implementazione di fullUpdate e create sono identiche, dato che utilizziamo lo stesso metodo "save" della repository
        return this.create(email, updatedVenditoreDto);
    }

    @Override
    public VenditoreDto partialUpdate(String email, VenditoreDto updatedVenditoreDto) throws InvalidParameterException {

        // Recuperiamo l'entità con l'id passato per parametro
        updatedVenditoreDto.setEmail(email);
        Optional<Venditore> foundVenditore = venditoreRepository.findById(email);

        if (foundVenditore.isEmpty())
            throw new UpdateRuntimeException("L'email non corrisponde a nessun venditore esistente!");
        else {

            // Recuperiamo l'entità Profilo dal wrapping Optional
            Venditore existingVenditore = foundVenditore.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedVenditoreDto, existingVenditore);

            // Non è possibile modificare le associazioni "profilo", "notificheInviate", "notificheRicevute", "astePossedute", "offerteCollegate"

            return venditoreMapper.toDto(venditoreRepository.save(existingVenditore));
        }
    }

    @Override
    public void updatePresentFields(VenditoreDto updatedVenditoreDto, Venditore existingVenditore) throws InvalidParameterException {
        accountService.updatePresentFields(updatedVenditoreDto, existingVenditore);
    }


    @Override
    public void delete(String email) throws InvalidParameterException {

        Optional<Venditore> existingVenditore = venditoreRepository.findById(email);
        if (existingVenditore.isPresent() && accountService.isLastProfiloAccount(existingVenditore.get())) {
            throw new IllegalDeleteRequestException("Non puoi eliminare l'unico account associato al profilo!");
        }

        // Eliminiamo l'entità con l'id passato per parametro
        venditoreRepository.deleteById(email);
    }
}

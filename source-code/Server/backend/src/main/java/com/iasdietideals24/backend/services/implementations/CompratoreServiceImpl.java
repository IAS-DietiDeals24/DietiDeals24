package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.IllegalDeleteRequestException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.CompratoreDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.*;
import com.iasdietideals24.backend.repositories.*;
import com.iasdietideals24.backend.services.AccountService;
import com.iasdietideals24.backend.services.CompratoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CompratoreServiceImpl implements CompratoreService {

    private final AccountService accountService;

    private final CompratoreMapper compratoreMapper;
    private final AstaMapper astaMapper;
    private final OffertaMapper offertaMapper;

    private final CompratoreRepository compratoreRepository;
    private final AstaDiCompratoreRepository astaDiCompratoreRepository;
    private final OffertaDiCompratoreRepository offertaDiCompratoreRepository;

    public CompratoreServiceImpl(AccountService accountService,
                                 CompratoreMapper compratoreMapper,
                                 AstaMapper astaMapper,
                                 OffertaMapper offertaMapper,
                                 CompratoreRepository compratoreRepository,
                                 AstaDiCompratoreRepository astaDiCompratoreRepository,
                                 OffertaDiCompratoreRepository offertaDiCompratoreRepository) {

        this.accountService = accountService;

        this.compratoreMapper = compratoreMapper;
        this.astaMapper = astaMapper;
        this.offertaMapper = offertaMapper;
        this.compratoreRepository = compratoreRepository;

        this.astaDiCompratoreRepository = astaDiCompratoreRepository;
        this.offertaDiCompratoreRepository = offertaDiCompratoreRepository;
    }

    @Override
    public CompratoreDto create(String email, CompratoreDto nuovoCompratoreDto) throws InvalidParameterException {

        //Verifichiamo l'integrità dei dati
        nuovoCompratoreDto.setEmail(email);
        checkFieldsValid(nuovoCompratoreDto);

        // Convertiamo a entità
        Compratore nuovoCompratore = compratoreMapper.toEntity(nuovoCompratoreDto);

        // Recuperiamo le associazioni
        convertRelations(nuovoCompratoreDto, nuovoCompratore);

        //Registriamo l'entità
        Compratore savedCompratore = compratoreRepository.save(nuovoCompratore);

        return compratoreMapper.toDto(savedCompratore);
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

    void convertAstePosseduteShallow(Set<AstaShallowDto> astePosseduteShallowDto, Compratore nuovoCompratore) throws InvalidParameterException {
        if (astePosseduteShallowDto != null) {
            for (Asta asta : astaMapper.toEntity(astePosseduteShallowDto)) {
                Optional<AstaDiCompratore> foundAsta = astaDiCompratoreRepository.findById(asta.getIdAsta());
                if (foundAsta.isEmpty())
                    throw new IdNotFoundException("L'id asta \"" + asta.getIdAsta() + "\" non corrisponde a nessuna asta esistente!");
                else {
                    nuovoCompratore.addAstaPosseduta(foundAsta.get());
                    foundAsta.get().setProprietario(nuovoCompratore);
                }
            }
        }
    }

    void convertOfferteCollegateShallow(Set<OffertaShallowDto> offerteCollegateShallowDto, Compratore nuovoCompratore) throws InvalidParameterException {
        if (offerteCollegateShallowDto != null) {
            for (Offerta offerta : offertaMapper.toEntity(offerteCollegateShallowDto)) {
                Optional<OffertaDiCompratore> foundOfferta = offertaDiCompratoreRepository.findById(offerta.getIdOfferta());
                if (foundOfferta.isEmpty())
                    throw new IdNotFoundException("L'id offerta \"" + offerta.getIdOfferta() + "\" non corrisponde a nessuna offerta esistente!");
                else {
                    nuovoCompratore.addOffertaCollegata(foundOfferta.get());
                    foundOfferta.get().setCompratoreCollegato(nuovoCompratore);
                }
            }
        }
    }

    @Override
    public Page<CompratoreDto> findAll(Pageable pageable) {

        // Recuperiamo tutte le entità
        Page<Compratore> foundCompratori = compratoreRepository.findAll(pageable);

        return foundCompratori.map(compratoreMapper::toDto);
    }

    @Override
    public Optional<CompratoreDto> findOne(String email) {

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<Compratore> foundCompratore = compratoreRepository.findById(email);

        return foundCompratore.map(compratoreMapper::toDto);
    }

    @Override
    public boolean isExists(String email) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return compratoreRepository.existsById(email);
    }

    @Override
    public CompratoreDto fullUpdate(String email, CompratoreDto updatedCompratoreDto) throws InvalidParameterException {

        // L'implementazione di fullUpdate e create sono identiche, dato che utilizziamo lo stesso metodo "save" della repository
        return this.create(email, updatedCompratoreDto);
    }

    @Override
    public CompratoreDto partialUpdate(String email, CompratoreDto updatedCompratoreDto) throws InvalidParameterException {

        // Recuperiamo l'entità con l'id passato per parametro
        updatedCompratoreDto.setEmail(email);
        Optional<Compratore> foundCompratore = compratoreRepository.findById(email);

        if (foundCompratore.isEmpty())
            throw new UpdateRuntimeException("L'email non corrisponde a nessun compratore esistente!");
        else {

            // Recuperiamo l'entità Profilo dal wrapping Optional
            Compratore existingCompratore = foundCompratore.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedCompratoreDto, existingCompratore);

            // Non è possibile modificare le associazioni "profilo", "notificheInviate", "notificheRicevute", "astePossedute", "offerteCollegate"

            return compratoreMapper.toDto(compratoreRepository.save(existingCompratore));
        }
    }

    @Override
    public void updatePresentFields(CompratoreDto updatedCompratoreDto, Compratore existingCompratore) throws InvalidParameterException {
        accountService.updatePresentFields(updatedCompratoreDto, existingCompratore);
    }


    @Override
    public void delete(String email) throws InvalidParameterException {

        Optional<Compratore> existingCompratore = compratoreRepository.findById(email);
        if (existingCompratore.isPresent() && accountService.isLastProfiloAccount(existingCompratore.get())) {
            throw new IllegalDeleteRequestException("Non puoi eliminare l'unico account associato al profilo!");
        }

        // Eliminiamo l'entità con l'id passato per parametro
        compratoreRepository.deleteById(email);
    }
}

package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.entities.utilities.TokensAccount;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.AccountDto;
import com.iasdietideals24.backend.mapstruct.dto.VenditoreDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.ProfiloShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.utilities.TokensAccountDto;
import com.iasdietideals24.backend.mapstruct.mappers.*;
import com.iasdietideals24.backend.repositories.*;
import com.iasdietideals24.backend.services.VenditoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public class VenditoreServiceImpl extends AccountServiceImpl implements VenditoreService {

    private final VenditoreMapper venditoreMapper;
    private final TokensAccountMapper tokensAccountMapper;
    private final ProfiloMapper profiloMapper;
    private final AstaMapper astaMapper;
    private final OffertaMapper offertaMapper;
    private final NotificaMapper notificaMapper;

    private final VenditoreRepository venditoreRepository;
    private final ProfiloRepository profiloRepository;
    private final NotificaRepository notificaRepository;
    private final AstaDiVenditoreRepository astaDiVenditoreRepository;
    private final OffertaDiVenditoreRepository offertaDiVenditoreRepository;

    public VenditoreServiceImpl(VenditoreMapper venditoreMapper,
                                TokensAccountMapper tokensAccountMapper,
                                ProfiloMapper profiloMapper,
                                NotificaMapper notificaMapper,
                                AstaMapper astaMapper,
                                OffertaMapper offertaMapper,
                                VenditoreRepository venditoreRepository,
                                ProfiloRepository profiloRepository,
                                NotificaRepository notificaRepository,
                                AstaDiVenditoreRepository astaDiVenditoreRepository,
                                OffertaDiVenditoreRepository offertaDiVenditoreRepository) {

        this.venditoreMapper = venditoreMapper;
        this.tokensAccountMapper = tokensAccountMapper;
        this.profiloMapper = profiloMapper;
        this.notificaMapper = notificaMapper;
        this.astaMapper = astaMapper;
        this.offertaMapper = offertaMapper;
        this.venditoreRepository = venditoreRepository;
        this.profiloRepository = profiloRepository;
        this.notificaRepository = notificaRepository;
        this.astaDiVenditoreRepository = astaDiVenditoreRepository;
        this.offertaDiVenditoreRepository = offertaDiVenditoreRepository;
    }

    @Override
    public VenditoreDto create(String email, VenditoreDto nuovoVenditoreDto) throws InvalidParameterException {

        //Verifichiamo l'integrità dei dati
        nuovoVenditoreDto.setEmail(email);
        this.validateData(nuovoVenditoreDto);

        // Convertiamo a entità
        Venditore nuovoVenditore = venditoreMapper.toEntity(nuovoVenditoreDto);

        // Recuperiamo le associazioni
        convertProfiloShallow(nuovoVenditoreDto.getProfiloShallow(), nuovoVenditore);
        convertNotificheInviateShallow(nuovoVenditoreDto.getNotificheInviateShallow(), nuovoVenditore);
        convertNotificheRicevuteShallow(nuovoVenditoreDto.getNotificheRicevuteShallow(), nuovoVenditore);
        convertAstePosseduteShallow(nuovoVenditoreDto.getAstePosseduteShallow(), nuovoVenditore);
        convertOfferteCollegateShallow(nuovoVenditoreDto.getOfferteCollegateShallow(), nuovoVenditore);

        //Registriamo l'entità
        Venditore savedVenditore = venditoreRepository.save(nuovoVenditore);

        return venditoreMapper.toDto(savedVenditore);
    }

    private void convertProfiloShallow(ProfiloShallowDto profiloShallowDto, Venditore nuovoVenditore) throws InvalidParameterException {
        if (profiloShallowDto != null) {
            Profilo profilo = profiloMapper.toEntity(profiloShallowDto);
            Optional<Profilo> foundProfilo = profiloRepository.findById(profiloShallowDto.getNomeUtente());
            if (foundProfilo.isEmpty())
                throw new IdNotFoundException("Il nome utente \"" + profilo.getNomeUtente() + "\" non corrisponde a nessun profilo esistente!");
            else {
                if (foundProfilo.get().getVenditore() != null)
                    throw new InvalidParameterException("Il profilo \"" + "\" è già associato a un account venditore! Eliminare prima quello precedente");
                else {
                    nuovoVenditore.setProfilo(foundProfilo.get());
                    foundProfilo.get().addAccount(nuovoVenditore);
                }
            }
        }
    }

    private void convertNotificheInviateShallow(Set<NotificaShallowDto> notificheInviateShallowDto, Venditore nuovoVenditore) throws InvalidParameterException {
        if (notificheInviateShallowDto != null) {
            for (Notifica notifica : notificaMapper.toEntity(notificheInviateShallowDto)) {
                Optional<Notifica> foundNotifica = notificaRepository.findById(notifica.getIdNotifica());
                if (foundNotifica.isEmpty())
                    throw new IdNotFoundException("L'id notifica inviata \"" + notifica.getIdNotifica() + "\" non corrisponde a nessuna notifica esistente!");
                else {
                    nuovoVenditore.addNotificaInviata(foundNotifica.get());
                    foundNotifica.get().setMittente(nuovoVenditore);
                }
            }
        }
    }

    private void convertNotificheRicevuteShallow(Set<NotificaShallowDto> notificheRicevuteShallowDto, Venditore nuovoVenditore) throws InvalidParameterException {
        if (notificheRicevuteShallowDto != null) {
            for (Notifica notifica : notificaMapper.toEntity(notificheRicevuteShallowDto)) {
                Optional<Notifica> foundNotifica = notificaRepository.findById(notifica.getIdNotifica());
                if (foundNotifica.isEmpty())
                    throw new IdNotFoundException("L'id notifica ricevuta \"" + notifica.getIdNotifica() + "\" non corrisponde a nessuna notifica esistente!");
                else {
                    nuovoVenditore.addNotificaRicevuta(foundNotifica.get());
                    foundNotifica.get().addDestinatario(nuovoVenditore);
                }
            }
        }
    }

    private void convertAstePosseduteShallow(Set<AstaShallowDto> astePosseduteShallowDto, Venditore nuovoVenditore) throws InvalidParameterException {
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

    private void convertOfferteCollegateShallow(Set<OffertaShallowDto> offerteCollegateShallowDto, Venditore nuovoVenditore) throws InvalidParameterException {
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
        Page<Venditore> foundCompratori = venditoreRepository.findAll(pageable);

        return foundCompratori.map(venditoreMapper::toDto);
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
            ifPresentUpdateEmail(updatedVenditoreDto.getEmail(), existingVenditore);
            ifPresentUpdatePassword(updatedVenditoreDto.getPassword(), existingVenditore);
            ifPresentUpdateTokens(updatedVenditoreDto.getTokens(), existingVenditore);

            // Non è possibile modificare le associazioni "profilo", "notificheInviate", "notificheRicevute", "astePossedute", "offerteCollegate"

            return venditoreMapper.toDto(venditoreRepository.save(existingVenditore));
        }
    }

    private void ifPresentUpdateEmail(String updatedEmail, Venditore existingVenditore) throws InvalidParameterException {
        if (updatedEmail != null) {
            super.isEmailValid(updatedEmail);
            existingVenditore.setEmail(updatedEmail);
        }
    }

    private void ifPresentUpdatePassword(String updatedPassword, Venditore existingVenditore) throws InvalidParameterException {
        if (updatedPassword != null) {
            super.isPasswordValid(updatedPassword);
            existingVenditore.setPassword(updatedPassword);
        }
    }

    private void ifPresentUpdateTokens(TokensAccountDto updatedTokensDto, Venditore existingVenditore) {
        TokensAccount existingTokens = existingVenditore.getTokens();

        if (updatedTokensDto != null) {
            if (existingTokens == null) {
                existingVenditore.setTokens(tokensAccountMapper.toEntity(updatedTokensDto));
            } else {
                ifPresentUpdateIdFacebook(updatedTokensDto.getIdFacebook(), existingTokens);
                ifPresentUpdateIdGoogle(updatedTokensDto.getIdGoogle(), existingTokens);
                ifPresentUpdateIdX(updatedTokensDto.getIdX(), existingTokens);
                ifPresentUpdateIdGitHub(updatedTokensDto.getIdGitHub(), existingTokens);
            }
        }
    }

    private void ifPresentUpdateIdFacebook(String updatedIdFacebook, TokensAccount existingTokens) {
        if (updatedIdFacebook != null) {
            existingTokens.setIdFacebook(updatedIdFacebook);
        }
    }

    private void ifPresentUpdateIdGoogle(String updatedIdGoogle, TokensAccount existingTokens) {
        if (updatedIdGoogle != null) {
            existingTokens.setIdGoogle(updatedIdGoogle);
        }
    }

    private void ifPresentUpdateIdX(String updatedIdX, TokensAccount existingTokens) {
        if (updatedIdX != null) {
            existingTokens.setIdX(updatedIdX);
        }
    }

    private void ifPresentUpdateIdGitHub(String updatedIdGitHub, TokensAccount existingTokens) {
        if (updatedIdGitHub != null) {
            existingTokens.setIdGitHub(updatedIdGitHub);
        }
    }

    @Override
    public void delete(String email) {

        // Eliminiamo l'entità con l'id passato per parametro
        venditoreRepository.deleteById(email);
    }

    @Override
    public void validateData(AccountDto nuovoAccountDto) throws InvalidParameterException {
        super.validateData(nuovoAccountDto);
    }
}

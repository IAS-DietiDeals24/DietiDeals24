package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.entities.utilities.TokensAccount;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.AccountDto;
import com.iasdietideals24.backend.mapstruct.dto.CompratoreDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.ProfiloShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.utilities.TokensAccountDto;
import com.iasdietideals24.backend.mapstruct.mappers.*;
import com.iasdietideals24.backend.repositories.*;
import com.iasdietideals24.backend.services.CompratoreService;
import org.hibernate.sql.Update;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public class CompratoreServiceImpl extends AccountServiceImpl implements CompratoreService {

    private final CompratoreMapper compratoreMapper;
    private final TokensAccountMapper tokensAccountMapper;
    private final ProfiloMapper profiloMapper;
    private final AstaMapper astaMapper;
    private final OffertaMapper offertaMapper;
    private final NotificaMapper notificaMapper;

    private final CompratoreRepository compratoreRepository;
    private final ProfiloRepository profiloRepository;
    private final NotificaRepository notificaRepository;
    private final AstaDiCompratoreRepository astaDiCompratoreRepository;
    private final OffertaDiCompratoreRepository offertaDiCompratoreRepository;

    public CompratoreServiceImpl(CompratoreMapper compratoreMapper,
                                 TokensAccountMapper tokensAccountMapper,
                                 ProfiloMapper profiloMapper,
                                 NotificaMapper notificaMapper,
                                 AstaMapper astaMapper,
                                 OffertaMapper offertaMapper,
                                 CompratoreRepository compratoreRepository,
                                 ProfiloRepository profiloRepository,
                                 NotificaRepository notificaRepository,
                                 AstaDiCompratoreRepository astaDiCompratoreRepository,
                                 OffertaDiCompratoreRepository offertaDiCompratoreRepository) {

        this.compratoreMapper = compratoreMapper;
        this.tokensAccountMapper = tokensAccountMapper;
        this.profiloMapper = profiloMapper;
        this.notificaMapper = notificaMapper;
        this.astaMapper = astaMapper;
        this.offertaMapper = offertaMapper;
        this.compratoreRepository = compratoreRepository;
        this.profiloRepository = profiloRepository;
        this.notificaRepository = notificaRepository;
        this.astaDiCompratoreRepository = astaDiCompratoreRepository;
        this.offertaDiCompratoreRepository = offertaDiCompratoreRepository;
    }

    @Override
    public CompratoreDto create(String email, CompratoreDto nuovoCompratoreDto) throws InvalidParameterException {

        //Verifichiamo l'integrità dei dati
        nuovoCompratoreDto.setEmail(email);
        this.validateData(nuovoCompratoreDto);

        // Convertiamo a entità
        Compratore nuovoCompratore = compratoreMapper.toEntity(nuovoCompratoreDto);

        // Recuperiamo le associazioni
        convertProfiloShallow(nuovoCompratoreDto.getProfiloShallow(), nuovoCompratore);
        convertNotificheInviateShallow(nuovoCompratoreDto.getNotificheInviateShallow(), nuovoCompratore);
        convertNotificheRicevuteShallow(nuovoCompratoreDto.getNotificheRicevuteShallow(), nuovoCompratore);
        convertAstePosseduteShallow(nuovoCompratoreDto.getAstePosseduteShallow(), nuovoCompratore);
        convertOfferteCollegateShallow(nuovoCompratoreDto.getOfferteCollegateShallow(), nuovoCompratore);

        //Registriamo l'entità
        Compratore savedCompratore = compratoreRepository.save(nuovoCompratore);

        return compratoreMapper.toDto(savedCompratore);
    }

    private void convertProfiloShallow(ProfiloShallowDto profiloShallowDto, Compratore nuovoCompratore) throws InvalidParameterException {
        if (profiloShallowDto != null) {
            Profilo profilo = profiloMapper.toEntity(profiloShallowDto);
            Optional<Profilo> foundProfilo = profiloRepository.findById(profiloShallowDto.getNomeUtente());
            if (foundProfilo.isEmpty())
                throw new IdNotFoundException("Il nome utente \"" + profilo.getNomeUtente() + "\" non corrisponde a nessun profilo esistente!");
            else {
                if (foundProfilo.get().getCompratore() != null)
                    throw new InvalidParameterException("Il profilo \"" + "\" è già associato a un account compratore! Eliminare prima quello precedente");
                else {
                    nuovoCompratore.setProfilo(foundProfilo.get());
                    foundProfilo.get().addAccount(nuovoCompratore);
                }
            }
        }
    }

    private void convertNotificheInviateShallow(Set<NotificaShallowDto> notificheInviateShallowDto, Compratore nuovoCompratore) throws InvalidParameterException {
        if (notificheInviateShallowDto != null) {
            for (Notifica notifica : notificaMapper.toEntity(notificheInviateShallowDto)) {
                Optional<Notifica> foundNotifica = notificaRepository.findById(notifica.getIdNotifica());
                if (foundNotifica.isEmpty())
                    throw new IdNotFoundException("L'id notifica inviata \"" + notifica.getIdNotifica() + "\" non corrisponde a nessuna notifica esistente!");
                else {
                    nuovoCompratore.addNotificaInviata(foundNotifica.get());
                    foundNotifica.get().setMittente(nuovoCompratore);
                }
            }
        }
    }

    private void convertNotificheRicevuteShallow(Set<NotificaShallowDto> notificheRicevuteShallowDto, Compratore nuovoCompratore) throws InvalidParameterException {
        if (notificheRicevuteShallowDto != null) {
            for (Notifica notifica : notificaMapper.toEntity(notificheRicevuteShallowDto)) {
                Optional<Notifica> foundNotifica = notificaRepository.findById(notifica.getIdNotifica());
                if (foundNotifica.isEmpty())
                    throw new IdNotFoundException("L'id notifica ricevuta \"" + notifica.getIdNotifica() + "\" non corrisponde a nessuna notifica esistente!");
                else {
                    nuovoCompratore.addNotificaRicevuta(foundNotifica.get());
                    foundNotifica.get().addDestinatario(nuovoCompratore);
                }
            }
        }
    }

    private void convertAstePosseduteShallow(Set<AstaShallowDto> astePosseduteShallowDto, Compratore nuovoCompratore) throws InvalidParameterException {
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

    private void convertOfferteCollegateShallow(Set<OffertaShallowDto> offerteCollegateShallowDto, Compratore nuovoCompratore) throws InvalidParameterException {
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
            ifPresentUpdateEmail(updatedCompratoreDto.getEmail(), existingCompratore);
            ifPresentUpdatePassword(updatedCompratoreDto.getPassword(), existingCompratore);
            ifPresentUpdateTokens(updatedCompratoreDto.getTokens(), existingCompratore);

            // Non è possibile modificare le associazioni "profilo", "notificheInviate", "notificheRicevute", "astePossedute", "offerteCollegate"

            return compratoreMapper.toDto(compratoreRepository.save(existingCompratore));
        }
    }

    private void ifPresentUpdateEmail(String updatedEmail, Compratore existingCompratore) throws InvalidParameterException {
        if (updatedEmail != null) {
            super.isEmailValid(updatedEmail);
            existingCompratore.setEmail(updatedEmail);
        }
    }

    private void ifPresentUpdatePassword(String updatedPassword, Compratore existingCompratore) throws InvalidParameterException {
        if (updatedPassword != null) {
            super.isPasswordValid(updatedPassword);
            existingCompratore.setPassword(updatedPassword);
        }
    }

    private void ifPresentUpdateTokens(TokensAccountDto updatedTokensDto, Compratore existingCompratore) {
        TokensAccount existingTokens = existingCompratore.getTokens();

        if (updatedTokensDto != null) {
            if (existingTokens == null) {
                existingCompratore.setTokens(tokensAccountMapper.toEntity(updatedTokensDto));
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
        compratoreRepository.deleteById(email);
    }
}

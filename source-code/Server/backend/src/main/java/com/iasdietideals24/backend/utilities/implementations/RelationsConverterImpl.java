package com.iasdietideals24.backend.utilities.implementations;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.Asta;
import com.iasdietideals24.backend.entities.AstaDiCompratore;
import com.iasdietideals24.backend.entities.AstaDiVenditore;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.Notifica;
import com.iasdietideals24.backend.entities.Offerta;
import com.iasdietideals24.backend.entities.OffertaDiCompratore;
import com.iasdietideals24.backend.entities.OffertaDiVenditore;
import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.entities.Venditore;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.ProfiloShallowDto;
import com.iasdietideals24.backend.repositories.AstaDiCompratoreRepository;
import com.iasdietideals24.backend.repositories.AstaDiVenditoreRepository;
import com.iasdietideals24.backend.repositories.CompratoreRepository;
import com.iasdietideals24.backend.repositories.NotificaRepository;
import com.iasdietideals24.backend.repositories.OffertaDiCompratoreRepository;
import com.iasdietideals24.backend.repositories.OffertaDiVenditoreRepository;
import com.iasdietideals24.backend.repositories.ProfiloRepository;
import com.iasdietideals24.backend.repositories.VenditoreRepository;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RelationsConverterImpl implements RelationsConverter {

    private static final String INIZIO_MESSAGGIO_ERRORE_TIPO = "Il tipo \"";
    private static final String FINE_MESSAGGIO_ERRORE_TIPO = "\" non esiste";
    
    private final ProfiloRepository profiloRepository;
    private final NotificaRepository notificaRepository;
    private final AstaDiCompratoreRepository astaDiCompratoreRepository;
    private final AstaDiVenditoreRepository astaDiVenditoreRepository;
    private final OffertaDiCompratoreRepository offertaDiCompratoreRepository;
    private final OffertaDiVenditoreRepository offertaDiVenditoreRepository;
    private final CompratoreRepository compratoreRepository;
    private final VenditoreRepository venditoreRepository;

    public RelationsConverterImpl(ProfiloRepository profiloRepository,
                                  NotificaRepository notificaRepository,
                                  AstaDiCompratoreRepository astaDiCompratoreRepository,
                                  AstaDiVenditoreRepository astaDiVenditoreRepository,
                                  OffertaDiCompratoreRepository offertaDiCompratoreRepository,
                                  OffertaDiVenditoreRepository offertaDiVenditoreRepository,
                                  CompratoreRepository compratoreRepository,
                                  VenditoreRepository venditoreRepository) {
        this.profiloRepository = profiloRepository;
        this.notificaRepository = notificaRepository;
        this.astaDiCompratoreRepository = astaDiCompratoreRepository;
        this.astaDiVenditoreRepository = astaDiVenditoreRepository;
        this.offertaDiCompratoreRepository = offertaDiCompratoreRepository;
        this.offertaDiVenditoreRepository = offertaDiVenditoreRepository;
        this.compratoreRepository = compratoreRepository;
        this.venditoreRepository = venditoreRepository;
    }

    @Override
    public Profilo convertProfiloShallowRelation(ProfiloShallowDto profiloShallowDto) throws IdNotFoundException {
        Profilo profilo = null;

        if (profiloShallowDto != null) {
            Optional<Profilo> foundProfilo = profiloRepository.findById(profiloShallowDto.getNomeUtente());
            if (foundProfilo.isEmpty())
                throw new IdNotFoundException("Il nome utente \"" + profiloShallowDto.getNomeUtente() + "\" non corrisponde a nessun profilo esistente!");
            else
                profilo = foundProfilo.get();
        }

        return profilo;
    }

    @Override
    public Notifica convertNotificaShallowRelation(NotificaShallowDto notificaShallowDto) throws IdNotFoundException {
        Notifica notifica = null;

        if (notificaShallowDto != null) {
            Optional<Notifica> foundNotifica = notificaRepository.findById(notificaShallowDto.getIdNotifica());
            if (foundNotifica.isEmpty())
                throw new IdNotFoundException("L'id notifica \"" + notificaShallowDto.getIdNotifica() + "\" non corrisponde a nessuna notifica esistente!");
            else
                notifica = foundNotifica.get();
        }

        return notifica;
    }

    @Override
    public Asta convertAstaShallowRelation(AstaShallowDto astaShallowDto) throws IdNotFoundException, InvalidTypeException {
        Asta asta = null;

        if (astaShallowDto != null) {
            if (astaShallowDto.getTipoAstaPerAccount().equals(AstaDiCompratore.class.getSimpleName())) {
                Optional<AstaDiCompratore> foundAstaDiCompratore = astaDiCompratoreRepository.findById(astaShallowDto.getIdAsta());
                if (foundAstaDiCompratore.isEmpty())
                    throw new IdNotFoundException("L'id asta \"" + astaShallowDto.getIdAsta() + "\" non corrisponde a nessuna asta di compratore esistente!");

                asta = foundAstaDiCompratore.get();
            } else if (astaShallowDto.getTipoAstaPerAccount().equals(AstaDiVenditore.class.getSimpleName())) {
                Optional<AstaDiVenditore> foundAstaDiVenditore = astaDiVenditoreRepository.findById(astaShallowDto.getIdAsta());
                if (foundAstaDiVenditore.isEmpty())
                    throw new IdNotFoundException("L'id asta \"" + astaShallowDto.getIdAsta() + "\" non corrisponde a nessuna asta di venditore esistente!");

                asta = foundAstaDiVenditore.get();
            } else {
                throw new InvalidTypeException(INIZIO_MESSAGGIO_ERRORE_TIPO + astaShallowDto.getTipoAstaPerAccount() + FINE_MESSAGGIO_ERRORE_TIPO);
            }
        }

        return asta;
    }

    @Override
    public Offerta convertOffertaShallowRelation(OffertaShallowDto offertaShallowDto) throws IdNotFoundException, InvalidTypeException {
        Offerta offerta = null;

        if (offertaShallowDto != null) {
            if (offertaShallowDto.getTipoOffertaPerAccount().equals(OffertaDiCompratore.class.getSimpleName())) {
                Optional<OffertaDiCompratore> foundOffertaDiCompratore = offertaDiCompratoreRepository.findById(offertaShallowDto.getIdOfferta());
                if (foundOffertaDiCompratore.isEmpty())
                    throw new IdNotFoundException("L'id offerta \"" + offertaShallowDto.getIdOfferta() + "\" non corrisponde a nessuna offerta di compratore esistente!");

                offerta = foundOffertaDiCompratore.get();
            } else if (offertaShallowDto.getTipoOffertaPerAccount().equals(OffertaDiVenditore.class.getSimpleName())) {
                Optional<OffertaDiVenditore> foundOffertaDiVenditore = offertaDiVenditoreRepository.findById(offertaShallowDto.getIdOfferta());
                if (foundOffertaDiVenditore.isEmpty())
                    throw new IdNotFoundException("L'id offerta \"" + offertaShallowDto.getIdOfferta() + "\" non corrisponde a nessuna offerta di venditore esistente!");

                offerta = foundOffertaDiVenditore.get();
            } else {
                throw new InvalidTypeException(INIZIO_MESSAGGIO_ERRORE_TIPO + offertaShallowDto.getTipoOffertaPerAccount() + FINE_MESSAGGIO_ERRORE_TIPO);
            }
        }

        return offerta;
    }

    @Override
    public Account convertAccountShallowRelation(AccountShallowDto accountShallowDto) throws IdNotFoundException, InvalidTypeException {
        Account account = null;

        if (accountShallowDto != null) {
            if (accountShallowDto.getTipoAccount().equals(Compratore.class.getSimpleName())) {
                Optional<Compratore> foundCompratore = compratoreRepository.findById(accountShallowDto.getEmail());
                if (foundCompratore.isEmpty())
                    throw new IdNotFoundException("L'email \"" + accountShallowDto.getEmail() + "\" non corrisponde a nessun compratore esistente!");

                account = foundCompratore.get();
            } else if (accountShallowDto.getTipoAccount().equals(Venditore.class.getSimpleName())) {
                Optional<Venditore> foundVenditore = venditoreRepository.findById(accountShallowDto.getEmail());
                if (foundVenditore.isEmpty())
                    throw new IdNotFoundException("L'email \"" + accountShallowDto.getEmail() + "\" non corrisponde a nessun venditore esistente!");

                account = foundVenditore.get();
            } else {
                throw new InvalidTypeException(INIZIO_MESSAGGIO_ERRORE_TIPO + accountShallowDto.getTipoAccount() + FINE_MESSAGGIO_ERRORE_TIPO);
            }
        }

        return account;
    }
}

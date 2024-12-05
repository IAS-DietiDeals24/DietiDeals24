package com.iasdietideals24.backend.services.helper.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.shallows.*;
import com.iasdietideals24.backend.repositories.*;
import com.iasdietideals24.backend.services.helper.RelationsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class RelationsConverterImpl implements RelationsConverter {

    private final ProfiloRepository profiloRepository;
    private final NotificaRepository notificaRepository;
    private final AstaRepository astaRepository;
    private final OffertaRepository offertaRepository;
    private final AccountRepository accountRepository;
    private final CategoriaAstaRepository categoriaAstaRepository;

    public RelationsConverterImpl(ProfiloRepository profiloRepository,
                                  NotificaRepository notificaRepository,
                                  AstaRepository astaRepository,
                                  OffertaRepository offertaRepository,
                                  AccountRepository accountRepository,
                                  CategoriaAstaRepository categoriaAstaRepository) {
        this.profiloRepository = profiloRepository;
        this.notificaRepository = notificaRepository;
        this.astaRepository = astaRepository;
        this.offertaRepository = offertaRepository;
        this.accountRepository = accountRepository;
        this.categoriaAstaRepository = categoriaAstaRepository;
    }

    @Override
    public Profilo convertProfiloShallowRelation(ProfiloShallowDto profiloShallowDto) throws IdNotFoundException {
        Profilo profilo = null;

        if (profiloShallowDto != null) {
            Optional<Profilo> foundProfilo = profiloRepository.findById(profiloShallowDto.getNomeUtente());
            if (foundProfilo.isEmpty()) {
                log.warn("Il nome utente '{}' non corrisponde a nessun profilo esistente!", profiloShallowDto.getNomeUtente());
                throw new IdNotFoundException("Il nome utente '" + profiloShallowDto.getNomeUtente() + "' non corrisponde a nessun profilo esistente!");
            } else
                profilo = foundProfilo.get();
        }

        return profilo;
    }

    @Override
    public Notifica convertNotificaShallowRelation(NotificaShallowDto notificaShallowDto) throws IdNotFoundException {
        Notifica notifica = null;

        if (notificaShallowDto != null) {
            Optional<Notifica> foundNotifica = notificaRepository.findById(notificaShallowDto.getIdNotifica());
            if (foundNotifica.isEmpty()) {
                log.warn("L'id notifica '{}' non corrisponde a nessuna notifica esistente!", notificaShallowDto.getIdNotifica());
                throw new IdNotFoundException("L'id notifica '" + notificaShallowDto.getIdNotifica() + "' non corrisponde a nessuna notifica esistente!");
            } else
                notifica = foundNotifica.get();
        }

        return notifica;
    }

    @Override
    public Asta convertAstaShallowRelation(AstaShallowDto astaShallowDto) throws IdNotFoundException, InvalidTypeException {
        Asta asta = null;

        if (astaShallowDto != null) {
            Optional<Asta> foundAsta = astaRepository.findById(astaShallowDto.getIdAsta());
            if (foundAsta.isEmpty()) {
                log.warn("L'id asta '{}' non corrisponde a nessuna asta esistente!", astaShallowDto.getIdAsta());
                throw new IdNotFoundException("L'id asta '" + astaShallowDto.getIdAsta() + "' non corrisponde a nessuna asta esistente!");
            } else {
                asta = foundAsta.get();
                if (!astaShallowDto.getTipoAstaSpecifica().equals(asta.getClass().getSimpleName())) {
                    log.warn("L'id asta '{}' non corrisponde a nessuna asta di tipo '{}'!", astaShallowDto.getIdAsta(), astaShallowDto.getTipoAstaSpecifica());
                    throw new InvalidTypeException("L'id asta '" + astaShallowDto.getIdAsta() + "' non corrisponde a nessuna asta di tipo '" + astaShallowDto.getTipoAstaSpecifica() + "'!");
                }
            }
        }

        return asta;
    }

    @Override
    public Offerta convertOffertaShallowRelation(OffertaShallowDto offertaShallowDto) throws IdNotFoundException, InvalidTypeException {
        Offerta offerta = null;

        if (offertaShallowDto != null) {
            Optional<Offerta> foundOfferta = offertaRepository.findById(offertaShallowDto.getIdOfferta());
            if (foundOfferta.isEmpty()) {
                log.warn("L'id offerta '{}' non corrisponde a nessuna offerta esistente!", offertaShallowDto.getIdOfferta());
                throw new IdNotFoundException("L'id offerta '" + offertaShallowDto.getIdOfferta() + "' non corrisponde a nessuna offerta esistente!");
            } else {
                offerta = foundOfferta.get();
                if (!offertaShallowDto.getTipoOffertaSpecifica().equals(offerta.getClass().getSimpleName())) {
                    log.warn("L'id offerta '{}' non corrisponde a nessuna offerta di tipo '{}'!", offertaShallowDto.getIdOfferta(), offertaShallowDto.getTipoOffertaSpecifica());
                    throw new InvalidTypeException("L'id offerta '" + offertaShallowDto.getIdOfferta() + "' non corrisponde a nessuna offerta di tipo '" + offertaShallowDto.getTipoOffertaSpecifica() + "'!");
                }
            }
        }

        return offerta;
    }

    @Override
    public Account convertAccountShallowRelation(AccountShallowDto accountShallowDto) throws IdNotFoundException, InvalidTypeException {
        Account account = null;

        if (accountShallowDto != null) {
            Optional<Account> foundAccount = accountRepository.findById(accountShallowDto.getIdAccount());
            if (foundAccount.isEmpty()) {
                log.warn("L'id account '{}' non corrisponde a nessun account esistente!", accountShallowDto.getIdAccount());
                throw new IdNotFoundException("L'id account '" + accountShallowDto.getIdAccount() + "' non corrisponde a nessun account esistente!");
            } else {
                account = foundAccount.get();
                if (!accountShallowDto.getTipoAccount().equals(account.getClass().getSimpleName())) {
                    log.warn("L'id account '{}' non corrisponde a nessun account di tipo '{}'!", accountShallowDto.getIdAccount(), accountShallowDto.getTipoAccount());
                    throw new InvalidTypeException("L'id account '" + accountShallowDto.getIdAccount() + "' non corrisponde a nessun account di tipo '" + accountShallowDto.getTipoAccount() + "'!");
                }
            }
        }

        return account;
    }

    @Override
    public CategoriaAsta convertCategoriaAstaShallowRelation(CategoriaAstaShallowDto categoriaAstaShallowDto) throws IdNotFoundException {
        CategoriaAsta categoriaAsta = null;

        if (categoriaAstaShallowDto != null) {
            Optional<CategoriaAsta> foundCategoriaAsta = categoriaAstaRepository.findById(categoriaAstaShallowDto.getNome());
            if (foundCategoriaAsta.isEmpty()) {
                log.warn("Il nome '{}' non corrisponde a nessuna categoria asta esistente!", categoriaAstaShallowDto.getNome());
                throw new IdNotFoundException("Il nome '" + categoriaAstaShallowDto.getNome() + "' non corrisponde a nessuna categoria asta esistente!");
            } else {
                categoriaAsta = foundCategoriaAsta.get();
            }
        }

        return categoriaAsta;
    }
}
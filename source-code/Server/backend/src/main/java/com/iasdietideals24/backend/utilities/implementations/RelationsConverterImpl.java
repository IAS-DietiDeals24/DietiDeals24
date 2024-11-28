package com.iasdietideals24.backend.utilities.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.shallows.*;
import com.iasdietideals24.backend.repositories.*;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RelationsConverterImpl implements RelationsConverter {

    private final ProfiloRepository profiloRepository;
    private final NotificaRepository notificaRepository;
    private final AstaRepository astaRepository;
    private final OffertaRepository offertaRepository;
    private final AccountRepository accountRepository;

    public RelationsConverterImpl(ProfiloRepository profiloRepository,
                                  NotificaRepository notificaRepository,
                                  AstaRepository astaRepository,
                                  OffertaRepository offertaRepository,
                                  AccountRepository accountRepository) {
        this.profiloRepository = profiloRepository;
        this.notificaRepository = notificaRepository;
        this.astaRepository = astaRepository;
        this.offertaRepository = offertaRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Profilo convertProfiloShallowRelation(ProfiloShallowDto profiloShallowDto) throws IdNotFoundException {
        Profilo profilo = null;

        if (profiloShallowDto != null) {
            Optional<Profilo> foundProfilo = profiloRepository.findById(profiloShallowDto.getNomeUtente());
            if (foundProfilo.isEmpty())
                throw new IdNotFoundException("Il nome utente '" + profiloShallowDto.getNomeUtente() + "' non corrisponde a nessun profilo esistente!");
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
                throw new IdNotFoundException("L'id notifica '" + notificaShallowDto.getIdNotifica() + "' non corrisponde a nessuna notifica esistente!");
            else
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
                throw new IdNotFoundException("L'id asta '" + astaShallowDto.getIdAsta() + "' non corrisponde a nessuna asta esistente!");
            } else {
                asta = foundAsta.get();
                if (!astaShallowDto.getTipoAstaSpecifica().equals(asta.getClass().getSimpleName()))
                    throw new InvalidTypeException("L'id asta '" + astaShallowDto.getIdAsta() + "' non corrisponde a nessuna asta di tipo '" + astaShallowDto.getTipoAstaSpecifica() + "'!");
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
                throw new IdNotFoundException("L'id offerta '" + offertaShallowDto.getIdOfferta() + "' non corrisponde a nessuna offerta esistente!");
            } else {
                offerta = foundOfferta.get();
                if (!offertaShallowDto.getTipoOffertaSpecifica().equals(offerta.getClass().getSimpleName()))
                    throw new InvalidTypeException("L'id offerta '" + offertaShallowDto.getIdOfferta() + "' non corrisponde a nessuna offerta di tipo '" + offertaShallowDto.getTipoOffertaSpecifica() + "'!");
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
                throw new IdNotFoundException("L'id account '" + accountShallowDto.getIdAccount() + "' non corrisponde a nessun account esistente!");
            } else {
                account = foundAccount.get();
                if (!accountShallowDto.getTipoAccount().equals(account.getClass().getSimpleName()))
                    throw new InvalidTypeException("L'id account '" + accountShallowDto.getIdAccount() + "' non corrisponde a nessun account di tipo '" + accountShallowDto.getTipoAccount() + "'!");
            }
        }

        return account;
    }
}

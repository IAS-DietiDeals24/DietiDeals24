package com.iasdietideals24.backend.utilities.implementations;

import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.exceptions.IdNotFoundException;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AccountShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.NotificaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.OffertaShallowDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.ProfiloShallowDto;
import com.iasdietideals24.backend.repositories.*;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RelationsConverterImpl implements RelationsConverter {

    private static final String INIZIO_MESSAGGIO_ERRORE_TIPO = "Il tipo \"";
    private static final String FINE_MESSAGGIO_ERRORE_TIPO = "\" non esiste";

    private static final String INIZIO_MESSAGGIO_ERRORE_IDASTA = "L'id asta \"";
    private static final String FINE_MESSAGGIO_ERRORE_IDASTA = "\" non corrisponde a nessuna asta di tipo ";

    private static final String INIZIO_MESSAGGIO_ERRORE_IDOFFERTA = "L'id offerta \"";
    private static final String FINE_MESSAGGIO_ERRORE_IDOFFERTA = "\" non corrisponde a nessuna offerta di tipo ";

    private static final String INIZIO_MESSAGGIO_ERRORE_EMAIL = "L'email \"";
    private static final String FINE_MESSAGGIO_ERRORE_EMAIL = "\" non corrisponde a nessun account di tipo ";
    
    private final ProfiloRepository profiloRepository;
    private final NotificaRepository notificaRepository;
    private final AstaInversaRepository astaInversaRepository;
    private final AstaTempoFissoRepository astaTempoFissoRepository;
    private final AstaSilenziosaRepository astaSilenziosaRepository;
    private final OffertaInversaRepository offertaInversaRepository;
    private final OffertaTempoFissoRepository offertaTempoFissoRepository;
    private final OffertaSilenziosaRepository offertaSilenziosaRepository;
    private final CompratoreRepository compratoreRepository;
    private final VenditoreRepository venditoreRepository;

    public RelationsConverterImpl(ProfiloRepository profiloRepository,
                                  NotificaRepository notificaRepository,
                                  AstaInversaRepository astaInversaRepository,
                                  AstaTempoFissoRepository astaTempoFissoRepository,
                                  AstaSilenziosaRepository astaSilenziosaRepository,
                                  OffertaInversaRepository offertaInversaRepository,
                                  OffertaTempoFissoRepository offertaTempoFissoRepository,
                                  OffertaSilenziosaRepository offertaSilenziosaRepository,
                                  CompratoreRepository compratoreRepository,
                                  VenditoreRepository venditoreRepository) {
        this.profiloRepository = profiloRepository;
        this.notificaRepository = notificaRepository;
        this.astaInversaRepository = astaInversaRepository;
        this.astaTempoFissoRepository = astaTempoFissoRepository;
        this.astaSilenziosaRepository = astaSilenziosaRepository;
        this.offertaInversaRepository = offertaInversaRepository;
        this.offertaTempoFissoRepository = offertaTempoFissoRepository;
        this.offertaSilenziosaRepository = offertaSilenziosaRepository;
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
                asta = convertAstaDiCompratoreShallowRelation(astaShallowDto);

            } else if (astaShallowDto.getTipoAstaPerAccount().equals(AstaDiVenditore.class.getSimpleName())) {
                asta = convertAstaDiVenditoreShallowRelation(astaShallowDto);

            } else {
                throw new InvalidTypeException(INIZIO_MESSAGGIO_ERRORE_TIPO + astaShallowDto.getTipoAstaPerAccount() + FINE_MESSAGGIO_ERRORE_TIPO);
            }
        }

        return asta;
    }

    private Asta convertAstaDiCompratoreShallowRelation(AstaShallowDto astaShallowDto) throws IdNotFoundException, InvalidTypeException {
        Asta asta = null;

        if (!astaShallowDto.getTipoAstaPerAccount().equals(AstaDiCompratore.class.getSimpleName()))
            throw new InvalidTypeException("L'asta non è di tipo asta di compratore!");

        if (astaShallowDto.getTipoAstaSpecifica().equals(AstaSilenziosa.class.getSimpleName())) {
            Optional<AstaSilenziosa> foundAstaSilenziosa = astaSilenziosaRepository.findById(astaShallowDto.getIdAsta());
            if (foundAstaSilenziosa.isEmpty())
                throw new IdNotFoundException(INIZIO_MESSAGGIO_ERRORE_IDASTA + astaShallowDto.getIdAsta() + FINE_MESSAGGIO_ERRORE_IDASTA + "silenziosa!");
            asta = foundAstaSilenziosa.get();

        } else if (astaShallowDto.getTipoAstaSpecifica().equals(AstaTempoFisso.class.getSimpleName())) {
            Optional<AstaTempoFisso> foundAstaTempoFisso = astaTempoFissoRepository.findById(astaShallowDto.getIdAsta());
            if (foundAstaTempoFisso.isEmpty())
                throw new IdNotFoundException(INIZIO_MESSAGGIO_ERRORE_IDASTA + astaShallowDto.getIdAsta() + FINE_MESSAGGIO_ERRORE_IDASTA + "tempo fisso!");
            asta = foundAstaTempoFisso.get();

        } else {
            throw new InvalidTypeException(INIZIO_MESSAGGIO_ERRORE_TIPO + astaShallowDto.getTipoAstaSpecifica() + FINE_MESSAGGIO_ERRORE_TIPO);
        }

        return asta;
    }

    private Asta convertAstaDiVenditoreShallowRelation(AstaShallowDto astaShallowDto) throws IdNotFoundException, InvalidTypeException {
        Asta asta = null;

        if (!astaShallowDto.getTipoAstaPerAccount().equals(AstaDiVenditore.class.getSimpleName()))
            throw new InvalidTypeException("L'asta non è di tipo asta di venditore!");

        if (astaShallowDto.getTipoAstaSpecifica().equals(AstaInversa.class.getSimpleName())) {
            Optional<AstaInversa> foundAstaInversa = astaInversaRepository.findById(astaShallowDto.getIdAsta());
            if (foundAstaInversa.isEmpty())
                throw new IdNotFoundException(INIZIO_MESSAGGIO_ERRORE_IDASTA + astaShallowDto.getIdAsta() + FINE_MESSAGGIO_ERRORE_IDASTA + "inversa!");
            asta = foundAstaInversa.get();

        } else {
            throw new InvalidTypeException(INIZIO_MESSAGGIO_ERRORE_TIPO + astaShallowDto.getTipoAstaSpecifica() + FINE_MESSAGGIO_ERRORE_TIPO);
        }

        return asta;
    }

    @Override
    public Offerta convertOffertaShallowRelation(OffertaShallowDto offertaShallowDto) throws IdNotFoundException, InvalidTypeException {
        Offerta offerta = null;

        if (offertaShallowDto != null) {
            if (offertaShallowDto.getTipoOffertaPerAccount().equals(OffertaDiCompratore.class.getSimpleName())) {
                offerta = convertOffertaDiCompratoreShallowRelation(offertaShallowDto);

            } else if (offertaShallowDto.getTipoOffertaPerAccount().equals(OffertaDiVenditore.class.getSimpleName())) {
                offerta = convertOffertaDiVenditoreShallowRelation(offertaShallowDto);

            } else {
                throw new InvalidTypeException(INIZIO_MESSAGGIO_ERRORE_TIPO + offertaShallowDto.getTipoOffertaPerAccount() + FINE_MESSAGGIO_ERRORE_TIPO);
            }
        }

        return offerta;
    }

    private Offerta convertOffertaDiCompratoreShallowRelation(OffertaShallowDto offertaShallowDto) throws IdNotFoundException, InvalidTypeException {
        Offerta offerta = null;

        if (!offertaShallowDto.getTipoOffertaPerAccount().equals(OffertaDiCompratore.class.getSimpleName()))
            throw new InvalidTypeException("L'offerta non è di tipo offerta di compratore!");

        if (offertaShallowDto.getTipoOffertaSpecifica().equals(OffertaSilenziosa.class.getSimpleName())) {
            Optional<OffertaSilenziosa> foundOffertaSilenziosa = offertaSilenziosaRepository.findById(offertaShallowDto.getIdOfferta());
            if (foundOffertaSilenziosa.isEmpty())
                throw new IdNotFoundException(INIZIO_MESSAGGIO_ERRORE_IDOFFERTA + offertaShallowDto.getIdOfferta() + FINE_MESSAGGIO_ERRORE_IDOFFERTA + "silenziosa!");
            offerta = foundOffertaSilenziosa.get();

        } else if (offertaShallowDto.getTipoOffertaSpecifica().equals(OffertaTempoFisso.class.getSimpleName())) {
            Optional<OffertaTempoFisso> foundOffertaTempoFisso = offertaTempoFissoRepository.findById(offertaShallowDto.getIdOfferta());
            if (foundOffertaTempoFisso.isEmpty())
                throw new IdNotFoundException(INIZIO_MESSAGGIO_ERRORE_IDOFFERTA + offertaShallowDto.getIdOfferta() + FINE_MESSAGGIO_ERRORE_IDOFFERTA + "tempo fisso!");
            offerta = foundOffertaTempoFisso.get();

        } else {
            throw new InvalidTypeException(INIZIO_MESSAGGIO_ERRORE_TIPO + offertaShallowDto.getTipoOffertaSpecifica() + FINE_MESSAGGIO_ERRORE_TIPO);
        }

        return offerta;
    }

    private Offerta convertOffertaDiVenditoreShallowRelation(OffertaShallowDto offertaShallowDto) throws IdNotFoundException, InvalidTypeException {
        Offerta offerta = null;

        if (!offertaShallowDto.getTipoOffertaPerAccount().equals(OffertaDiVenditore.class.getSimpleName()))
            throw new InvalidTypeException("L'offerta non è di tipo offerta di venditore!");

        if (offertaShallowDto.getTipoOffertaSpecifica().equals(OffertaInversa.class.getSimpleName())) {
            Optional<OffertaInversa> foundOffertaInversa = offertaInversaRepository.findById(offertaShallowDto.getIdOfferta());
            if (foundOffertaInversa.isEmpty())
                throw new IdNotFoundException(INIZIO_MESSAGGIO_ERRORE_IDOFFERTA + offertaShallowDto.getIdOfferta() + FINE_MESSAGGIO_ERRORE_IDOFFERTA + "inversa!");
            offerta = foundOffertaInversa.get();

        } else {
            throw new InvalidTypeException(INIZIO_MESSAGGIO_ERRORE_TIPO + offertaShallowDto.getTipoOffertaSpecifica() + FINE_MESSAGGIO_ERRORE_TIPO);
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
                    throw new IdNotFoundException(INIZIO_MESSAGGIO_ERRORE_EMAIL + accountShallowDto.getEmail() + FINE_MESSAGGIO_ERRORE_EMAIL + "compratore!");
                account = foundCompratore.get();

            } else if (accountShallowDto.getTipoAccount().equals(Venditore.class.getSimpleName())) {
                Optional<Venditore> foundVenditore = venditoreRepository.findById(accountShallowDto.getEmail());
                if (foundVenditore.isEmpty())
                    throw new IdNotFoundException(INIZIO_MESSAGGIO_ERRORE_EMAIL + accountShallowDto.getEmail() + FINE_MESSAGGIO_ERRORE_EMAIL + "venditore!");
                account = foundVenditore.get();

            } else {
                throw new InvalidTypeException(INIZIO_MESSAGGIO_ERRORE_TIPO + accountShallowDto.getTipoAccount() + FINE_MESSAGGIO_ERRORE_TIPO);
            }
        }

        return account;
    }
}

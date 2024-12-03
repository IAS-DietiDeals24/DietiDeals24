package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Asta;
import com.iasdietideals24.backend.entities.CategoriaAsta;
import com.iasdietideals24.backend.exceptions.IllegalDeleteRequestException;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.CategoriaAstaDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.CategoriaAstaMapper;
import com.iasdietideals24.backend.repositories.CategoriaAstaRepository;
import com.iasdietideals24.backend.services.CategoriaAstaService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class CategoriaAstaServiceImpl implements CategoriaAstaService {

    public static final String LOG_RECUPERO_CATEGORIA_ASTA = "Recupero la categoria asta dal database...";
    public static final String LOG_FOUND_CATEGORIA_ASTA = "foundCategoriaAsta: {}";
    public static final String LOG_CATEGORIA_ASTA_RECUPERATA = "Categoria asta recuperata dal database.";

    private final CategoriaAstaMapper categoriaAstaMapper;
    private final CategoriaAstaRepository categoriaAstaRepository;
    private final RelationsConverter relationsConverter;

    public CategoriaAstaServiceImpl(CategoriaAstaMapper categoriaAstaMapper,
                                    CategoriaAstaRepository categoriaAstaRepository,
                                    RelationsConverter relationsConverter) {
        this.categoriaAstaMapper = categoriaAstaMapper;
        this.categoriaAstaRepository = categoriaAstaRepository;
        this.relationsConverter = relationsConverter;
    }

    @Override
    public CategoriaAstaDto create(String nome, CategoriaAstaDto nuovoCategoriaAstaDto) throws InvalidParameterException {

        log.trace("Nome categoria asta da creare: {}", nome);

        // Verifichiamo l'integrità dei dati
        nuovoCategoriaAstaDto.setNome(nome);
        checkFieldsValid(nuovoCategoriaAstaDto);

        log.debug("Converto il DTO in entità...");

        // Convertiamo a entità
        CategoriaAsta nuovaCategoriaAsta = categoriaAstaMapper.toEntity(nuovoCategoriaAstaDto);

        log.debug("DTO convertito correttamente.");
        log.trace("nuovaCategoriaAsta: {}", nuovaCategoriaAsta);

        // Recuperiamo le associazioni
        convertRelations(nuovoCategoriaAstaDto, nuovaCategoriaAsta);

        log.trace("nuovaCategoriaAsta: {}", nuovaCategoriaAsta);

        log.debug("Salvo la categoria asta nel database...");

        // Registriamo l'entità
        CategoriaAsta savedCategoriaAsta = categoriaAstaRepository.save(nuovaCategoriaAsta);

        log.trace("savedCategoriaAsta: {}", savedCategoriaAsta);
        log.debug("Categoria asta salvata correttamente nel database con nome {}...", savedCategoriaAsta.getNome());

        return categoriaAstaMapper.toDto(savedCategoriaAsta);
    }

    @Override
    public Page<CategoriaAstaDto> findAll(Pageable pageable) {

        log.debug("Recupero le categorie asta dal database...");

        // Recuperiamo tutte le entità
        Page<CategoriaAsta> foundCategorieAsta = categoriaAstaRepository.findAll(pageable);

        log.trace("foundCategorieAsta: {}", foundCategorieAsta);
        log.debug("Categorie asta recuperate dal database.");

        return foundCategorieAsta.map(categoriaAstaMapper::toDto);
    }

    @Override
    public Optional<CategoriaAstaDto> findOne(String nome) {

        log.trace("Nome categoria asta da recuperare: {}", nome);
        log.debug(LOG_RECUPERO_CATEGORIA_ASTA);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<CategoriaAsta> foundCategoriaAsta = categoriaAstaRepository.findById(nome);

        log.trace(LOG_FOUND_CATEGORIA_ASTA, foundCategoriaAsta);
        log.debug(LOG_CATEGORIA_ASTA_RECUPERATA);

        return foundCategoriaAsta.map(categoriaAstaMapper::toDto);
    }

    @Override
    public boolean isExists(String nome) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return categoriaAstaRepository.existsById(nome);
    }

    @Override
    public CategoriaAstaDto fullUpdate(String nome, CategoriaAstaDto updatedCategoriaAstaDto) throws InvalidParameterException {

        log.trace("Nome categoria asta da sostituire: {}", nome);

        // L'implementazione di fullUpdate e create sono identiche, dato che utilizziamo lo stesso metodo "save" della repository
        return this.create(nome, updatedCategoriaAstaDto);
    }

    @Override
    public CategoriaAstaDto partialUpdate(String nome, CategoriaAstaDto updatedCategoriaAstaDto) throws InvalidParameterException {

        log.trace("Nome categoria asta da aggiornare: {}", nome);

        updatedCategoriaAstaDto.setNome(nome);

        log.debug(LOG_RECUPERO_CATEGORIA_ASTA);

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<CategoriaAsta> foundCategoriaAsta = categoriaAstaRepository.findById(nome);

        log.trace(LOG_FOUND_CATEGORIA_ASTA, foundCategoriaAsta);
        log.debug(LOG_CATEGORIA_ASTA_RECUPERATA);

        if (foundCategoriaAsta.isEmpty())
            throw new UpdateRuntimeException("Il nome '" + nome + "' non corrisponde a nessuna categoria asta esistente!");
        else {

            // Recuperiamo l'entità dal wrapping Optional
            CategoriaAsta existingCategoriaAsta = foundCategoriaAsta.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedCategoriaAstaDto, existingCategoriaAsta);

            return categoriaAstaMapper.toDto(categoriaAstaRepository.save(existingCategoriaAsta));
        }
    }

    @Override
    public void delete(String nome) throws IllegalDeleteRequestException {

        log.trace("Nome categoria asta da eliminare: {}", nome);
        log.debug("Verifico che non ci sia nessun'asta associata alla categoria da eliminare...");

        Optional<CategoriaAsta> existingCategoriaAsta = categoriaAstaRepository.findById(nome);
        if (existingCategoriaAsta.isPresent() && (!existingCategoriaAsta.get().getAsteAssegnate().isEmpty())) {
            log.warn("Non puoi eliminare una categoria asta alla quale vi sono ancora aste associate! Elimina prima le aste associate...");

            throw new IllegalDeleteRequestException("Non puoi eliminare una categoria asta alla quale vi sono ancora aste associate! Elimina prima le aste associate...");
        }

        log.debug("Non ci sono aste associate alla categoria asta. Elimino la categoria asta dal database...");

        // Eliminiamo l'entità con l'id passato per parametro
        categoriaAstaRepository.deleteById(nome);

        log.debug("Categoria asta eliminata dal database.");
    }

    @Override
    public void checkFieldsValid(CategoriaAstaDto categoriaAstaDto) throws InvalidParameterException {

        log.debug("Verifico l'integrità dei dati di categoria asta...");

        checkNomeValid(categoriaAstaDto.getNome());

        log.debug("Integrità dei dati di categoria asta verificata.");
    }

    private void checkNomeValid(String nome) throws InvalidParameterException {

        log.trace("Controllo che 'nome' sia valido...");

        if (nome == null)
            throw new InvalidParameterException("Il nome non può essere null!");
        else if (nome.isBlank())
            throw new InvalidParameterException("Il nome non può essere vuoto!");

        log.trace("'nome' valido.");
    }

    @Override
    public void convertRelations(CategoriaAstaDto categoriaAstaDto, CategoriaAsta categoriaAsta) throws InvalidParameterException {

        log.debug("Recupero le associazioni di categoria asta...");

        convertAsteAssegnateShallow(categoriaAstaDto.getAsteAssegnateShallow(), categoriaAsta);

        log.debug("Associazioni di categoria asta recuperate.");
    }

    private void convertAsteAssegnateShallow(Set<AstaShallowDto> asteAssegnateShallow, CategoriaAsta categoriaAsta) throws InvalidParameterException {

        log.trace("Converto l'associazione 'asteAssegnate'...");

        categoriaAsta.getAsteAssegnate().clear();

        if (asteAssegnateShallow != null) {
            for (AstaShallowDto astaShallowDto : asteAssegnateShallow) {

                Asta convertedAsta = relationsConverter.convertAstaShallowRelation(astaShallowDto);

                if (convertedAsta != null) {
                    categoriaAsta.addAstaAssegnata(convertedAsta);
                    convertedAsta.setCategoria(categoriaAsta);
                }
            }
        }

        log.trace("'asteAssegnate' convertita correttamente.");
    }

    @Override
    public void updatePresentFields(CategoriaAstaDto updatedCategoriaAstaDto, CategoriaAsta existingCategoriaAsta) throws InvalidParameterException {

        log.debug("Effettuo le modifiche di categoria asta richieste...");

        ifPresentUpdateNome(updatedCategoriaAstaDto.getNome(), existingCategoriaAsta);

        log.debug("Modifiche di categoria asta effettuate correttamente.");

        // Non è possibile modificare l'associazione "asteAssegnate" tramite la risorsa "categoria-asta"
    }

    private void ifPresentUpdateNome(String updatedNome, CategoriaAsta existingCategoriaAsta) throws InvalidParameterException {

        log.trace("Effettuo la modifica di 'nome'...");

        if (updatedNome != null) {
            checkNomeValid(updatedNome);
            existingCategoriaAsta.setNome(updatedNome);
        }

        log.trace("'nome' modificato correttamente.");
    }
}

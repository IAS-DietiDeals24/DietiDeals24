package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Asta;
import com.iasdietideals24.backend.entities.CategoriaAsta;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.exceptions.UpdateRuntimeException;
import com.iasdietideals24.backend.mapstruct.dto.CategoriaAstaDto;
import com.iasdietideals24.backend.mapstruct.dto.shallows.AstaShallowDto;
import com.iasdietideals24.backend.mapstruct.mappers.CategoriaAstaMapper;
import com.iasdietideals24.backend.repositories.CategoriaAstaRepository;
import com.iasdietideals24.backend.services.CategoriaAstaService;
import com.iasdietideals24.backend.utilities.RelationsConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CategoriaAstaServiceImpl implements CategoriaAstaService {

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

        // Verifichiamo l'integrità dei dati
        nuovoCategoriaAstaDto.setNome(nome);
        checkFieldsValid(nuovoCategoriaAstaDto);

        // Convertiamo a entità
        CategoriaAsta nuovaCategoriaAsta = categoriaAstaMapper.toEntity(nuovoCategoriaAstaDto);

        // Recuperiamo le associazioni
        convertRelations(nuovoCategoriaAstaDto, nuovaCategoriaAsta);

        // Registriamo l'entità
        CategoriaAsta savedCategoriaAsta = categoriaAstaRepository.save(nuovaCategoriaAsta);

        return categoriaAstaMapper.toDto(savedCategoriaAsta);
    }

    @Override
    public Page<CategoriaAstaDto> findAll(Pageable pageable) {

        // Recuperiamo tutte le entità
        Page<CategoriaAsta> foundCategoriaAsta = categoriaAstaRepository.findAll(pageable);

        return foundCategoriaAsta.map(categoriaAstaMapper::toDto);
    }

    @Override
    public Optional<CategoriaAstaDto> findOne(String nome) {

        // Recuperiamo l'entità con l'id passato per parametro
        Optional<CategoriaAsta> foundCategoriaAsta = categoriaAstaRepository.findById(nome);

        return foundCategoriaAsta.map(categoriaAstaMapper::toDto);
    }

    @Override
    public boolean isExists(String nome) {

        // Verifichiamo che esista un'entità con l'id passato per parametro
        return categoriaAstaRepository.existsById(nome);
    }

    @Override
    public CategoriaAstaDto fullUpdate(String nome, CategoriaAstaDto updatedCategoriaAstaDto) throws InvalidParameterException {

        // L'implementazione di fullUpdate e create sono identiche, dato che utilizziamo lo stesso metodo "save" della repository
        return this.create(nome, updatedCategoriaAstaDto);
    }

    @Override
    public CategoriaAstaDto partialUpdate(String nome, CategoriaAstaDto updatedCategoriaAstaDto) throws InvalidParameterException {

        // Recuperiamo l'entità con l'id passato per parametro
        updatedCategoriaAstaDto.setNome(nome);
        Optional<CategoriaAsta> foundCategoriaAsta = categoriaAstaRepository.findById(nome);

        if (foundCategoriaAsta.isEmpty())
            throw new UpdateRuntimeException("Il nome \"" + nome + "\" non corrisponde a nessuna categoria asta esistente!");
        else {

            // Recuperiamo l'entità dal wrapping Optional
            CategoriaAsta existingCategoriaAsta = foundCategoriaAsta.get();

            // Effettuiamo le modifiche
            updatePresentFields(updatedCategoriaAstaDto, existingCategoriaAsta);

            return categoriaAstaMapper.toDto(categoriaAstaRepository.save(existingCategoriaAsta));
        }
    }

    @Override
    public void delete(String nome) {

        // Eliminiamo l'entità con l'id passato per parametro
        categoriaAstaRepository.deleteById(nome);
    }

    @Override
    public void checkFieldsValid(CategoriaAstaDto categoriaAstaDto) throws InvalidParameterException {
        checkNomeValid(categoriaAstaDto.getNome());
    }

    private void checkNomeValid(String nome) throws InvalidParameterException {
        if (nome == null)
            throw new InvalidParameterException("Il nome non può essere null!");
        else if (nome.isBlank())
            throw new InvalidParameterException("Il nome non può essere vuoto!");
    }

    @Override
    public void convertRelations(CategoriaAstaDto categoriaAstaDto, CategoriaAsta categoriaAsta) throws InvalidParameterException {
        convertAsteAssegnateShallow(categoriaAstaDto.getAsteAssegnateShallow(), categoriaAsta);
    }

    private void convertAsteAssegnateShallow(Set<AstaShallowDto> asteAssegnateShallow, CategoriaAsta categoriaAsta) throws InvalidParameterException {
        if (asteAssegnateShallow != null) {
            for (AstaShallowDto astaShallowDto : asteAssegnateShallow) {

                Asta convertedAsta = relationsConverter.convertAstaShallowRelation(astaShallowDto);

                if (convertedAsta != null) {
                    categoriaAsta.addAstaAssegnata(convertedAsta);
                    convertedAsta.setCategoria(categoriaAsta);
                }
            }
        }
    }

    @Override
    public void updatePresentFields(CategoriaAstaDto updatedCategoriaAstaDto, CategoriaAsta existingCategoriaAsta) throws InvalidParameterException {
        ifPresentUpdateNome(updatedCategoriaAstaDto.getNome(), existingCategoriaAsta);

        // Non è possibile modificare l'associazione "asteAssegnate" tramite la risorsa "categoria-asta"
    }

    private void ifPresentUpdateNome(String updatedNome, CategoriaAsta existingCategoriaAsta) throws InvalidParameterException {
        if (updatedNome != null) {
            checkNomeValid(updatedNome);
            existingCategoriaAsta.setNome(updatedNome);
        }
    }
}

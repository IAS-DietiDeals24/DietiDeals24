package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Asta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstaRepository extends CrudRepository<Asta, Long>, PagingAndSortingRepository<Asta, Long> {

    @Query(value = "select a from asta a where a.nome like ?1 and a.categoria.nome = ?2")
    Page<Asta> findByNomeLikeAndCategoria_NomeIs(String nomeAsta, String nomeCategoria, Pageable pageable);
}

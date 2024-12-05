package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Asta;
import com.iasdietideals24.backend.entities.utilities.StatoAsta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface AstaRepository extends CrudRepository<Asta, Long>, PagingAndSortingRepository<Asta, Long> {

    @Query(value = "select a from asta a where a.nome like ?1 and a.categoria.nome = ?2")
    Page<Asta> findByNomeLikeAndCategoria_NomeIs(String nomeAsta, String nomeCategoria, Pageable pageable);

    @Query(value = "select a from asta a where a.dataScadenza <= ?1 and a.oraScadenza <= ?2 and a.stato = ?3")
    Page<Asta> findByDataScadenzaIsAfterAndOraScadenzaIsAfterAndStatoIs(LocalDate data, LocalTime ora, StatoAsta stato, Pageable pageable);
}

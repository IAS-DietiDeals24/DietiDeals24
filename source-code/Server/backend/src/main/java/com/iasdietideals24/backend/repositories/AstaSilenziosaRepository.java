package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.AstaSilenziosa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstaSilenziosaRepository extends CrudRepository<AstaSilenziosa, Long>, PagingAndSortingRepository<AstaSilenziosa, Long> {

    @Query(value = "select a from asta_silenziosa a where a.proprietario.idAccount = ?1")
    Page<AstaSilenziosa> findByProprietario_IdAccountIs(Long idAccount, Pageable pageable);

    @Query(value = "select a from asta_silenziosa a where a.nome like ?1 and a.categoria.nome = ?2")
    Page<AstaSilenziosa> findByNomeLikeAndCategoria_NomeIs(String nomeAsta, String nomeCategoria, Pageable pageable);

    @Query(value = "select a from asta_silenziosa a join a.offerteRicevute o where o.compratoreCollegato.idAccount = ?1")
    Page<AstaSilenziosa> findByOfferente_IdAccountIs(Long idAccount, Pageable pageable);
}

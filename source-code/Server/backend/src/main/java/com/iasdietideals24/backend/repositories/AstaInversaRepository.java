package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.AstaInversa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstaInversaRepository extends CrudRepository<AstaInversa, Long>, PagingAndSortingRepository<AstaInversa, Long> {

    @Query(value = "select a from asta_inversa a where a.proprietario.idAccount = ?1")
    Page<AstaInversa> findByProprietario_IdAccountIs(Long idAccount, Pageable pageable);

    @Query(value = "select a from asta_inversa a where a.proprietario.idAccount <> ?1")
    Page<AstaInversa> findByProprietario_IdAccountNot(Long idAccount, Pageable pageable);

    @Query(value = "select a from asta_inversa a where a.nome like ?1 and a.categoria.nome = ?2")
    Page<AstaInversa> findByNomeLikeAndCategoria_NomeIs(String nomeAsta, String nomeCategoria, Pageable pageable);

    @Query(value = "select a from asta_inversa a join a.offerteRicevute o where o.venditoreCollegato.idAccount = ?1")
    Page<AstaInversa> findByOfferente_IdAccountIs(Long idAccount, Pageable pageable);
}

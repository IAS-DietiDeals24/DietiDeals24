package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.AstaDiVenditore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstaDiVenditoreRepository extends CrudRepository<AstaDiVenditore, Long>, PagingAndSortingRepository<AstaDiVenditore, Long> {

    @Query(value = "select a from asta_di_venditore a where a.proprietario.idAccount = ?1")
    Page<AstaDiVenditore> findByIdAccountProprietario(Long idAccount, Pageable pageable);

    @Query(value = "select a from asta_di_venditore a where a.nome like ?1 and a.categoria.nome = ?2")
    Page<AstaDiVenditore> findByNomeAstaLikeAndNomeCategoria(String nomeAsta, String nomeCategoria, Pageable pageable);
}

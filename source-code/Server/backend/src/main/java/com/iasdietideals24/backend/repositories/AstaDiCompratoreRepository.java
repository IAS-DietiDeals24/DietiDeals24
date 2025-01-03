package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.AstaDiCompratore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstaDiCompratoreRepository extends CrudRepository<AstaDiCompratore, Long>, PagingAndSortingRepository<AstaDiCompratore, Long> {

    @Query(value = "select a from asta_di_compratore a where a.proprietario.idAccount = ?1")
    Page<AstaDiCompratore> findByProprietario_IdAccountIs(Long idAccount, Pageable pageable);

    @Query(value = "select a from asta_di_compratore a where upper(a.nome) like ?1 and a.categoria.nome like ?2")
    Page<AstaDiCompratore> findByNomeLikeAndCategoria_NomeLike(String nomeAsta, String nomeCategoria, Pageable pageable);
}

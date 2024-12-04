package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.OffertaSilenziosa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OffertaSilenziosaRepository extends CrudRepository<OffertaSilenziosa, Long>, PagingAndSortingRepository<OffertaSilenziosa, Long> {

    @Query("select o from offerta_silenziosa o where o.astaRiferimento.idAsta = ?1")
    Page<OffertaSilenziosa> findByAstaRiferimento_IdAsta(Long idAsta, Pageable pageable);

    @Query("select o from offerta_silenziosa o where o.astaRiferimento.idAsta = ?1 and o.valore = (select max(o.valore) from offerta_silenziosa o where o.astaRiferimento.idAsta = ?1)")
    Optional<OffertaSilenziosa> findMaxByValoreAndAstaRiferimento_IdAstaIs(Long idAsta);

    @Query("select o from offerta_silenziosa o where o.astaRiferimento.idAsta = ?1 and o.compratoreCollegato.idAccount = ?2 and o.valore = (select max(o.valore) from offerta_silenziosa o where o.astaRiferimento.idAsta = ?1 and o.compratoreCollegato.idAccount = ?2)")
    Optional<OffertaSilenziosa> findMaxByValoreAndAstaRiferimento_IdAstaIsAndCompratoreCollegato_IdAccountIs(Long idAsta, Long idAccount);
}

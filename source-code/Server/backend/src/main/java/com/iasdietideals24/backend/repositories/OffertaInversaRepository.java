package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.OffertaInversa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OffertaInversaRepository extends CrudRepository<OffertaInversa, Long>, PagingAndSortingRepository<OffertaInversa, Long> {

    @Query("select o from offerta_inversa o where o.astaRiferimento.idAsta = ?1")
    Page<OffertaInversa> findByAstaRiferimento_IdAsta(Long idAsta, Pageable pageable);

    @Query("select o from offerta_inversa o where o.astaRiferimento.idAsta = ?1 and o.valore = (select min(o.valore) from offerta_inversa o where o.astaRiferimento.idAsta = ?1)")
    Optional<OffertaInversa> findMinByValoreAndAstaRiferimento_IdAstaIs(Long idAsta);

    @Query("select o from offerta_inversa o where o.astaRiferimento.idAsta = ?1 and o.venditoreCollegato.idAccount = ?2 and o.valore = (select min(o.valore) from offerta_inversa o where o.astaRiferimento.idAsta = ?1 and o.venditoreCollegato.idAccount = ?2)")
    Optional<OffertaInversa> findMinByValoreAndAstaRiferimento_IdAstaIsAndVenditoreCollegato_IdAccountIs(Long idAsta, Long idAccount);
}

package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.OffertaTempoFisso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OffertaTempoFissoRepository extends CrudRepository<OffertaTempoFisso, Long>, PagingAndSortingRepository<OffertaTempoFisso, Long> {

    @Query("select o from offerta_tempo_fisso o where o.astaRiferimento.idAsta = ?1")
    Page<OffertaTempoFisso> findByAstaRiferimento_IdAsta(Long idAsta, Pageable pageable);

    @Query("select o from offerta_tempo_fisso o where o.astaRiferimento.idAsta = ?1 and o.valore = (select max(o.valore) from offerta_tempo_fisso o where o.astaRiferimento.idAsta = ?1)")
    Optional<OffertaTempoFisso> findMaxByValoreAndAstaRiferimento_IdAstaIs(Long idAsta);

    @Query("select o from offerta_tempo_fisso o where o.astaRiferimento.idAsta = ?1 and o.compratoreCollegato.idAccount = ?2 and o.valore = (select max(o.valore) from offerta_tempo_fisso o where o.astaRiferimento.idAsta = ?1 and o.compratoreCollegato.idAccount = ?2)")
    Optional<OffertaTempoFisso> findMaxByValoreAndAstaRiferimento_IdAstaIsAndCompratoreCollegato_IdAccountIs(Long idAsta, Long idAccount);
}

package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.OffertaDiCompratore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffertaDiCompratoreRepository extends CrudRepository<OffertaDiCompratore, Long>, PagingAndSortingRepository<OffertaDiCompratore, Long> {
}

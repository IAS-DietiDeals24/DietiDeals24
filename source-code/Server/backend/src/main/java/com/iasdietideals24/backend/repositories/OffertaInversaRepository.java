package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.OffertaInversa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffertaInversaRepository extends CrudRepository<OffertaInversa, Long>, PagingAndSortingRepository<OffertaInversa, Long> {
}

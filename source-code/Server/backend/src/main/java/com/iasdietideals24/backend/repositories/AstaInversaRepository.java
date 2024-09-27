package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.AstaInversa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstaInversaRepository extends CrudRepository<AstaInversa, Long>, PagingAndSortingRepository<AstaInversa, Long> {
}

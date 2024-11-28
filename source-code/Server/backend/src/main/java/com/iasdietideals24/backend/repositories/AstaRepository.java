package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Asta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstaRepository extends CrudRepository<Asta, Long>, PagingAndSortingRepository<Asta, Long> {
}

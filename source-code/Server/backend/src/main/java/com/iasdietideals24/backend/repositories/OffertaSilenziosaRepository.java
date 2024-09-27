package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.OffertaSilenziosa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffertaSilenziosaRepository extends CrudRepository<OffertaSilenziosa, Long>, PagingAndSortingRepository<OffertaSilenziosa, Long> {
}

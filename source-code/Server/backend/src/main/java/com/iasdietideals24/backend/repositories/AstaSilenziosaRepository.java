package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.AstaSilenziosa;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstaSilenziosaRepository extends CrudRepository<AstaSilenziosa, Long>, PagingAndSortingRepository<AstaSilenziosa, Long> {
}

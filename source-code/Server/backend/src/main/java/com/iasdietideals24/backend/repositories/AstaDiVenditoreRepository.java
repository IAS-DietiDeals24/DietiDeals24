package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.AstaDiVenditore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstaDiVenditoreRepository extends CrudRepository<AstaDiVenditore, Long>, PagingAndSortingRepository<AstaDiVenditore, Long> {
}

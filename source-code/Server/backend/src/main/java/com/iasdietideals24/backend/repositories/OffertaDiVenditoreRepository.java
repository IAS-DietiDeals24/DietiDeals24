package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.OffertaDiVenditore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffertaDiVenditoreRepository extends CrudRepository<OffertaDiVenditore, Long>, PagingAndSortingRepository<OffertaDiVenditore, Long> {
}

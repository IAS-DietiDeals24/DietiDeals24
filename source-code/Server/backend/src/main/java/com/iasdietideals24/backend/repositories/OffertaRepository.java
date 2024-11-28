package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Offerta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffertaRepository extends CrudRepository<Offerta, Long>, PagingAndSortingRepository<Offerta, Long> {
}

package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Venditore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenditoreRepository extends CrudRepository<Venditore, Long>, PagingAndSortingRepository<Venditore, Long> {

    Page<Venditore> findByEmailIs(String email, Pageable pageable);
}

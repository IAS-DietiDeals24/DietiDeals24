package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Compratore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompratoreRepository extends CrudRepository<Compratore, Long>, PagingAndSortingRepository<Compratore, Long> {

    Page<Compratore> findByEmail(String email, Pageable pageable);
}

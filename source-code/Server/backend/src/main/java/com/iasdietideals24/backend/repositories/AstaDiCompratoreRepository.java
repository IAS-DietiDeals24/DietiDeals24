package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.AstaDiCompratore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AstaDiCompratoreRepository extends CrudRepository<AstaDiCompratore, Long>, PagingAndSortingRepository<AstaDiCompratore, Long> {
}

package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Profilo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfiloRepository extends CrudRepository<Profilo, String>, PagingAndSortingRepository<Profilo, String> {
}

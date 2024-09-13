package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Compratore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompratoreRepository extends CrudRepository<Compratore, String> {
}

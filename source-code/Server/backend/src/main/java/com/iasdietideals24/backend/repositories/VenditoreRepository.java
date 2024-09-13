package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Venditore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenditoreRepository extends CrudRepository<Venditore, String> {
}

package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Profilo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfiloRepository extends CrudRepository<Profilo, String>, PagingAndSortingRepository<Profilo, String> {

    @Query("SELECT p FROM profilo p JOIN p.accounts a WHERE a.email = ?1")
    Optional<Profilo> findByAccountsEmail(String nome);
}

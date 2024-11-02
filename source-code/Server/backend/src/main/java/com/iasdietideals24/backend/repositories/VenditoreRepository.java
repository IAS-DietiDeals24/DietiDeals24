package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Venditore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VenditoreRepository extends CrudRepository<Venditore, String>, PagingAndSortingRepository<Venditore, String> {

    Optional<Venditore> findByTokensIdFacebook(String idFacebook);

    Optional<Venditore> findByEmailAndPassword(String email, String password);
}

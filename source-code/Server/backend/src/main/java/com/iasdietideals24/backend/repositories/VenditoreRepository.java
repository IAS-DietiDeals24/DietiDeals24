package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Venditore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VenditoreRepository extends CrudRepository<Venditore, Long>, PagingAndSortingRepository<Venditore, Long> {

    Optional<Venditore> findOneByTokensIdFacebook(String idFacebook);

    Optional<Venditore> findOneByEmail(String email);

    Optional<Venditore> findOneByEmailAndPassword(String email, String password);
}

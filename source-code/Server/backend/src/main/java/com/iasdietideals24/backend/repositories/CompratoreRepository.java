package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Compratore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompratoreRepository extends CrudRepository<Compratore, String>, PagingAndSortingRepository<Compratore, String> {

    Optional<Compratore> findByTokensIdFacebook(String idFacebook);

    Optional<Compratore> findByEmailAndPassword(String email, String password);
}

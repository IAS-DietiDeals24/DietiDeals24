package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Compratore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompratoreRepository extends CrudRepository<Compratore, Long>, PagingAndSortingRepository<Compratore, Long> {

    Optional<Compratore> findOneByTokensIdFacebook(String idFacebook);

    Optional<Compratore> findOneByEmail(String email);

    Optional<Compratore> findOneByEmailAndPassword(String email, String password);
}

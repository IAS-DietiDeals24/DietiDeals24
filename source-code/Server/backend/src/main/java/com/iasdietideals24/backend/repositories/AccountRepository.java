package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.Venditore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long>, PagingAndSortingRepository<Account, Long> {

    Page<Account> findByTokensIdFacebook(String idFacebook, Pageable pageable);

    Page<Account> findByEmail(String email, Pageable pageable);

    Page<Account> findByEmailAndPassword(String email, String password, Pageable pageable);
}

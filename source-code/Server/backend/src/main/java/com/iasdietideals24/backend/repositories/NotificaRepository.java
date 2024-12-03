package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Notifica;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificaRepository extends CrudRepository<Notifica, Long>, PagingAndSortingRepository<Notifica, Long> {

    @Query(value = "select n from notifica n join n.destinatari d where d.idAccount = ?1")
    Page<Notifica> findByDestinatario(Long idAccount, Pageable pageable);
}

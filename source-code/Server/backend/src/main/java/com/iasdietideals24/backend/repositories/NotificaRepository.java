package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.entities.Notifica;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificaRepository extends CrudRepository<Notifica, Long> {
}

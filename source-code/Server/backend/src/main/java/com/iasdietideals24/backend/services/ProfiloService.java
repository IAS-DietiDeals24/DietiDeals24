package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.entities.Profilo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProfiloService {

    Profilo createOrFullUpdate(Profilo nuovoProfilo);

    Page<Profilo> findAll(Pageable pageable);

    Optional<Profilo> findOne(String nomeUtente);

    boolean isExists(String nomeUtente);

    Profilo partialUpdate(String nomeUtente, Profilo nuovoProfilo);

    void delete(String nomeUtente);
}

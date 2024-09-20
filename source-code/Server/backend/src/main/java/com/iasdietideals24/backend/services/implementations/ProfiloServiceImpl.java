package com.iasdietideals24.backend.services.implementations;

import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.services.ProfiloService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfiloServiceImpl implements ProfiloService {
    @Override
    public Profilo createOrFullUpdate(Profilo nuovoProfilo) {
        return null;
    }

    @Override
    public Page<Profilo> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Profilo> findOne(String nomeUtente) {
        return Optional.empty();
    }

    @Override
    public boolean isExists(String nomeUtente) {
        return false;
    }

    @Override
    public Profilo partialUpdate(String nomeUtente, Profilo nuovoProfilo) {
        return null;
    }

    @Override
    public void delete(String nomeUtente) {

    }
}

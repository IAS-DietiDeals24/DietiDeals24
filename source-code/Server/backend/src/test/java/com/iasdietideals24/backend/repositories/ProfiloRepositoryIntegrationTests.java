package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.TestDataProfilo;
import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.exceptions.ParameterNotValidException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ProfiloRepositoryIntegrationTests {

    private final ProfiloRepository underTest;

    @Autowired
    public ProfiloRepositoryIntegrationTests(ProfiloRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    @Transactional
    void testProfiloCanBeCreatedAndRecalled() throws ParameterNotValidException {
        // Creazione oggetto
        Profilo profilo = TestDataProfilo.createProfiloVenditoreA();

        // Salvataggio oggetto nel database
        underTest.save(profilo);

        // Recupero oggetto dal database
        Optional<Profilo> result = underTest.findById(profilo.getNomeUtente());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(profilo, result.get());
    }

    @Test
    @Transactional
    void testMultipleProfiloCanBeCreatedAndRecalled() throws ParameterNotValidException {
        // Creazione oggetti
        Profilo profiloA = TestDataProfilo.createProfiloVenditoreA();

        Profilo profiloB = TestDataProfilo.createProfiloCompratoreB();

        Profilo profiloC = TestDataProfilo.createProfiloVenditoreC();

        // Salvataggio oggetti nel database
        underTest.save(profiloA);
        underTest.save(profiloB);
        underTest.save(profiloC);

        // Recupero oggetti dal database
        List<Profilo> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(profiloA) && result.contains(profiloB) && result.contains(profiloC));
    }

    @Test
    @Transactional
    void testProfiloCanBeUpdated() throws ParameterNotValidException {
        // Creazione e salvataggio oggetto nel database
        Profilo profilo = TestDataProfilo.createProfiloVenditoreB();
        underTest.save(profilo);

        // Modifica e salvataggio dell'oggetto nel database
        profilo.setNomeUtente("UPDATED");
        underTest.save(profilo);

        // Recupero l'oggetto dal database
        Optional<Profilo> result = underTest.findById(profilo.getNomeUtente());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(profilo, result.get());
    }

    @Test
    @Transactional
    void testProfiloCanBeDeleted() throws ParameterNotValidException {
        // Creazione e salvataggio oggetto nel database
        Profilo profilo = TestDataProfilo.createProfiloCompratoreC();
        underTest.save(profilo);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(profilo.getNomeUtente());

        // Recupero l'oggetto dal database
        Optional<Profilo> result = underTest.findById(profilo.getNomeUtente());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

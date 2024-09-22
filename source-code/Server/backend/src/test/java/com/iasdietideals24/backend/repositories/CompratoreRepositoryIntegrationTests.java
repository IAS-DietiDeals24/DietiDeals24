package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.TestDataProfilo;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
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
class CompratoreRepositoryIntegrationTests {

    private final CompratoreRepository underTest;

    @Autowired
    public CompratoreRepositoryIntegrationTests(CompratoreRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    @Transactional
    void testCompratoreCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetto
        Profilo profiloCompratore = TestDataProfilo.createProfiloCompratoreC();
        Compratore compratore = profiloCompratore.getCompratore();

        // Salvataggio oggetto nel database
        underTest.save(compratore);

        // Recupero oggetto dal database
        Optional<Compratore> result = underTest.findById(compratore.getEmail());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(compratore, result.get());
    }

    @Test
    @Transactional
    void testMultipleCompratoreCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetti
        Profilo profiloCompratoreA = TestDataProfilo.createProfiloCompratoreA();
        Compratore compratoreA = profiloCompratoreA.getCompratore();

        Profilo profiloCompratoreB = TestDataProfilo.createProfiloCompratoreB();
        Compratore compratoreB = profiloCompratoreB.getCompratore();

        Profilo profiloCompratoreC = TestDataProfilo.createProfiloCompratoreC();
        Compratore compratoreC = profiloCompratoreC.getCompratore();

        // Salvataggio oggetti nel database
        underTest.save(compratoreA);
        underTest.save(compratoreB);
        underTest.save(compratoreC);

        // Recupero oggetti dal database
        List<Compratore> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(compratoreA) && result.contains(compratoreB) && result.contains(compratoreC));
    }

    @Test
    @Transactional
    void testCompratoreCanBeUpdated() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloCompratore = TestDataProfilo.createProfiloCompratoreA();
        Compratore compratore = profiloCompratore.getCompratore();
        underTest.save(compratore);

        // Modifica e salvataggio dell'oggetto nel database
        compratore.setEmail("UPDATED");
        underTest.save(compratore);

        // Recupero l'oggetto dal database
        Optional<Compratore> result = underTest.findById(compratore.getEmail());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(compratore, result.get());
    }

    @Test
    @Transactional
    void testCompratoreCanBeDeleted() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloCompratore = TestDataProfilo.createProfiloCompratoreA();
        Compratore compratore = profiloCompratore.getCompratore();
        underTest.save(compratore);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(compratore.getEmail());

        // Recupero l'oggetto dal database
        Optional<Compratore> result = underTest.findById(compratore.getEmail());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

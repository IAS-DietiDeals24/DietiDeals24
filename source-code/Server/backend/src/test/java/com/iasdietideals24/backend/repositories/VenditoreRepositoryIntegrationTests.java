package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.TestDataProfilo;
import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.entities.Venditore;
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
class VenditoreRepositoryIntegrationTests {

    private final VenditoreRepository underTest;

    @Autowired
    public VenditoreRepositoryIntegrationTests(VenditoreRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    @Transactional
    void testVenditoreCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetto
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreC();
        Venditore venditore = profiloVenditore.getVenditore();

        // Salvataggio oggetto nel database
        underTest.save(venditore);

        // Recupero oggetto dal database
        Optional<Venditore> result = underTest.findById(venditore.getIdAccount());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(venditore, result.get());
    }

    @Test
    @Transactional
    void testMultipleVenditoreCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetti
        Profilo profiloVenditoreA = TestDataProfilo.createProfiloVenditoreA();
        Venditore venditoreA = profiloVenditoreA.getVenditore();

        Profilo profiloVenditoreB = TestDataProfilo.createProfiloVenditoreB();
        Venditore venditoreB = profiloVenditoreB.getVenditore();

        Profilo profiloVenditoreC = TestDataProfilo.createProfiloVenditoreC();
        Venditore venditoreC = profiloVenditoreC.getVenditore();

        // Salvataggio oggetti nel database
        underTest.save(venditoreA);
        underTest.save(venditoreB);
        underTest.save(venditoreC);

        // Recupero oggetti dal database
        List<Venditore> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(venditoreA) && result.contains(venditoreB) && result.contains(venditoreC));
    }

    @Test
    @Transactional
    void testVenditoreCanBeUpdated() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreA();
        Venditore venditore = profiloVenditore.getVenditore();
        underTest.save(venditore);

        // Modifica e salvataggio dell'oggetto nel database
        venditore.setEmail("UPDATED");
        underTest.save(venditore);

        // Recupero l'oggetto dal database
        Optional<Venditore> result = underTest.findById(venditore.getIdAccount());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(venditore, result.get());
    }

    @Test
    @Transactional
    void testVenditoreCanBeDeleted() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreA();
        Venditore venditore = profiloVenditore.getVenditore();
        underTest.save(venditore);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(venditore.getIdAccount());

        // Recupero l'oggetto dal database
        Optional<Venditore> result = underTest.findById(venditore.getIdAccount());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

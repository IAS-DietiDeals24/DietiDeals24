package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.TestDataAstaInversa;
import com.iasdietideals24.backend.datautil.TestDataProfilo;
import com.iasdietideals24.backend.entities.AstaInversa;
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
class AstaInversaRepositoryIntegrationTests {

    private final AstaInversaRepository underTest;

    @Autowired
    public AstaInversaRepositoryIntegrationTests(AstaInversaRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    @Transactional
    void testAstaInversaCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetto
        Profilo profiloCompratore = TestDataProfilo.createProfiloCompratoreC();
        Compratore compratore = profiloCompratore.getCompratore();
        AstaInversa astaInversa = TestDataAstaInversa.createAstaInversaA(compratore);

        // Salvataggio oggetto nel database
        underTest.save(astaInversa);

        // Recupero oggetto dal database
        Optional<AstaInversa> result = underTest.findById(astaInversa.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(astaInversa, result.get());
    }

    @Test
    @Transactional
    void testMultipleAstaInversaCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetti
        Profilo profiloCompratoreA = TestDataProfilo.createProfiloCompratoreA();
        Compratore compratoreA = profiloCompratoreA.getCompratore();
        AstaInversa astaInversaA = TestDataAstaInversa.createAstaInversaA(compratoreA);

        Profilo profiloCompratoreB = TestDataProfilo.createProfiloCompratoreB();
        Compratore compratoreB = profiloCompratoreB.getCompratore();
        AstaInversa astaInversaB = TestDataAstaInversa.createAstaInversaB(compratoreB);

        Profilo profiloCompratoreC = TestDataProfilo.createProfiloCompratoreC();
        Compratore compratoreC = profiloCompratoreC.getCompratore();
        AstaInversa astaInversaC = TestDataAstaInversa.createAstaInversaC(compratoreC);

        // Salvataggio oggetti nel database
        underTest.save(astaInversaA);
        underTest.save(astaInversaB);
        underTest.save(astaInversaC);

        // Recupero oggetti dal database
        List<AstaInversa> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(astaInversaA) && result.contains(astaInversaB) && result.contains(astaInversaC));
    }

    @Test
    @Transactional
    void testAstaInversaCanBeUpdated() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloCompratore = TestDataProfilo.createProfiloCompratoreA();
        Compratore compratore = profiloCompratore.getCompratore();
        AstaInversa astaInversa = TestDataAstaInversa.createAstaInversaA(compratore);
        underTest.save(astaInversa);

        // Modifica e salvataggio dell'oggetto nel database
        astaInversa.setNome("UPDATED");
        underTest.save(astaInversa);

        // Recupero l'oggetto dal database
        Optional<AstaInversa> result = underTest.findById(astaInversa.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(astaInversa, result.get());
    }

    @Test
    @Transactional
    void testAstaInversaCanBeDeleted() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloCompratore = TestDataProfilo.createProfiloCompratoreA();
        Compratore compratore = profiloCompratore.getCompratore();
        AstaInversa astaInversa = TestDataAstaInversa.createAstaInversaA(compratore);
        underTest.save(astaInversa);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(astaInversa.getIdAsta());

        // Recupero l'oggetto dal database
        Optional<AstaInversa> result = underTest.findById(astaInversa.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

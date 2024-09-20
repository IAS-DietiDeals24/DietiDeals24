package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.TestDataAstaInversa;
import com.iasdietideals24.backend.datautil.TestDataProfilo;
import com.iasdietideals24.backend.entities.AstaDiCompratore;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
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
class AstaDiCompratoreRepositoryIntegrationTests {

    private final AstaDiCompratoreRepository underTest;

    @Autowired
    public AstaDiCompratoreRepositoryIntegrationTests(AstaDiCompratoreRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    @Transactional
    void testAstaDiCompratoreCanBeCreatedAndRecalled() throws InvalidParameterException {
        // Creazione oggetto
        Profilo profiloCompratore = TestDataProfilo.createProfiloCompratoreC();
        Compratore compratore = profiloCompratore.getCompratore();
        AstaDiCompratore astaDiCompratore = TestDataAstaInversa.createAstaInversaA(compratore);

        // Salvataggio oggetto nel database
        underTest.save(astaDiCompratore);

        // Recupero oggetto dal database
        Optional<AstaDiCompratore> result = underTest.findById(astaDiCompratore.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(astaDiCompratore, result.get());
    }

    @Test
    @Transactional
    void testMultipleAstaDiCompratoreCanBeCreatedAndRecalled() throws InvalidParameterException {
        // Creazione oggetti
        Profilo profiloCompratoreA = TestDataProfilo.createProfiloCompratoreA();
        Compratore compratoreA = profiloCompratoreA.getCompratore();
        AstaDiCompratore astaDiCompratoreA = TestDataAstaInversa.createAstaInversaA(compratoreA);

        Profilo profiloCompratoreB = TestDataProfilo.createProfiloCompratoreB();
        Compratore compratoreB = profiloCompratoreB.getCompratore();
        AstaDiCompratore astaDiCompratoreB = TestDataAstaInversa.createAstaInversaB(compratoreB);

        Profilo profiloCompratoreC = TestDataProfilo.createProfiloCompratoreC();
        Compratore compratoreC = profiloCompratoreC.getCompratore();
        AstaDiCompratore astaDiCompratoreC = TestDataAstaInversa.createAstaInversaC(compratoreC);

        // Salvataggio oggetti nel database
        underTest.save(astaDiCompratoreA);
        underTest.save(astaDiCompratoreB);
        underTest.save(astaDiCompratoreC);

        // Recupero oggetti dal database
        List<AstaDiCompratore> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(astaDiCompratoreA) && result.contains(astaDiCompratoreB) && result.contains(astaDiCompratoreC));
    }

    @Test
    @Transactional
    void testAstaDiCompratoreCanBeUpdated() throws InvalidParameterException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloCompratore = TestDataProfilo.createProfiloCompratoreA();
        Compratore compratore = profiloCompratore.getCompratore();
        AstaDiCompratore astaDiCompratore = TestDataAstaInversa.createAstaInversaA(compratore);
        underTest.save(astaDiCompratore);

        // Modifica e salvataggio dell'oggetto nel database
        astaDiCompratore.setNome("UPDATED");
        underTest.save(astaDiCompratore);

        // Recupero l'oggetto dal database
        Optional<AstaDiCompratore> result = underTest.findById(astaDiCompratore.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(astaDiCompratore, result.get());
    }

    @Test
    @Transactional
    void testAstaDiCompratoreCanBeDeleted() throws InvalidParameterException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloCompratore = TestDataProfilo.createProfiloCompratoreA();
        Compratore compratore = profiloCompratore.getCompratore();
        AstaDiCompratore astaDiCompratore = TestDataAstaInversa.createAstaInversaA(compratore);
        underTest.save(astaDiCompratore);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(astaDiCompratore.getIdAsta());

        // Recupero l'oggetto dal database
        Optional<AstaDiCompratore> result = underTest.findById(astaDiCompratore.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

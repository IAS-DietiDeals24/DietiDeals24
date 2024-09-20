package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.TestDataAstaSilenziosa;
import com.iasdietideals24.backend.datautil.TestDataAstaTempoFisso;
import com.iasdietideals24.backend.datautil.TestDataProfilo;
import com.iasdietideals24.backend.entities.AstaDiVenditore;
import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.entities.Venditore;
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
class AstaDiVenditoreRepositoryIntegrationTests {

    private final AstaDiVenditoreRepository underTest;

    @Autowired
    public AstaDiVenditoreRepositoryIntegrationTests(AstaDiVenditoreRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    @Transactional
    void testAstaDiVenditoreCanBeCreatedAndRecalled() throws InvalidParameterException {
        // Creazione oggetto
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreC();
        Venditore venditore = profiloVenditore.getVenditore();
        AstaDiVenditore astaDiVenditore = TestDataAstaSilenziosa.createAstaSilenziosaA(venditore);

        // Salvataggio oggetto nel database
        underTest.save(astaDiVenditore);

        // Recupero oggetto dal database
        Optional<AstaDiVenditore> result = underTest.findById(astaDiVenditore.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(astaDiVenditore, result.get());
    }

    @Test
    @Transactional
    void testMultipleAstaDiVenditoreCanBeCreatedAndRecalled() throws InvalidParameterException {
        // Creazione oggetti
        Profilo profiloVenditoreA = TestDataProfilo.createProfiloVenditoreA();
        Venditore venditoreA = profiloVenditoreA.getVenditore();
        AstaDiVenditore astaDiVenditoreA = TestDataAstaSilenziosa.createAstaSilenziosaA(venditoreA);

        Profilo profiloVenditoreB = TestDataProfilo.createProfiloVenditoreB();
        Venditore venditoreB = profiloVenditoreB.getVenditore();
        AstaDiVenditore astaDiVenditoreB = TestDataAstaTempoFisso.createAstaTempoFissoB(venditoreB);

        Profilo profiloVenditoreC = TestDataProfilo.createProfiloVenditoreC();
        Venditore venditoreC = profiloVenditoreC.getVenditore();
        AstaDiVenditore astaDiVenditoreC = TestDataAstaSilenziosa.createAstaSilenziosaC(venditoreC);

        // Salvataggio oggetti nel database
        underTest.save(astaDiVenditoreA);
        underTest.save(astaDiVenditoreB);
        underTest.save(astaDiVenditoreC);

        // Recupero oggetti dal database
        List<AstaDiVenditore> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(astaDiVenditoreA) && result.contains(astaDiVenditoreB) && result.contains(astaDiVenditoreC));
    }

    @Test
    @Transactional
    void testAstaDiVenditoreCanBeUpdated() throws InvalidParameterException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreA();
        Venditore venditore = profiloVenditore.getVenditore();
        AstaDiVenditore astaDiVenditore = TestDataAstaTempoFisso.createAstaTempoFissoC(venditore);
        underTest.save(astaDiVenditore);

        // Modifica e salvataggio dell'oggetto nel database
        astaDiVenditore.setNome("UPDATED");
        underTest.save(astaDiVenditore);

        // Recupero l'oggetto dal database
        Optional<AstaDiVenditore> result = underTest.findById(astaDiVenditore.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(astaDiVenditore, result.get());
    }

    @Test
    @Transactional
    void testAstaDiVenditoreCanBeDeleted() throws InvalidParameterException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreA();
        Venditore venditore = profiloVenditore.getVenditore();
        AstaDiVenditore astaDiVenditore = TestDataAstaSilenziosa.createAstaSilenziosaB(venditore);
        underTest.save(astaDiVenditore);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(astaDiVenditore.getIdAsta());

        // Recupero l'oggetto dal database
        Optional<AstaDiVenditore> result = underTest.findById(astaDiVenditore.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

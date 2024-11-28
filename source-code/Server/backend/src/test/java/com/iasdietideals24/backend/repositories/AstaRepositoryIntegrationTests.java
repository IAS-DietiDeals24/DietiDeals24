package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.*;
import com.iasdietideals24.backend.entities.*;
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
class AstaRepositoryIntegrationTests {

    private final AstaRepository underTest;

    @Autowired
    public AstaRepositoryIntegrationTests(AstaRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    @Transactional
    void testAstaCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetto
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreA();
        Venditore venditore = profiloVenditore.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaE();
        Asta asta = TestDataAstaSilenziosa.createAstaSilenziosaA(categoriaAsta, venditore);

        // Salvataggio oggetto nel database
        underTest.save(asta);

        // Recupero oggetto dal database
        Optional<Asta> result = underTest.findById(asta.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(asta, result.get());
    }

    @Test
    @Transactional
    void testMultipleAstaCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetti
        Profilo profiloCompratoreA = TestDataProfilo.createProfiloCompratoreA();
        Compratore compratoreA = profiloCompratoreA.getCompratore();
        CategoriaAsta categoriaAstaA = TestDataCategoriaAsta.createCategoriaAstaE();
        Asta astaA = TestDataAstaInversa.createAstaInversaA(categoriaAstaA, compratoreA);

        Profilo profiloVenditoreB = TestDataProfilo.createProfiloVenditoreB();
        Venditore venditoreB = profiloVenditoreB.getVenditore();
        CategoriaAsta categoriaAstaC = TestDataCategoriaAsta.createCategoriaAstaK();
        Asta astaB = TestDataAstaTempoFisso.createAstaTempoFissoB(categoriaAstaC, venditoreB);

        Profilo profiloVenditoreC = TestDataProfilo.createProfiloVenditoreC();
        Venditore venditoreC = profiloVenditoreC.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaL();
        Asta astaC = TestDataAstaSilenziosa.createAstaSilenziosaC(categoriaAsta, venditoreC);

        // Salvataggio oggetti nel database
        underTest.save(astaA);
        underTest.save(astaB);
        underTest.save(astaC);

        // Recupero oggetti dal database
        List<Asta> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(astaA) && result.contains(astaB) && result.contains(astaC));
    }

    @Test
    @Transactional
    void testAstaCanBeUpdated() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloCompratore = TestDataProfilo.createProfiloCompratoreA();
        Compratore compratore = profiloCompratore.getCompratore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaE();
        Asta asta = TestDataAstaInversa.createAstaInversaB(categoriaAsta, compratore);
        underTest.save(asta);

        // Modifica e salvataggio dell'oggetto nel database
        asta.setNome("UPDATED");
        underTest.save(asta);

        // Recupero l'oggetto dal database
        Optional<Asta> result = underTest.findById(asta.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(asta, result.get());
    }

    @Test
    @Transactional
    void testAstaCanBeDeleted() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreB();
        Venditore venditore = profiloVenditore.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaK();
        Asta asta = TestDataAstaTempoFisso.createAstaTempoFissoC(categoriaAsta, venditore);
        underTest.save(asta);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(asta.getIdAsta());

        // Recupero l'oggetto dal database
        Optional<Asta> result = underTest.findById(asta.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

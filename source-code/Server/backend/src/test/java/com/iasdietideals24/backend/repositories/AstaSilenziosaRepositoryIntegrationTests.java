package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.TestDataAstaSilenziosa;
import com.iasdietideals24.backend.datautil.TestDataCategoriaAsta;
import com.iasdietideals24.backend.datautil.TestDataProfilo;
import com.iasdietideals24.backend.entities.AstaSilenziosa;
import com.iasdietideals24.backend.entities.CategoriaAsta;
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
class AstaSilenziosaRepositoryIntegrationTests {

    private final ProfiloRepository profiloRepository;
    private final CategoriaAstaRepository categoriaAstaRepository;

    private final AstaSilenziosaRepository underTest;

    @Autowired
    public AstaSilenziosaRepositoryIntegrationTests(AstaSilenziosaRepository underTest, ProfiloRepository profiloRepository, CategoriaAstaRepository categoriaAstaRepository) {
        this.underTest = underTest;
        this.profiloRepository = profiloRepository;
        this.categoriaAstaRepository = categoriaAstaRepository;
    }

    @Test
    @Transactional
    void testAstaSilenziosaCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetto
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreC();
        profiloVenditore = profiloRepository.save(profiloVenditore);
        Venditore venditore = profiloVenditore.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaM();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaSilenziosa astaSilenziosa = TestDataAstaSilenziosa.createAstaSilenziosaA(categoriaAsta, venditore);

        // Salvataggio oggetto nel database
        underTest.save(astaSilenziosa);

        // Recupero oggetto dal database
        Optional<AstaSilenziosa> result = underTest.findById(astaSilenziosa.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(astaSilenziosa, result.get());
    }

    @Test
    @Transactional
    void testMultipleAstaSilenziosaCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetti
        Profilo profiloVenditoreA = TestDataProfilo.createProfiloVenditoreA();
        profiloVenditoreA = profiloRepository.save(profiloVenditoreA);
        Venditore venditoreA = profiloVenditoreA.getVenditore();
        CategoriaAsta categoriaAstaA = TestDataCategoriaAsta.createCategoriaAstaM();
        categoriaAstaA = categoriaAstaRepository.save(categoriaAstaA);
        AstaSilenziosa astaSilenziosaA = TestDataAstaSilenziosa.createAstaSilenziosaA(categoriaAstaA, venditoreA);

        Profilo profiloVenditoreB = TestDataProfilo.createProfiloVenditoreB();
        profiloVenditoreB = profiloRepository.save(profiloVenditoreB);
        Venditore venditoreB = profiloVenditoreB.getVenditore();
        CategoriaAsta categoriaAstaB = TestDataCategoriaAsta.createCategoriaAstaB();
        categoriaAstaB = categoriaAstaRepository.save(categoriaAstaB);
        AstaSilenziosa astaSilenziosaB = TestDataAstaSilenziosa.createAstaSilenziosaB(categoriaAstaB, venditoreB);

        Profilo profiloVenditoreC = TestDataProfilo.createProfiloVenditoreC();
        profiloVenditoreC = profiloRepository.save(profiloVenditoreC);
        Venditore venditoreC = profiloVenditoreC.getVenditore();
        CategoriaAsta categoriaAstaC = TestDataCategoriaAsta.createCategoriaAstaC();
        categoriaAstaC = categoriaAstaRepository.save(categoriaAstaC);
        AstaSilenziosa astaSilenziosaC = TestDataAstaSilenziosa.createAstaSilenziosaC(categoriaAstaC, venditoreC);

        // Salvataggio oggetti nel database
        underTest.save(astaSilenziosaA);
        underTest.save(astaSilenziosaB);
        underTest.save(astaSilenziosaC);

        // Recupero oggetti dal database
        List<AstaSilenziosa> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(astaSilenziosaA) && result.contains(astaSilenziosaB) && result.contains(astaSilenziosaC));
    }

    @Test
    @Transactional
    void testAstaSilenziosaCanBeUpdated() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreA();
        profiloVenditore = profiloRepository.save(profiloVenditore);
        Venditore venditore = profiloVenditore.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaC();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaSilenziosa astaSilenziosa = TestDataAstaSilenziosa.createAstaSilenziosaC(categoriaAsta, venditore);
        underTest.save(astaSilenziosa);

        // Modifica e salvataggio dell'oggetto nel database
        astaSilenziosa.setNome("UPDATED");
        underTest.save(astaSilenziosa);

        // Recupero l'oggetto dal database
        Optional<AstaSilenziosa> result = underTest.findById(astaSilenziosa.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(astaSilenziosa, result.get());
    }

    @Test
    @Transactional
    void testAstaSilenziosaCanBeDeleted() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreA();
        profiloVenditore = profiloRepository.save(profiloVenditore);
        Venditore venditore = profiloVenditore.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaB();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaSilenziosa astaSilenziosa = TestDataAstaSilenziosa.createAstaSilenziosaB(categoriaAsta, venditore);
        underTest.save(astaSilenziosa);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(astaSilenziosa.getIdAsta());

        // Recupero l'oggetto dal database
        Optional<AstaSilenziosa> result = underTest.findById(astaSilenziosa.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

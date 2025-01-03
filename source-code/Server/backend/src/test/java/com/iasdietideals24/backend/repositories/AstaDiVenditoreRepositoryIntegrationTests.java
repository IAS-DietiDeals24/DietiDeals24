package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.TestDataAstaSilenziosa;
import com.iasdietideals24.backend.datautil.TestDataAstaTempoFisso;
import com.iasdietideals24.backend.datautil.TestDataCategoriaAsta;
import com.iasdietideals24.backend.datautil.TestDataProfilo;
import com.iasdietideals24.backend.entities.AstaDiVenditore;
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
class AstaDiVenditoreRepositoryIntegrationTests {

    private final ProfiloRepository profiloRepository;
    private final CategoriaAstaRepository categoriaAstaRepository;

    private final AstaDiVenditoreRepository underTest;

    @Autowired
    public AstaDiVenditoreRepositoryIntegrationTests(AstaDiVenditoreRepository underTest, ProfiloRepository profiloRepository, CategoriaAstaRepository categoriaAstaRepository) {
        this.underTest = underTest;
        this.profiloRepository = profiloRepository;
        this.categoriaAstaRepository = categoriaAstaRepository;
    }

    @Test
    @Transactional
    void testAstaDiVenditoreCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetto
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreC();
        profiloVenditore = profiloRepository.save(profiloVenditore);
        Venditore venditore = profiloVenditore.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaM();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaDiVenditore astaDiVenditore = TestDataAstaSilenziosa.createAstaSilenziosaA(categoriaAsta, venditore);

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
    void testMultipleAstaDiVenditoreCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetti
        Profilo profiloVenditoreA = TestDataProfilo.createProfiloVenditoreA();
        profiloVenditoreA = profiloRepository.save(profiloVenditoreA);
        Venditore venditoreA = profiloVenditoreA.getVenditore();
        CategoriaAsta categoriaAstaA = TestDataCategoriaAsta.createCategoriaAstaM();
        categoriaAstaA = categoriaAstaRepository.save(categoriaAstaA);
        AstaDiVenditore astaDiVenditoreA = TestDataAstaTempoFisso.createAstaTempoFissoA(categoriaAstaA, venditoreA);

        Profilo profiloVenditoreB = TestDataProfilo.createProfiloVenditoreB();
        profiloVenditoreB = profiloRepository.save(profiloVenditoreB);
        Venditore venditoreB = profiloVenditoreB.getVenditore();
        CategoriaAsta categoriaAstaB = TestDataCategoriaAsta.createCategoriaAstaB();
        categoriaAstaB = categoriaAstaRepository.save(categoriaAstaB);
        AstaDiVenditore astaDiVenditoreB = TestDataAstaSilenziosa.createAstaSilenziosaB(categoriaAstaB, venditoreB);

        Profilo profiloVenditoreC = TestDataProfilo.createProfiloVenditoreC();
        profiloVenditoreC = profiloRepository.save(profiloVenditoreC);
        Venditore venditoreC = profiloVenditoreC.getVenditore();
        CategoriaAsta categoriaAstaC = TestDataCategoriaAsta.createCategoriaAstaC();
        categoriaAstaC = categoriaAstaRepository.save(categoriaAstaC);
        AstaDiVenditore astaDiVenditoreC = TestDataAstaTempoFisso.createAstaTempoFissoC(categoriaAstaC, venditoreC);

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
    void testAstaDiVenditoreCanBeUpdated() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreA();
        profiloVenditore = profiloRepository.save(profiloVenditore);
        Venditore venditore = profiloVenditore.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaC();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaDiVenditore astaDiVenditore = TestDataAstaSilenziosa.createAstaSilenziosaC(categoriaAsta, venditore);
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
    void testAstaDiVenditoreCanBeDeleted() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreA();
        profiloVenditore = profiloRepository.save(profiloVenditore);
        Venditore venditore = profiloVenditore.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaB();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaDiVenditore astaDiVenditore = TestDataAstaTempoFisso.createAstaTempoFissoB(categoriaAsta, venditore);
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

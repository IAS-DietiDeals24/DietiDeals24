package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.TestDataAstaTempoFisso;
import com.iasdietideals24.backend.datautil.TestDataCategoriaAsta;
import com.iasdietideals24.backend.datautil.TestDataProfilo;
import com.iasdietideals24.backend.entities.AstaTempoFisso;
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
class AstaTempoFissoRepositoryIntegrationTests {

    private final ProfiloRepository profiloRepository;
    private final CategoriaAstaRepository categoriaAstaRepository;

    private final AstaTempoFissoRepository underTest;

    @Autowired
    public AstaTempoFissoRepositoryIntegrationTests(AstaTempoFissoRepository underTest, ProfiloRepository profiloRepository, CategoriaAstaRepository categoriaAstaRepository) {
        this.underTest = underTest;
        this.profiloRepository = profiloRepository;
        this.categoriaAstaRepository = categoriaAstaRepository;
    }

    @Test
    @Transactional
    void testAstaTempoFissoCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetto
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreC();
        profiloVenditore = profiloRepository.save(profiloVenditore);
        Venditore venditore = profiloVenditore.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaB();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaTempoFisso astaTempoFisso = TestDataAstaTempoFisso.createAstaTempoFissoA(categoriaAsta, venditore);

        // Salvataggio oggetto nel database
        underTest.save(astaTempoFisso);

        // Recupero oggetto dal database
        Optional<AstaTempoFisso> result = underTest.findById(astaTempoFisso.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(astaTempoFisso, result.get());
    }

    @Test
    @Transactional
    void testMultipleAstaTempoFissoCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetti
        Profilo profiloVenditoreA = TestDataProfilo.createProfiloVenditoreA();
        profiloVenditoreA = profiloRepository.save(profiloVenditoreA);
        Venditore venditoreA = profiloVenditoreA.getVenditore();
        CategoriaAsta categoriaAstaA = TestDataCategoriaAsta.createCategoriaAstaB();
        categoriaAstaA = categoriaAstaRepository.save(categoriaAstaA);
        AstaTempoFisso astaTempoFissoA = TestDataAstaTempoFisso.createAstaTempoFissoA(categoriaAstaA, venditoreA);

        Profilo profiloVenditoreB = TestDataProfilo.createProfiloVenditoreB();
        profiloVenditoreB = profiloRepository.save(profiloVenditoreB);
        Venditore venditoreB = profiloVenditoreB.getVenditore();
        CategoriaAsta categoriaAstaB = TestDataCategoriaAsta.createCategoriaAstaE();
        categoriaAstaB = categoriaAstaRepository.save(categoriaAstaB);
        AstaTempoFisso astaTempoFissoB = TestDataAstaTempoFisso.createAstaTempoFissoB(categoriaAstaB, venditoreB);

        Profilo profiloVenditoreC = TestDataProfilo.createProfiloVenditoreC();
        profiloVenditoreC = profiloRepository.save(profiloVenditoreC);
        Venditore venditoreC = profiloVenditoreC.getVenditore();
        CategoriaAsta categoriaAstaC = TestDataCategoriaAsta.createCategoriaAstaK();
        categoriaAstaC = categoriaAstaRepository.save(categoriaAstaC);
        AstaTempoFisso astaTempoFissoC = TestDataAstaTempoFisso.createAstaTempoFissoC(categoriaAstaC, venditoreC);

        // Salvataggio oggetti nel database
        underTest.save(astaTempoFissoA);
        underTest.save(astaTempoFissoB);
        underTest.save(astaTempoFissoC);

        // Recupero oggetti dal database
        List<AstaTempoFisso> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(astaTempoFissoA) && result.contains(astaTempoFissoB) && result.contains(astaTempoFissoC));
    }

    @Test
    @Transactional
    void testAstaTempoFissoCanBeUpdated() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreA();
        profiloVenditore = profiloRepository.save(profiloVenditore);
        Venditore venditore = profiloVenditore.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaK();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaTempoFisso astaTempoFisso = TestDataAstaTempoFisso.createAstaTempoFissoC(categoriaAsta, venditore);
        underTest.save(astaTempoFisso);

        // Modifica e salvataggio dell'oggetto nel database
        astaTempoFisso.setNome("UPDATED");
        underTest.save(astaTempoFisso);

        // Recupero l'oggetto dal database
        Optional<AstaTempoFisso> result = underTest.findById(astaTempoFisso.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(astaTempoFisso, result.get());
    }

    @Test
    @Transactional
    void testAstaTempoFissoCanBeDeleted() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloVenditore = TestDataProfilo.createProfiloVenditoreA();
        profiloVenditore = profiloRepository.save(profiloVenditore);
        Venditore venditore = profiloVenditore.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaE();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaTempoFisso astaTempoFisso = TestDataAstaTempoFisso.createAstaTempoFissoB(categoriaAsta, venditore);
        underTest.save(astaTempoFisso);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(astaTempoFisso.getIdAsta());

        // Recupero l'oggetto dal database
        Optional<AstaTempoFisso> result = underTest.findById(astaTempoFisso.getIdAsta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

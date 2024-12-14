package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.TestDataAstaInversa;
import com.iasdietideals24.backend.datautil.TestDataCategoriaAsta;
import com.iasdietideals24.backend.datautil.TestDataProfilo;
import com.iasdietideals24.backend.entities.AstaInversa;
import com.iasdietideals24.backend.entities.CategoriaAsta;
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

    private final ProfiloRepository profiloRepository;
    private final CategoriaAstaRepository categoriaAstaRepository;

    private final AstaInversaRepository underTest;

    @Autowired
    public AstaInversaRepositoryIntegrationTests(AstaInversaRepository underTest, ProfiloRepository profiloRepository, CategoriaAstaRepository categoriaAstaRepository) {
        this.underTest = underTest;
        this.profiloRepository = profiloRepository;
        this.categoriaAstaRepository = categoriaAstaRepository;
    }

    @Test
    @Transactional
    void testAstaInversaCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetto
        Profilo profiloCompratore = TestDataProfilo.createProfiloCompratoreC();
        profiloCompratore = profiloRepository.save(profiloCompratore);
        Compratore compratore = profiloCompratore.getCompratore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaE();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaInversa astaInversa = TestDataAstaInversa.createAstaInversaA(categoriaAsta, compratore);

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
        profiloCompratoreA = profiloRepository.save(profiloCompratoreA);
        Compratore compratoreA = profiloCompratoreA.getCompratore();
        CategoriaAsta categoriaAstaA = TestDataCategoriaAsta.createCategoriaAstaE();
        categoriaAstaA = categoriaAstaRepository.save(categoriaAstaA);
        AstaInversa astaInversaA = TestDataAstaInversa.createAstaInversaA(categoriaAstaA, compratoreA);

        Profilo profiloCompratoreB = TestDataProfilo.createProfiloCompratoreB();
        profiloCompratoreB = profiloRepository.save(profiloCompratoreB);
        Compratore compratoreB = profiloCompratoreB.getCompratore();
        CategoriaAsta categoriaAstaB = TestDataCategoriaAsta.createCategoriaAstaK();
        categoriaAstaB = categoriaAstaRepository.save(categoriaAstaB);
        AstaInversa astaInversaB = TestDataAstaInversa.createAstaInversaB(categoriaAstaB, compratoreB);

        Profilo profiloCompratoreC = TestDataProfilo.createProfiloCompratoreC();
        profiloCompratoreC = profiloRepository.save(profiloCompratoreC);
        Compratore compratoreC = profiloCompratoreC.getCompratore();
        AstaInversa astaInversaC = TestDataAstaInversa.createAstaInversaC(categoriaAstaB, compratoreC);

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
        profiloCompratore = profiloRepository.save(profiloCompratore);
        Compratore compratore = profiloCompratore.getCompratore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaE();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaInversa astaInversa = TestDataAstaInversa.createAstaInversaA(categoriaAsta, compratore);
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
        profiloCompratore = profiloRepository.save(profiloCompratore);
        Compratore compratore = profiloCompratore.getCompratore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaE();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaInversa astaInversa = TestDataAstaInversa.createAstaInversaA(categoriaAsta, compratore);
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

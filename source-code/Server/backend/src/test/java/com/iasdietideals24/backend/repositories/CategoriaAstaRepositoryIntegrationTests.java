package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.TestDataCategoriaAsta;
import com.iasdietideals24.backend.entities.CategoriaAsta;
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
class CategoriaAstaRepositoryIntegrationTests {

    private final CategoriaAstaRepository underTest;

    @Autowired
    public CategoriaAstaRepositoryIntegrationTests(CategoriaAstaRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    @Transactional
    void testCategoriaAstaCanBeCreatedAndRecalled() {
        // Creazione oggetto
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaA();

        // Salvataggio oggetto nel database
        underTest.save(categoriaAsta);

        // Recupero oggetto dal database
        Optional<CategoriaAsta> result = underTest.findById(categoriaAsta.getNome());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(categoriaAsta, result.get());
    }

    @Test
    @Transactional
    void testMultipleCategoriaAstaCanBeCreatedAndRecalled() {
        // Creazione oggetti
        CategoriaAsta categoriaAstaA = TestDataCategoriaAsta.createCategoriaAstaA();
        CategoriaAsta categoriaAstaB = TestDataCategoriaAsta.createCategoriaAstaB();
        CategoriaAsta categoriaAstaC = TestDataCategoriaAsta.createCategoriaAstaC();

        // Salvataggio oggetti nel database
        underTest.save(categoriaAstaA);
        underTest.save(categoriaAstaB);
        underTest.save(categoriaAstaC);

        // Recupero oggetti dal database
        List<CategoriaAsta> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(categoriaAstaA) && result.contains(categoriaAstaB) && result.contains(categoriaAstaC));
    }

    @Test
    @Transactional
    void testCategoriaAstaCanBeUpdated() {
        // Creazione e salvataggio oggetto nel database
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaD();
        underTest.save(categoriaAsta);

        // Modifica e salvataggio dell'oggetto nel database
        categoriaAsta.setNome("UPDATED");
        underTest.save(categoriaAsta);

        // Recupero l'oggetto dal database
        Optional<CategoriaAsta> result = underTest.findById(categoriaAsta.getNome());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(categoriaAsta, result.get());
    }

    @Test
    @Transactional
    void testCategoriaAstaCanBeDeleted() {
        // Creazione e salvataggio oggetto nel database
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaE();
        underTest.save(categoriaAsta);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(categoriaAsta.getNome());

        // Recupero l'oggetto dal database
        Optional<CategoriaAsta> result = underTest.findById(categoriaAsta.getNome());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

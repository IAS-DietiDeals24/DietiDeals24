package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.TestDataAstaSilenziosa;
import com.iasdietideals24.backend.datautil.TestDataCategoriaAsta;
import com.iasdietideals24.backend.datautil.TestDataOffertaSilenziosa;
import com.iasdietideals24.backend.datautil.TestDataProfilo;
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

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OffertaSilenziosaRepositoryIntegrationTests {

    private final OffertaSilenziosaRepository underTest;

    @Autowired
    public OffertaSilenziosaRepositoryIntegrationTests(OffertaSilenziosaRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    @Transactional
    void testOffertaSilenziosaCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetto
        Profilo profiloProprietario = TestDataProfilo.createProfiloVenditoreA();
        Venditore proprietario = profiloProprietario.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaM();
        AstaSilenziosa astaRiferimento = TestDataAstaSilenziosa.createAstaSilenziosaA(categoriaAsta, proprietario);
        Profilo profiloCompratoreCollegato = TestDataProfilo.createProfiloCompratoreB();
        Compratore compratoreCollegato = profiloCompratoreCollegato.getCompratore();
        OffertaSilenziosa offertaSilenziosa = TestDataOffertaSilenziosa.createOffertaSilenziosaA(compratoreCollegato, astaRiferimento);

        // Salvataggio oggetto nel database
        underTest.save(offertaSilenziosa);

        // Recupero oggetto dal database
        Optional<OffertaSilenziosa> result = underTest.findById(offertaSilenziosa.getIdOfferta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(offertaSilenziosa, result.get());
    }

    @Test
    @Transactional
    void testMultipleOffertaSilenziosaCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetti
        Profilo profiloProprietarioA = TestDataProfilo.createProfiloVenditoreA();
        Venditore proprietarioA = profiloProprietarioA.getVenditore();
        CategoriaAsta categoriaAstaA = TestDataCategoriaAsta.createCategoriaAstaM();
        AstaSilenziosa astaRiferimentoA = TestDataAstaSilenziosa.createAstaSilenziosaA(categoriaAstaA, proprietarioA);
        Profilo profiloCompratoreCollegatoB = TestDataProfilo.createProfiloCompratoreB();
        Compratore compratoreCollegatoB = profiloCompratoreCollegatoB.getCompratore();
        OffertaSilenziosa offertaSilenziosaA = TestDataOffertaSilenziosa.createOffertaSilenziosaA(compratoreCollegatoB, astaRiferimentoA);

        Profilo profiloProprietarioC = TestDataProfilo.createProfiloVenditoreC();
        Venditore proprietarioC = profiloProprietarioC.getVenditore();
        CategoriaAsta categoriaAstaB = TestDataCategoriaAsta.createCategoriaAstaB();
        AstaSilenziosa astaRiferimentoB = TestDataAstaSilenziosa.createAstaSilenziosaB(categoriaAstaB, proprietarioC);
        OffertaSilenziosa offertaSilenziosaB = TestDataOffertaSilenziosa.createOffertaSilenziosaB(compratoreCollegatoB, astaRiferimentoB);

        CategoriaAsta categoriaAstaC = TestDataCategoriaAsta.createCategoriaAstaC();
        AstaSilenziosa astaRiferimentoC = TestDataAstaSilenziosa.createAstaSilenziosaC(categoriaAstaC, proprietarioC);
        OffertaSilenziosa offertaSilenziosaC = TestDataOffertaSilenziosa.createOffertaSilenziosaC(compratoreCollegatoB, astaRiferimentoC);

        // Salvataggio oggetti nel database
        underTest.save(offertaSilenziosaA);
        underTest.save(offertaSilenziosaB);
        underTest.save(offertaSilenziosaC);

        // Recupero oggetti dal database
        List<OffertaSilenziosa> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(offertaSilenziosaA) && result.contains(offertaSilenziosaB) && result.contains(offertaSilenziosaC));
    }

    @Test
    @Transactional
    void testOffertaSilenziosaCanBeUpdated() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloProprietario = TestDataProfilo.createProfiloVenditoreB();
        Venditore proprietario = profiloProprietario.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaB();
        AstaSilenziosa astaRiferimento = TestDataAstaSilenziosa.createAstaSilenziosaB(categoriaAsta, proprietario);
        Profilo profiloCompratoreCollegato = TestDataProfilo.createProfiloCompratoreA();
        Compratore compratoreCollegato = profiloCompratoreCollegato.getCompratore();
        OffertaSilenziosa offertaSilenziosa = TestDataOffertaSilenziosa.createOffertaSilenziosaC(compratoreCollegato, astaRiferimento);
        underTest.save(offertaSilenziosa);

        // Modifica e salvataggio dell'oggetto nel database
        offertaSilenziosa.setOraInvio(LocalTime.of(0, 0));
        underTest.save(offertaSilenziosa);

        // Recupero l'oggetto dal database
        Optional<OffertaSilenziosa> result = underTest.findById(offertaSilenziosa.getIdOfferta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(offertaSilenziosa, result.get());
    }

    @Test
    @Transactional
    void testOffertaSilenziosaCanBeDeleted() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloProprietario = TestDataProfilo.createProfiloVenditoreC();
        Venditore proprietario = profiloProprietario.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaC();
        AstaSilenziosa astaRiferimento = TestDataAstaSilenziosa.createAstaSilenziosaC(categoriaAsta, proprietario);
        Profilo profiloCompratoreCollegato = TestDataProfilo.createProfiloCompratoreA();
        Compratore compratoreCollegato = profiloCompratoreCollegato.getCompratore();
        OffertaSilenziosa offertaSilenziosa = TestDataOffertaSilenziosa.createOffertaSilenziosaA(compratoreCollegato, astaRiferimento);
        underTest.save(offertaSilenziosa);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(offertaSilenziosa.getIdOfferta());

        // Recupero l'oggetto dal database
        Optional<OffertaSilenziosa> result = underTest.findById(offertaSilenziosa.getIdOfferta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

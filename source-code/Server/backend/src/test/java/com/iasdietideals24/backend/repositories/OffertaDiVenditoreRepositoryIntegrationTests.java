package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.TestDataAstaInversa;
import com.iasdietideals24.backend.datautil.TestDataCategoriaAsta;
import com.iasdietideals24.backend.datautil.TestDataOffertaInversa;
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
class OffertaDiVenditoreRepositoryIntegrationTests {

    private final OffertaDiVenditoreRepository underTest;

    @Autowired
    public OffertaDiVenditoreRepositoryIntegrationTests(OffertaDiVenditoreRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    @Transactional
    void testOffertaDiVenditoreCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetto
        Profilo profiloProprietario = TestDataProfilo.createProfiloCompratoreA();
        Compratore proprietario = profiloProprietario.getCompratore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaE();
        AstaInversa astaRiferimento = TestDataAstaInversa.createAstaInversaA(categoriaAsta, proprietario);
        Profilo profiloVenditoreCollegato = TestDataProfilo.createProfiloVenditoreB();
        Venditore venditoreCollegato = profiloVenditoreCollegato.getVenditore();
        OffertaDiVenditore offertaDiVenditore = TestDataOffertaInversa.createOffertaInversaA(venditoreCollegato, astaRiferimento);

        // Salvataggio oggetto nel database
        underTest.save(offertaDiVenditore);

        // Recupero oggetto dal database
        Optional<OffertaDiVenditore> result = underTest.findById(offertaDiVenditore.getIdOfferta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(offertaDiVenditore, result.get());
    }

    @Test
    @Transactional
    void testMultipleOffertaDiVenditoreCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetti
        Profilo profiloProprietarioA = TestDataProfilo.createProfiloCompratoreA();
        Compratore proprietarioA = profiloProprietarioA.getCompratore();
        CategoriaAsta categoriaAstaA = TestDataCategoriaAsta.createCategoriaAstaE();
        AstaInversa astaRiferimentoA = TestDataAstaInversa.createAstaInversaA(categoriaAstaA, proprietarioA);
        Profilo profiloVenditoreCollegatoB = TestDataProfilo.createProfiloVenditoreB();
        Venditore venditoreCollegatoB = profiloVenditoreCollegatoB.getVenditore();
        OffertaDiVenditore offertaDiVenditoreA = TestDataOffertaInversa.createOffertaInversaA(venditoreCollegatoB, astaRiferimentoA);

        Profilo profiloProprietarioC = TestDataProfilo.createProfiloCompratoreC();
        Compratore proprietarioC = profiloProprietarioC.getCompratore();
        CategoriaAsta categoriaAstaB = TestDataCategoriaAsta.createCategoriaAstaK();
        AstaInversa astaRiferimentoB = TestDataAstaInversa.createAstaInversaB(categoriaAstaB, proprietarioC);
        OffertaDiVenditore offertaDiVenditoreB = TestDataOffertaInversa.createOffertaInversaA(venditoreCollegatoB, astaRiferimentoB);

        AstaInversa astaRiferimentoC = TestDataAstaInversa.createAstaInversaC(categoriaAstaB, proprietarioC);
        OffertaDiVenditore offertaDiVenditoreC = TestDataOffertaInversa.createOffertaInversaA(venditoreCollegatoB, astaRiferimentoC);

        // Salvataggio oggetti nel database
        underTest.save(offertaDiVenditoreA);
        underTest.save(offertaDiVenditoreB);
        underTest.save(offertaDiVenditoreC);

        // Recupero oggetti dal database
        List<OffertaDiVenditore> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(offertaDiVenditoreA) && result.contains(offertaDiVenditoreB) && result.contains(offertaDiVenditoreC));
    }

    @Test
    @Transactional
    void testOffertaDiVenditoreCanBeUpdated() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloProprietario = TestDataProfilo.createProfiloCompratoreB();
        Compratore proprietario = profiloProprietario.getCompratore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaK();
        AstaInversa astaRiferimento = TestDataAstaInversa.createAstaInversaB(categoriaAsta, proprietario);
        Profilo profiloVenditoreCollegato = TestDataProfilo.createProfiloVenditoreA();
        Venditore venditoreCollegato = profiloVenditoreCollegato.getVenditore();
        OffertaDiVenditore offertaDiVenditore = TestDataOffertaInversa.createOffertaInversaA(venditoreCollegato, astaRiferimento);
        underTest.save(offertaDiVenditore);

        // Modifica e salvataggio dell'oggetto nel database
        offertaDiVenditore.setOraInvio(LocalTime.of(0, 0));
        underTest.save(offertaDiVenditore);

        // Recupero l'oggetto dal database
        Optional<OffertaDiVenditore> result = underTest.findById(offertaDiVenditore.getIdOfferta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(offertaDiVenditore, result.get());
    }

    @Test
    @Transactional
    void testOffertaDiVenditoreCanBeDeleted() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloProprietario = TestDataProfilo.createProfiloCompratoreC();
        Compratore proprietario = profiloProprietario.getCompratore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaK();
        AstaInversa astaRiferimento = TestDataAstaInversa.createAstaInversaC(categoriaAsta, proprietario);
        Profilo profiloVenditoreCollegato = TestDataProfilo.createProfiloVenditoreA();
        Venditore venditoreCollegato = profiloVenditoreCollegato.getVenditore();
        OffertaDiVenditore offertaDiVenditore = TestDataOffertaInversa.createOffertaInversaA(venditoreCollegato, astaRiferimento);
        underTest.save(offertaDiVenditore);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(offertaDiVenditore.getIdOfferta());

        // Recupero l'oggetto dal database
        Optional<OffertaDiVenditore> result = underTest.findById(offertaDiVenditore.getIdOfferta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}
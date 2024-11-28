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
class OffertaRepositoryIntegrationTests {

    private final OffertaRepository underTest;

    @Autowired
    public OffertaRepositoryIntegrationTests(OffertaRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    @Transactional
    void testOffertaCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetto
        Profilo profiloProprietario = TestDataProfilo.createProfiloVenditoreA();
        Venditore proprietario = profiloProprietario.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaM();
        AstaSilenziosa astaRiferimento = TestDataAstaSilenziosa.createAstaSilenziosaA(categoriaAsta, proprietario);
        Profilo profiloCompratoreCollegato = TestDataProfilo.createProfiloCompratoreB();
        Compratore compratoreCollegato = profiloCompratoreCollegato.getCompratore();
        Offerta offerta = TestDataOffertaSilenziosa.createOffertaSilenziosaA(compratoreCollegato, astaRiferimento);

        // Salvataggio oggetto nel database
        underTest.save(offerta);

        // Recupero oggetto dal database
        Optional<Offerta> result = underTest.findById(offerta.getIdOfferta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(offerta, result.get());
    }

    @Test
    @Transactional
    void testMultipleOffertaCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetti
        Profilo profiloVenditoreA = TestDataProfilo.createProfiloVenditoreA();
        Venditore venditoreA = profiloVenditoreA.getVenditore();
        CategoriaAsta categoriaAstaA = TestDataCategoriaAsta.createCategoriaAstaA();
        AstaTempoFisso astaRiferimentoA = TestDataAstaTempoFisso.createAstaTempoFissoA(categoriaAstaA, venditoreA);
        Profilo profiloCompratoreCollegatoB = TestDataProfilo.createProfiloCompratoreB();
        Compratore compratoreCollegatoB = profiloCompratoreCollegatoB.getCompratore();
        Offerta offertaA = TestDataOffertaTempoFisso.createOffertaTempoFissoA(compratoreCollegatoB, astaRiferimentoA);

        Profilo profiloVenditoreC = TestDataProfilo.createProfiloVenditoreC();
        Venditore venditoreC = profiloVenditoreC.getVenditore();
        CategoriaAsta categoriaAstaB = TestDataCategoriaAsta.createCategoriaAstaB();
        AstaSilenziosa astaRiferimentoB = TestDataAstaSilenziosa.createAstaSilenziosaB(categoriaAstaB, venditoreC);
        Offerta offertaB = TestDataOffertaSilenziosa.createOffertaSilenziosaB(compratoreCollegatoB, astaRiferimentoB);

        CategoriaAsta categoriaAstaC = TestDataCategoriaAsta.createCategoriaAstaE();
        AstaInversa astaRiferimentoC = TestDataAstaInversa.createAstaInversaA(categoriaAstaC, compratoreCollegatoB);
        OffertaDiVenditore offertaC = TestDataOffertaInversa.createOffertaInversaA(venditoreA, astaRiferimentoC);

        // Salvataggio oggetti nel database
        underTest.save(offertaA);
        underTest.save(offertaB);
        underTest.save(offertaC);

        // Recupero oggetti dal database
        List<Offerta> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(offertaA) && result.contains(offertaB) && result.contains(offertaC));
    }

    @Test
    @Transactional
    void testOffertaCanBeUpdated() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloProprietario = TestDataProfilo.createProfiloCompratoreB();
        Compratore proprietario = profiloProprietario.getCompratore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaK();
        AstaInversa astaRiferimento = TestDataAstaInversa.createAstaInversaB(categoriaAsta, proprietario);
        Profilo profiloVenditoreCollegato = TestDataProfilo.createProfiloVenditoreA();
        Venditore venditoreCollegato = profiloVenditoreCollegato.getVenditore();
        OffertaDiVenditore offerta = TestDataOffertaInversa.createOffertaInversaA(venditoreCollegato, astaRiferimento);
        underTest.save(offerta);

        // Modifica e salvataggio dell'oggetto nel database
        offerta.setOraInvio(LocalTime.of(0, 0));
        underTest.save(offerta);

        // Recupero l'oggetto dal database
        Optional<Offerta> result = underTest.findById(offerta.getIdOfferta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(offerta, result.get());
    }

    @Test
    @Transactional
    void testOffertaCanBeDeleted() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloProprietario = TestDataProfilo.createProfiloVenditoreC();
        Venditore proprietario = profiloProprietario.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaC();
        AstaTempoFisso astaRiferimento = TestDataAstaTempoFisso.createAstaTempoFissoC(categoriaAsta, proprietario);
        Profilo profiloCompratoreCollegato = TestDataProfilo.createProfiloCompratoreA();
        Compratore compratoreCollegato = profiloCompratoreCollegato.getCompratore();
        Offerta offerta = TestDataOffertaTempoFisso.createOffertaTempoFissoB(compratoreCollegato, astaRiferimento);
        underTest.save(offerta);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(offerta.getIdOfferta());

        // Recupero l'oggetto dal database
        Optional<Offerta> result = underTest.findById(offerta.getIdOfferta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

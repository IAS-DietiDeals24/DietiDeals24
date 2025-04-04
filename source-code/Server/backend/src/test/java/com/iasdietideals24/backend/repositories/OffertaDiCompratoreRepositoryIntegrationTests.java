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
class OffertaDiCompratoreRepositoryIntegrationTests {

    private final ProfiloRepository profiloRepository;
    private final CategoriaAstaRepository categoriaAstaRepository;
    private final AstaRepository astaRepository;

    private final OffertaDiCompratoreRepository underTest;

    @Autowired
    public OffertaDiCompratoreRepositoryIntegrationTests(OffertaDiCompratoreRepository underTest, ProfiloRepository profiloRepository, CategoriaAstaRepository categoriaAstaRepository, AstaRepository astaRepository) {
        this.underTest = underTest;
        this.astaRepository = astaRepository;
        this.profiloRepository = profiloRepository;
        this.categoriaAstaRepository = categoriaAstaRepository;
    }

    @Test
    @Transactional
    void testOffertaDiCompratoreCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetto
        Profilo profiloProprietario = TestDataProfilo.createProfiloVenditoreA();
        profiloProprietario = profiloRepository.save(profiloProprietario);
        Venditore proprietario = profiloProprietario.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaM();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaSilenziosa astaRiferimento = TestDataAstaSilenziosa.createAstaSilenziosaA(categoriaAsta, proprietario);
        astaRiferimento = astaRepository.save(astaRiferimento);
        Profilo profiloCompratoreCollegato = TestDataProfilo.createProfiloCompratoreB();
        profiloCompratoreCollegato = profiloRepository.save(profiloCompratoreCollegato);
        Compratore compratoreCollegato = profiloCompratoreCollegato.getCompratore();
        OffertaDiCompratore offertaDiCompratore = TestDataOffertaSilenziosa.createOffertaSilenziosaA(compratoreCollegato, astaRiferimento);

        // Salvataggio oggetto nel database
        underTest.save(offertaDiCompratore);

        // Recupero oggetto dal database
        Optional<OffertaDiCompratore> result = underTest.findById(offertaDiCompratore.getIdOfferta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(offertaDiCompratore, result.get());
    }

    @Test
    @Transactional
    void testMultipleOffertaDiCompratoreCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetti
        Profilo profiloProprietarioA = TestDataProfilo.createProfiloVenditoreA();
        profiloProprietarioA = profiloRepository.save(profiloProprietarioA);
        Venditore venditoreA = profiloProprietarioA.getVenditore();
        CategoriaAsta categoriaAstaA = TestDataCategoriaAsta.createCategoriaAstaA();
        categoriaAstaA = categoriaAstaRepository.save(categoriaAstaA);
        AstaTempoFisso astaRiferimentoA = TestDataAstaTempoFisso.createAstaTempoFissoA(categoriaAstaA, venditoreA);
        astaRiferimentoA = astaRepository.save(astaRiferimentoA);
        Profilo profiloCompratoreCollegatoB = TestDataProfilo.createProfiloCompratoreB();
        profiloCompratoreCollegatoB = profiloRepository.save(profiloCompratoreCollegatoB);
        Compratore compratoreCollegatoB = profiloCompratoreCollegatoB.getCompratore();
        OffertaDiCompratore offertaDiCompratoreA = TestDataOffertaTempoFisso.createOffertaTempoFissoA(compratoreCollegatoB, astaRiferimentoA);

        Profilo profiloProprietarioC = TestDataProfilo.createProfiloVenditoreC();
        profiloProprietarioC = profiloRepository.save(profiloProprietarioC);
        Venditore proprietarioC = profiloProprietarioC.getVenditore();
        CategoriaAsta categoriaAstaB = TestDataCategoriaAsta.createCategoriaAstaB();
        categoriaAstaB = categoriaAstaRepository.save(categoriaAstaB);
        AstaSilenziosa astaRiferimentoB = TestDataAstaSilenziosa.createAstaSilenziosaB(categoriaAstaB, proprietarioC);
        astaRiferimentoB = astaRepository.save(astaRiferimentoB);
        OffertaDiCompratore offertaDiCompratoreB = TestDataOffertaSilenziosa.createOffertaSilenziosaB(compratoreCollegatoB, astaRiferimentoB);

        CategoriaAsta categoriaAstaC = TestDataCategoriaAsta.createCategoriaAstaC();
        categoriaAstaC = categoriaAstaRepository.save(categoriaAstaC);
        AstaTempoFisso astaRiferimentoC = TestDataAstaTempoFisso.createAstaTempoFissoC(categoriaAstaC, proprietarioC);
        astaRiferimentoC = astaRepository.save(astaRiferimentoC);
        OffertaDiCompratore offertaDiCompratoreC = TestDataOffertaTempoFisso.createOffertaTempoFissoC(compratoreCollegatoB, astaRiferimentoC);

        // Salvataggio oggetti nel database
        underTest.save(offertaDiCompratoreA);
        underTest.save(offertaDiCompratoreB);
        underTest.save(offertaDiCompratoreC);

        // Recupero oggetti dal database
        List<OffertaDiCompratore> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(offertaDiCompratoreA) && result.contains(offertaDiCompratoreB) && result.contains(offertaDiCompratoreC));
    }

    @Test
    @Transactional
    void testOffertaDiCompratoreCanBeUpdated() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloProprietario = TestDataProfilo.createProfiloVenditoreB();
        profiloProprietario = profiloRepository.save(profiloProprietario);
        Venditore proprietario = profiloProprietario.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaB();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaSilenziosa astaRiferimento = TestDataAstaSilenziosa.createAstaSilenziosaB(categoriaAsta, proprietario);
        astaRiferimento = astaRepository.save(astaRiferimento);
        Profilo profiloCompratoreCollegato = TestDataProfilo.createProfiloCompratoreA();
        profiloCompratoreCollegato = profiloRepository.save(profiloCompratoreCollegato);
        Compratore compratoreCollegato = profiloCompratoreCollegato.getCompratore();
        OffertaDiCompratore offertaDiCompratore = TestDataOffertaSilenziosa.createOffertaSilenziosaC(compratoreCollegato, astaRiferimento);
        underTest.save(offertaDiCompratore);

        // Modifica e salvataggio dell'oggetto nel database
        offertaDiCompratore.setOraInvio(LocalTime.of(0, 0));
        underTest.save(offertaDiCompratore);

        // Recupero l'oggetto dal database
        Optional<OffertaDiCompratore> result = underTest.findById(offertaDiCompratore.getIdOfferta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(offertaDiCompratore, result.get());
    }

    @Test
    @Transactional
    void testOffertaDiCompratoreCanBeDeleted() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloProprietario = TestDataProfilo.createProfiloVenditoreC();
        profiloProprietario = profiloRepository.save(profiloProprietario);
        Venditore proprietario = profiloProprietario.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaC();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaTempoFisso astaRiferimento = TestDataAstaTempoFisso.createAstaTempoFissoC(categoriaAsta, proprietario);
        astaRiferimento = astaRepository.save(astaRiferimento);
        Profilo profiloCompratoreCollegato = TestDataProfilo.createProfiloCompratoreA();
        profiloCompratoreCollegato = profiloRepository.save(profiloCompratoreCollegato);
        Compratore compratoreCollegato = profiloCompratoreCollegato.getCompratore();
        OffertaDiCompratore offertaDiCompratore = TestDataOffertaTempoFisso.createOffertaTempoFissoB(compratoreCollegato, astaRiferimento);
        underTest.save(offertaDiCompratore);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(offertaDiCompratore.getIdOfferta());

        // Recupero l'oggetto dal database
        Optional<OffertaDiCompratore> result = underTest.findById(offertaDiCompratore.getIdOfferta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

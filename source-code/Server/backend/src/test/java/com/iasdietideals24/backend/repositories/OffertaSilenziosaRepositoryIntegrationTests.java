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

    private final ProfiloRepository profiloRepository;
    private final CategoriaAstaRepository categoriaAstaRepository;
    private final AstaRepository astaRepository;
    private final OffertaSilenziosaRepository underTest;

    @Autowired
    public OffertaSilenziosaRepositoryIntegrationTests(OffertaSilenziosaRepository underTest, ProfiloRepository profiloRepository, CategoriaAstaRepository categoriaAstaRepository, AstaRepository astaRepository) {
        this.underTest = underTest;
        this.astaRepository = astaRepository;
        this.profiloRepository = profiloRepository;
        this.categoriaAstaRepository = categoriaAstaRepository;
    }

    @Test
    @Transactional
    void testOffertaSilenziosaCanBeCreatedAndRecalled() throws InvalidTypeException {
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
        profiloProprietarioA = profiloRepository.save(profiloProprietarioA);
        Venditore proprietarioA = profiloProprietarioA.getVenditore();
        CategoriaAsta categoriaAstaA = TestDataCategoriaAsta.createCategoriaAstaM();
        categoriaAstaA = categoriaAstaRepository.save(categoriaAstaA);
        AstaSilenziosa astaRiferimentoA = TestDataAstaSilenziosa.createAstaSilenziosaA(categoriaAstaA, proprietarioA);
        astaRiferimentoA = astaRepository.save(astaRiferimentoA);
        Profilo profiloCompratoreCollegatoB = TestDataProfilo.createProfiloCompratoreB();
        profiloCompratoreCollegatoB = profiloRepository.save(profiloCompratoreCollegatoB);
        Compratore compratoreCollegatoB = profiloCompratoreCollegatoB.getCompratore();
        OffertaSilenziosa offertaSilenziosaA = TestDataOffertaSilenziosa.createOffertaSilenziosaA(compratoreCollegatoB, astaRiferimentoA);

        Profilo profiloProprietarioC = TestDataProfilo.createProfiloVenditoreC();
        profiloProprietarioC = profiloRepository.save(profiloProprietarioC);
        Venditore proprietarioC = profiloProprietarioC.getVenditore();
        CategoriaAsta categoriaAstaB = TestDataCategoriaAsta.createCategoriaAstaB();
        categoriaAstaB = categoriaAstaRepository.save(categoriaAstaB);
        AstaSilenziosa astaRiferimentoB = TestDataAstaSilenziosa.createAstaSilenziosaB(categoriaAstaB, proprietarioC);
        astaRiferimentoB = astaRepository.save(astaRiferimentoB);
        OffertaSilenziosa offertaSilenziosaB = TestDataOffertaSilenziosa.createOffertaSilenziosaB(compratoreCollegatoB, astaRiferimentoB);

        CategoriaAsta categoriaAstaC = TestDataCategoriaAsta.createCategoriaAstaC();
        categoriaAstaC = categoriaAstaRepository.save(categoriaAstaC);
        AstaSilenziosa astaRiferimentoC = TestDataAstaSilenziosa.createAstaSilenziosaC(categoriaAstaC, proprietarioC);
        astaRiferimentoC = astaRepository.save(astaRiferimentoC);
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
        profiloProprietario = profiloRepository.save(profiloProprietario);
        Venditore proprietario = profiloProprietario.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaB();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaSilenziosa astaRiferimento = TestDataAstaSilenziosa.createAstaSilenziosaB(categoriaAsta, proprietario);
        astaRiferimento = astaRepository.save(astaRiferimento);
        Profilo profiloCompratoreCollegato = TestDataProfilo.createProfiloCompratoreA();
        profiloCompratoreCollegato = profiloRepository.save(profiloCompratoreCollegato);
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
        profiloProprietario = profiloRepository.save(profiloProprietario);
        Venditore proprietario = profiloProprietario.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaC();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaSilenziosa astaRiferimento = TestDataAstaSilenziosa.createAstaSilenziosaC(categoriaAsta, proprietario);
        astaRiferimento = astaRepository.save(astaRiferimento);
        Profilo profiloCompratoreCollegato = TestDataProfilo.createProfiloCompratoreA();
        profiloCompratoreCollegato = profiloRepository.save(profiloCompratoreCollegato);
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

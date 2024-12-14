package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.TestDataAstaTempoFisso;
import com.iasdietideals24.backend.datautil.TestDataCategoriaAsta;
import com.iasdietideals24.backend.datautil.TestDataOffertaTempoFisso;
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
class OffertaTempoFissoRepositoryIntegrationTests {

    private final ProfiloRepository profiloRepository;
    private final CategoriaAstaRepository categoriaAstaRepository;
    private final AstaRepository astaRepository;
    private final OffertaTempoFissoRepository underTest;

    @Autowired
    public OffertaTempoFissoRepositoryIntegrationTests(OffertaTempoFissoRepository underTest, ProfiloRepository profiloRepository, CategoriaAstaRepository categoriaAstaRepository, AstaRepository astaRepository) {
        this.underTest = underTest;
        this.astaRepository = astaRepository;
        this.profiloRepository = profiloRepository;
        this.categoriaAstaRepository = categoriaAstaRepository;
    }

    @Test
    @Transactional
    void testOffertaTempoFissoCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetto
        Profilo profiloProprietario = TestDataProfilo.createProfiloVenditoreA();
        profiloProprietario = profiloRepository.save(profiloProprietario);
        Venditore proprietario = profiloProprietario.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaB();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaTempoFisso astaRiferimento = TestDataAstaTempoFisso.createAstaTempoFissoA(categoriaAsta, proprietario);
        astaRiferimento = astaRepository.save(astaRiferimento);
        Profilo profiloCompratoreCollegato = TestDataProfilo.createProfiloCompratoreB();
        profiloCompratoreCollegato = profiloRepository.save(profiloCompratoreCollegato);
        Compratore compratoreCollegato = profiloCompratoreCollegato.getCompratore();
        OffertaTempoFisso offertaTempoFisso = TestDataOffertaTempoFisso.createOffertaTempoFissoA(compratoreCollegato, astaRiferimento);

        // Salvataggio oggetto nel database
        underTest.save(offertaTempoFisso);

        // Recupero oggetto dal database
        Optional<OffertaTempoFisso> result = underTest.findById(offertaTempoFisso.getIdOfferta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(offertaTempoFisso, result.get());
    }

    @Test
    @Transactional
    void testMultipleOffertaTempoFissoCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetti
        Profilo profiloProprietarioA = TestDataProfilo.createProfiloVenditoreA();
        profiloProprietarioA = profiloRepository.save(profiloProprietarioA);
        Venditore proprietarioA = profiloProprietarioA.getVenditore();
        CategoriaAsta categoriaAstaA = TestDataCategoriaAsta.createCategoriaAstaB();
        categoriaAstaA = categoriaAstaRepository.save(categoriaAstaA);
        AstaTempoFisso astaRiferimentoA = TestDataAstaTempoFisso.createAstaTempoFissoA(categoriaAstaA, proprietarioA);
        astaRiferimentoA = astaRepository.save(astaRiferimentoA);
        Profilo profiloCompratoreCollegatoB = TestDataProfilo.createProfiloCompratoreB();
        profiloCompratoreCollegatoB = profiloRepository.save(profiloCompratoreCollegatoB);
        Compratore compratoreCollegatoB = profiloCompratoreCollegatoB.getCompratore();
        OffertaTempoFisso offertaTempoFissoA = TestDataOffertaTempoFisso.createOffertaTempoFissoA(compratoreCollegatoB, astaRiferimentoA);

        Profilo profiloProprietarioC = TestDataProfilo.createProfiloVenditoreC();
        profiloProprietarioC = profiloRepository.save(profiloProprietarioC);
        Venditore proprietarioC = profiloProprietarioC.getVenditore();
        CategoriaAsta categoriaAstaB = TestDataCategoriaAsta.createCategoriaAstaE();
        categoriaAstaB = categoriaAstaRepository.save(categoriaAstaB);
        AstaTempoFisso astaRiferimentoB = TestDataAstaTempoFisso.createAstaTempoFissoB(categoriaAstaB, proprietarioC);
        astaRiferimentoB = astaRepository.save(astaRiferimentoB);
        OffertaTempoFisso offertaTempoFissoB = TestDataOffertaTempoFisso.createOffertaTempoFissoB(compratoreCollegatoB, astaRiferimentoB);

        CategoriaAsta categoriaAstaC = TestDataCategoriaAsta.createCategoriaAstaK();
        categoriaAstaC = categoriaAstaRepository.save(categoriaAstaC);
        AstaTempoFisso astaRiferimentoC = TestDataAstaTempoFisso.createAstaTempoFissoC(categoriaAstaC, proprietarioC);
        astaRiferimentoC = astaRepository.save(astaRiferimentoC);
        OffertaTempoFisso offertaTempoFissoC = TestDataOffertaTempoFisso.createOffertaTempoFissoC(compratoreCollegatoB, astaRiferimentoC);

        // Salvataggio oggetti nel database
        underTest.save(offertaTempoFissoA);
        underTest.save(offertaTempoFissoB);
        underTest.save(offertaTempoFissoC);

        // Recupero oggetti dal database
        List<OffertaTempoFisso> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(offertaTempoFissoA) && result.contains(offertaTempoFissoB) && result.contains(offertaTempoFissoC));
    }

    @Test
    @Transactional
    void testOffertaTempoFissoCanBeUpdated() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloProprietario = TestDataProfilo.createProfiloVenditoreB();
        profiloProprietario = profiloRepository.save(profiloProprietario);
        Venditore proprietario = profiloProprietario.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaE();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaTempoFisso astaRiferimento = TestDataAstaTempoFisso.createAstaTempoFissoB(categoriaAsta, proprietario);
        astaRiferimento = astaRepository.save(astaRiferimento);
        Profilo profiloCompratoreCollegato = TestDataProfilo.createProfiloCompratoreA();
        profiloCompratoreCollegato = profiloRepository.save(profiloCompratoreCollegato);
        Compratore compratoreCollegato = profiloCompratoreCollegato.getCompratore();
        OffertaTempoFisso offertaTempoFisso = TestDataOffertaTempoFisso.createOffertaTempoFissoC(compratoreCollegato, astaRiferimento);
        underTest.save(offertaTempoFisso);

        // Modifica e salvataggio dell'oggetto nel database
        offertaTempoFisso.setOraInvio(LocalTime.of(0, 0));
        underTest.save(offertaTempoFisso);

        // Recupero l'oggetto dal database
        Optional<OffertaTempoFisso> result = underTest.findById(offertaTempoFisso.getIdOfferta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(offertaTempoFisso, result.get());
    }

    @Test
    @Transactional
    void testOffertaTempoFissoCanBeDeleted() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloProprietario = TestDataProfilo.createProfiloVenditoreC();
        profiloProprietario = profiloRepository.save(profiloProprietario);
        Venditore proprietario = profiloProprietario.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaK();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaTempoFisso astaRiferimento = TestDataAstaTempoFisso.createAstaTempoFissoC(categoriaAsta, proprietario);
        astaRiferimento = astaRepository.save(astaRiferimento);
        Profilo profiloCompratoreCollegato = TestDataProfilo.createProfiloCompratoreA();
        profiloCompratoreCollegato = profiloRepository.save(profiloCompratoreCollegato);
        Compratore compratoreCollegato = profiloCompratoreCollegato.getCompratore();
        OffertaTempoFisso offertaTempoFisso = TestDataOffertaTempoFisso.createOffertaTempoFissoA(compratoreCollegato, astaRiferimento);
        underTest.save(offertaTempoFisso);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(offertaTempoFisso.getIdOfferta());

        // Recupero l'oggetto dal database
        Optional<OffertaTempoFisso> result = underTest.findById(offertaTempoFisso.getIdOfferta());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

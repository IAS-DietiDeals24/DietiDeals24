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

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class NotificaRepositoryIntegrationTests {

    private final ProfiloRepository profiloRepository;
    private final CategoriaAstaRepository categoriaAstaRepository;
    private final AstaRepository astaRepository;

    private final NotificaRepository underTest;

    @Autowired
    public NotificaRepositoryIntegrationTests(NotificaRepository underTest, ProfiloRepository profiloRepository, CategoriaAstaRepository categoriaAstaRepository, AstaRepository astaRepository) {
        this.underTest = underTest;
        this.astaRepository = astaRepository;
        this.profiloRepository = profiloRepository;
        this.categoriaAstaRepository = categoriaAstaRepository;
    }

    @Test
    @Transactional
    void testNotificaCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetto
        Profilo profiloMittente = TestDataProfilo.createProfiloCompratoreA();
        profiloMittente = profiloRepository.save(profiloMittente);
        Compratore mittente = profiloMittente.getCompratore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaE();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaDiCompratore astaAssociata = TestDataAstaInversa.createAstaInversaA(categoriaAsta, mittente);
        astaAssociata = astaRepository.save(astaAssociata);
        Profilo profiloDestinatario = TestDataProfilo.createProfiloVenditoreB();
        profiloDestinatario = profiloRepository.save(profiloDestinatario);
        Venditore destinatario = profiloDestinatario.getVenditore();
        Notifica notifica = TestDataNotifica.createNotificaA(mittente, destinatario, astaAssociata);

        // Salvataggio oggetto nel database
        underTest.save(notifica);

        // Recupero oggetto dal database
        Optional<Notifica> result = underTest.findById(notifica.getIdNotifica());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(notifica, result.get());
    }

    @Test
    @Transactional
    void testMultipleNotificaCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetti
        Profilo profiloMittenteA = TestDataProfilo.createProfiloCompratoreA();
        profiloMittenteA = profiloRepository.save(profiloMittenteA);
        Compratore mittenteA = profiloMittenteA.getCompratore();
        CategoriaAsta categoriaAstaA = TestDataCategoriaAsta.createCategoriaAstaE();
        categoriaAstaA = categoriaAstaRepository.save(categoriaAstaA);
        AstaDiCompratore astaAssociataA = TestDataAstaInversa.createAstaInversaA(categoriaAstaA, mittenteA);
        astaAssociataA = astaRepository.save(astaAssociataA);
        Profilo profiloDestinatarioB = TestDataProfilo.createProfiloVenditoreB();
        profiloDestinatarioB = profiloRepository.save(profiloDestinatarioB);
        Venditore destinatarioB = profiloDestinatarioB.getVenditore();
        Notifica notificaA = TestDataNotifica.createNotificaA(mittenteA, destinatarioB, astaAssociataA);

        Venditore mittenteB = destinatarioB;
        CategoriaAsta categoriaAstaB = TestDataCategoriaAsta.createCategoriaAstaB();
        categoriaAstaB = categoriaAstaRepository.save(categoriaAstaB);
        AstaDiVenditore astaAssociataB = TestDataAstaSilenziosa.createAstaSilenziosaB(categoriaAstaB, mittenteB);
        astaAssociataB = astaRepository.save(astaAssociataB);
        Compratore destinatarioA = mittenteA;
        Notifica notificaB = TestDataNotifica.createNotificaA(mittenteB, destinatarioA, astaAssociataB);

        Profilo profiloMittenteC = TestDataProfilo.createProfiloVenditoreC();
        profiloMittenteC = profiloRepository.save(profiloMittenteC);
        Venditore mittenteC = profiloMittenteC.getVenditore();
        CategoriaAsta categoriaAstaC = TestDataCategoriaAsta.createCategoriaAstaK();
        categoriaAstaC = categoriaAstaRepository.save(categoriaAstaC);
        AstaDiVenditore astaAssociataC = TestDataAstaTempoFisso.createAstaTempoFissoC(categoriaAstaC, mittenteC);
        astaAssociataC = astaRepository.save(astaAssociataC);
        Compratore destinatarioC = mittenteA;
        Notifica notificaC = TestDataNotifica.createNotificaA(mittenteC, destinatarioC, astaAssociataC);

        // Salvataggio oggetti nel database
        underTest.save(notificaA);
        underTest.save(notificaB);
        underTest.save(notificaC);

        // Recupero oggetti dal database
        List<Notifica> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(notificaA) && result.contains(notificaB) && result.contains(notificaC));
    }

    @Test
    @Transactional
    void testNotificaCanBeUpdated() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloMittente = TestDataProfilo.createProfiloCompratoreC();
        profiloMittente = profiloRepository.save(profiloMittente);
        Compratore mittente = profiloMittente.getCompratore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaE();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaDiCompratore astaAssociata = TestDataAstaInversa.createAstaInversaA(categoriaAsta, mittente);
        astaAssociata = astaRepository.save(astaAssociata);
        Profilo profiloDestinatario = TestDataProfilo.createProfiloVenditoreA();
        profiloDestinatario = profiloRepository.save(profiloDestinatario);
        Venditore destinatario = profiloDestinatario.getVenditore();
        Notifica notifica = TestDataNotifica.createNotificaA(mittente, destinatario, astaAssociata);
        underTest.save(notifica);

        // Modifica e salvataggio dell'oggetto nel database
        notifica.setMessaggio("UPDATED");
        underTest.save(notifica);

        // Recupero l'oggetto dal database
        Optional<Notifica> result = underTest.findById(notifica.getIdNotifica());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(notifica, result.get());
    }

    @Test
    @Transactional
    void testNotificaCanBeDeleted() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloMittente = TestDataProfilo.createProfiloVenditoreA();
        profiloMittente = profiloRepository.save(profiloMittente);
        Venditore mittente = profiloMittente.getVenditore();
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaB();
        categoriaAsta = categoriaAstaRepository.save(categoriaAsta);
        AstaDiVenditore astaAssociata = TestDataAstaTempoFisso.createAstaTempoFissoA(categoriaAsta, mittente);
        astaAssociata = astaRepository.save(astaAssociata);
        Profilo profiloDestinatario1 = TestDataProfilo.createProfiloCompratoreB();
        profiloDestinatario1 = profiloRepository.save(profiloDestinatario1);
        Compratore destinatario1 = profiloDestinatario1.getCompratore();
        Profilo profiloDestinatario2 = TestDataProfilo.createProfiloCompratoreC();
        profiloDestinatario2 = profiloRepository.save(profiloDestinatario2);
        Compratore destinatario2 = profiloDestinatario2.getCompratore();

        Notifica notifica = TestDataNotifica.createNotificaA(mittente, destinatario1, astaAssociata);
        notifica.addDestinatario(destinatario2);
        underTest.save(notifica);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(notifica.getIdNotifica());

        // Recupero l'oggetto dal database
        Optional<Notifica> result = underTest.findById(notifica.getIdNotifica());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

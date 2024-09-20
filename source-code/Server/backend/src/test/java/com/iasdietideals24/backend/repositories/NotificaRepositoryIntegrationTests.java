package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.*;
import com.iasdietideals24.backend.entities.*;
import com.iasdietideals24.backend.exceptions.InvalidParameterException;
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

    private final NotificaRepository underTest;

    @Autowired
    public NotificaRepositoryIntegrationTests(NotificaRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    @Transactional
    void testNotificaCanBeCreatedAndRecalled() throws InvalidParameterException {
        // Creazione oggetto
        Profilo profiloMittente = TestDataProfilo.createProfiloCompratoreA();
        Compratore mittente = profiloMittente.getCompratore();
        AstaDiCompratore astaAssociata = TestDataAstaInversa.createAstaInversaA(mittente);
        Profilo profiloDestinatario = TestDataProfilo.createProfiloVenditoreB();
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
    void testMultipleNotificaCanBeCreatedAndRecalled() throws InvalidParameterException {
        // Creazione oggetti
        Profilo profiloMittenteA = TestDataProfilo.createProfiloCompratoreA();
        Compratore mittenteA = profiloMittenteA.getCompratore();
        AstaDiCompratore astaAssociataA = TestDataAstaInversa.createAstaInversaA(mittenteA);
        Profilo profiloDestinatarioB = TestDataProfilo.createProfiloVenditoreB();
        Venditore destinatarioB = profiloDestinatarioB.getVenditore();
        Notifica notificaA = TestDataNotifica.createNotificaA(mittenteA, destinatarioB, astaAssociataA);

        Venditore mittenteB = destinatarioB;
        AstaDiVenditore astaAssociataB = TestDataAstaSilenziosa.createAstaSilenziosaB(mittenteB);
        Compratore destinatarioA = mittenteA;
        Notifica notificaB = TestDataNotifica.createNotificaA(mittenteB, destinatarioA, astaAssociataB);

        Profilo profiloMittenteC = TestDataProfilo.createProfiloVenditoreC();
        Venditore mittenteC = profiloMittenteC.getVenditore();
        AstaDiVenditore astaAssociataC = TestDataAstaTempoFisso.createAstaTempoFissoC(mittenteC);
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
    void testNotificaCanBeUpdated() throws InvalidParameterException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloMittente = TestDataProfilo.createProfiloCompratoreC();
        Compratore mittente = profiloMittente.getCompratore();
        AstaDiCompratore astaAssociata = TestDataAstaInversa.createAstaInversaA(mittente);
        Profilo profiloDestinatario = TestDataProfilo.createProfiloVenditoreA();
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
    void testNotificaCanBeDeleted() throws InvalidParameterException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloMittente = TestDataProfilo.createProfiloVenditoreA();
        Venditore mittente = profiloMittente.getVenditore();
        AstaDiVenditore astaAssociata = TestDataAstaTempoFisso.createAstaTempoFissoA(mittente);
        Profilo profiloDestinatario1 = TestDataProfilo.createProfiloCompratoreB();
        Compratore destinatario1 = profiloDestinatario1.getCompratore();
        Profilo profiloDestinatario2 = TestDataProfilo.createProfiloCompratoreC();
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

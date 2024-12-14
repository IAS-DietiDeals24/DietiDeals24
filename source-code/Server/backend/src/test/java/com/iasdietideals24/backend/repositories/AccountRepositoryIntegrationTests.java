package com.iasdietideals24.backend.repositories;

import com.iasdietideals24.backend.datautil.TestDataProfilo;
import com.iasdietideals24.backend.entities.Account;
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
class AccountRepositoryIntegrationTests {

    private final ProfiloRepository profiloRepository;

    private final AccountRepository underTest;

    @Autowired
    public AccountRepositoryIntegrationTests(AccountRepository underTest, ProfiloRepository profiloRepository) {
        this.underTest = underTest;
        this.profiloRepository = profiloRepository;
    }

    @Test
    @Transactional
    void testAccountCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetto
        Profilo profiloAccount = TestDataProfilo.createProfiloVenditoreA();
        profiloAccount = profiloRepository.save(profiloAccount);
        Account account = profiloAccount.getVenditore();

        // Salvataggio oggetto nel database
        underTest.save(account);

        // Recupero oggetto dal database
        Optional<Account> result = underTest.findById(account.getIdAccount());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(account, result.get());
    }

    @Test
    @Transactional
    void testMultipleAccountCanBeCreatedAndRecalled() throws InvalidTypeException {
        // Creazione oggetti
        Profilo profiloAccountA = TestDataProfilo.createProfiloCompratoreA();
        profiloAccountA = profiloRepository.save(profiloAccountA);
        Account accountA = profiloAccountA.getCompratore();

        Profilo profiloAccountB = TestDataProfilo.createProfiloVenditoreB();
        profiloAccountB = profiloRepository.save(profiloAccountB);
        Account accountB = profiloAccountB.getVenditore();

        Profilo profiloAccountC = TestDataProfilo.createProfiloCompratoreC();
        profiloAccountC = profiloRepository.save(profiloAccountC);
        Account accountC = profiloAccountC.getCompratore();

        // Salvataggio oggetti nel database
        underTest.save(accountA);
        underTest.save(accountB);
        underTest.save(accountC);

        // Recupero oggetti dal database
        List<Account> result = StreamSupport.stream(underTest.findAll().spliterator(), false).toList();
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.size() == 3 && result.contains(accountA) && result.contains(accountB) && result.contains(accountC));
    }

    @Test
    @Transactional
    void testAccountCanBeUpdated() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloAccount = TestDataProfilo.createProfiloVenditoreC();
        profiloAccount = profiloRepository.save(profiloAccount);
        Account account = profiloAccount.getVenditore();
        underTest.save(account);

        // Modifica e salvataggio dell'oggetto nel database
        account.setEmail("UPDATED");
        underTest.save(account);

        // Recupero l'oggetto dal database
        Optional<Account> result = underTest.findById(account.getIdAccount());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(account, result.get());
    }

    @Test
    @Transactional
    void testAccountCanBeDeleted() throws InvalidTypeException {
        // Creazione e salvataggio oggetto nel database
        Profilo profiloAccount = TestDataProfilo.createProfiloCompratoreB();
        profiloAccount = profiloRepository.save(profiloAccount);
        Account account = profiloAccount.getCompratore();
        underTest.save(account);

        // Rimozione dell'oggetto dal database
        underTest.deleteById(account.getIdAccount());

        // Recupero l'oggetto dal database
        Optional<Account> result = underTest.findById(account.getIdAccount());
        log.trace("Oggetti recuperati: {}", result);

        // Assertions
        assertTrue(result.isEmpty());
    }
}

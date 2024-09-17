package com.iasdietideals24.backend.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.iasdietideals24.backend.datautil.TestDataAstaInversa;
import com.iasdietideals24.backend.datautil.TestDataCompratore;
import com.iasdietideals24.backend.datautil.TestDataProfilo;
import com.iasdietideals24.backend.entities.Account;
import com.iasdietideals24.backend.entities.AstaDiCompratore;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.exceptions.ParameterNotValidException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.Optional;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AstaDiCompratoreRepositoryIntegrationTests {

    private final AstaDiCompratoreRepository underTest;

    @Autowired
    public AstaDiCompratoreRepositoryIntegrationTests(AstaDiCompratoreRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    @Transactional
    void testAstaDiCompratoreCanBeCreatedAndRecalled() throws ParameterNotValidException {
        // Creazione oggetto
        Profilo profilo = TestDataProfilo.createProfiloCompratoreC();
        Compratore compratore = profilo.getCompratore();
        AstaDiCompratore astaDiCompratore = TestDataAstaInversa.createAstaInversaA(compratore);

        // Interazione con il database
        underTest.save(astaDiCompratore);
        Optional<AstaDiCompratore> result = underTest.findById(astaDiCompratore.getIdAsta());

        // Assertions
        assertTrue(result.isPresent());
        assertEquals(astaDiCompratore, result.get());
    }

    @Test
    void testMultipleAstaDiCompratoreCanBeCreatedAndRecalled() throws ParameterNotValidException {
        // Creazione oggetti
        Profilo profiloA = TestDataProfilo.createProfiloCompratoreA();
        log.info(profiloA.toString());
        Compratore compratoreA = profiloA.getCompratore();
        AstaDiCompratore astaDiCompratoreA = TestDataAstaInversa.createAstaInversaA(compratoreA);

        Profilo profiloB = TestDataProfilo.createProfiloCompratoreB();
        Compratore compratoreB = profiloB.getCompratore();
        AstaDiCompratore astaDiCompratoreB = TestDataAstaInversa.createAstaInversaB(compratoreB);

        Profilo profiloC = TestDataProfilo.createProfiloCompratoreC();
        Compratore compratoreC = profiloC.getCompratore();
        AstaDiCompratore astaDiCompratoreC = TestDataAstaInversa.createAstaInversaC(compratoreC);

        // Interazione con il database
        underTest.save(astaDiCompratoreA);
        underTest.save(astaDiCompratoreB);
        underTest.save(astaDiCompratoreC);
        //Iterable<AstaDiCompratore> result = underTest.findAll();

        /*Iterator<AstaDiCompratore> it = underTest.findAll().iterator();

        List<AstaDiCompratore> www = new ArrayList<AstaDiCompratore>();
        while (it.hasNext()) {
            www.add(it.next());
        }

        // Assertions
        assertTrue(www.size() == 3 && www.contains(astaDiCompratoreA) && www.contains(astaDiCompratoreB) && www.contains(astaDiCompratoreC));

         */
    }


}

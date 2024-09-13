package com.iasdietideals24.backend.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.iasdietideals24.backend.datautil.TestDataAstaInversa;
import com.iasdietideals24.backend.datautil.TestDataCompratore;
import com.iasdietideals24.backend.datautil.TestDataProfilo;
import com.iasdietideals24.backend.entities.AstaDiCompratore;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.exceptions.ParameterNotValidException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AstaDiCompratoreRepositoryIntegrationTests {

    private AstaDiCompratoreRepository underTest;

    @Autowired
    public AstaDiCompratoreRepositoryIntegrationTests(AstaDiCompratoreRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    public void testAstaDiCompratoreCanBeCreatedAndRecalled() throws ParameterNotValidException {
        Profilo profilo = TestDataProfilo.createProfiloCompratoreC();
        Compratore compratore = TestDataCompratore.createCompratoreA(profilo);
        AstaDiCompratore astaDiCompratore = TestDataAstaInversa.createAstaInversaA(compratore);

        AstaDiCompratore saved = underTest.save(astaDiCompratore);
        Optional<AstaDiCompratore> result = underTest.findById(astaDiCompratore.getIdAsta());

        assertTrue(result.isPresent());
        assertEquals(result.get(), astaDiCompratore);
    }
}

package com.iasdietideals24.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.iasdietideals24.backend.entities.*;
import org.junit.jupiter.api.Test;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iasdietideals24.backend.datautil.TestDataAstaInversa;
import com.iasdietideals24.backend.datautil.TestDataProfilo;
import com.iasdietideals24.backend.exceptions.ParameterNotValidException;

public class JacksonTests {
    
    @Test
    public void testObjectMapperCreateJsonFromJavaObject() throws JsonProcessingException, ParameterNotValidException {

        // Arrange
        Profilo profilo = TestDataProfilo.createProfiloCompratoreA(); // Creiamo l'account per l'asta 
        
        Iterator<Account> itr = profilo.getAccounts().iterator(); // Cerchiamo l'account di tipo compratore
        Compratore proprietario = null;
        while ((itr.hasNext()) && (proprietario == null)) {
            Account account = itr.next();
            if (account instanceof Compratore)
                proprietario = (Compratore) account;
        }

        AstaInversa asta = TestDataAstaInversa.createAstaInversaA(proprietario); // Associamo l'account all'asta
        
        // CONVERTIRE ASTA IN DTO PRIMA DI SERIALIZZARE

        ObjectMapper objectMapper = new ObjectMapper(); // Convertiamo il dto in json
        String result = objectMapper.writeValueAsString(asta);


        // Act
        String oracolo = "";


        // Assert
        assertEquals(oracolo, result);
    }
}
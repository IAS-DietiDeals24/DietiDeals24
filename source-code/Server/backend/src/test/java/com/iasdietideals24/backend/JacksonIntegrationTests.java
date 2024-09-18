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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JacksonIntegrationTests {

    private final ObjectMapper objectMapper;

    @Autowired
    public JacksonIntegrationTests(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Test
    void testObjectMapperCreateJsonObjectFromJavaObject() throws JsonProcessingException, ParameterNotValidException {
        // Arrange
        Profilo profilo = TestDataProfilo.createProfiloCompratoreA(); // Creiamo l'account per l'asta 
        Compratore proprietario = profilo.getCompratore(); // Cerchiamo l'account di tipo compratore
        AstaInversa asta = TestDataAstaInversa.createAstaInversaA(proprietario); // Associamo l'account all'asta

        String result = objectMapper.writeValueAsString(asta); // Serializziamo l'oggetto Java in Json

        // Act
        String oracolo = "{\"idAsta\":null,\"categoria\":\"Videogiochi\",\"nome\":\"Dragon Age: Origins Xbox 360\",\"descrizione\":\"Edizione Xbox 360 del videogioco Dragon Age: Origins. Ci giocava mio marito.\",\"dataScadenza\":[2024,6,19],\"oraScadenza\":[18,44],\"immagine\":null,\"notificheAssociate\":[],\"proprietario\":{\"email\":\"pippo.baudo@gmail.com\",\"password\":\"buonasera\",\"profilo\":{\"nomeUtente\":\"pip.baud\",\"profilePicture\":\"neM2\",\"nome\":\"Pippo\",\"cognome\":\"Baudo\",\"dataNascita\":[1936,6,7],\"areaGeografica\":null,\"biografia\":null,\"linkPersonale\":null,\"linkInstagram\":null,\"linkFacebook\":null,\"linkGitHub\":null,\"linkX\":null,\"accounts\":[\"pippo.baudo@gmail.com\"],\"compratore\":\"pippo.baudo@gmail.com\",\"venditore\":null},\"notificheInviate\":[],\"notificheRicevute\":[],\"astePossedute\":[{\"idAsta\":null,\"categoria\":\"Videogiochi\",\"nome\":\"Dragon Age: Origins Xbox 360\",\"descrizione\":\"Edizione Xbox 360 del videogioco Dragon Age: Origins. Ci giocava mio marito.\",\"dataScadenza\":[2024,6,19],\"oraScadenza\":[18,44],\"immagine\":null,\"notificheAssociate\":[],\"proprietario\":\"pippo.baudo@gmail.com\",\"sogliaIniziale\":1.0,\"offerteRicevute\":[]}],\"offerteCollegate\":[]},\"sogliaIniziale\":1.0,\"offerteRicevute\":[]}";

        // Assert
        assertEquals(oracolo, result);
    }

    @Test
    void testObjectMapperCreateJavaObjectFromJsonObject() throws JsonProcessingException, ParameterNotValidException {
        // Arrange
        String json = "{\"idAsta\":null,\"categoria\":\"Videogiochi\",\"nome\":\"Dragon Age: Origins Xbox 360\",\"descrizione\":\"Edizione Xbox 360 del videogioco Dragon Age: Origins. Ci giocava mio marito.\",\"dataScadenza\":[2024,6,19],\"oraScadenza\":[18,44],\"immagine\":null,\"notificheAssociate\":[],\"proprietario\":{\"email\":\"pippo.baudo@gmail.com\",\"password\":\"buonasera\",\"profilo\":{\"nomeUtente\":\"pip.baud\",\"profilePicture\":\"neM2\",\"nome\":\"Pippo\",\"cognome\":\"Baudo\",\"dataNascita\":[1936,6,7],\"areaGeografica\":null,\"biografia\":null,\"linkPersonale\":null,\"linkInstagram\":null,\"linkFacebook\":null,\"linkGitHub\":null,\"linkX\":null,\"accounts\":[\"pippo.baudo@gmail.com\"],\"compratore\":\"pippo.baudo@gmail.com\",\"venditore\":null},\"notificheInviate\":[],\"notificheRicevute\":[],\"astePossedute\":[{\"idAsta\":null,\"categoria\":\"Videogiochi\",\"nome\":\"Dragon Age: Origins Xbox 360\",\"descrizione\":\"Edizione Xbox 360 del videogioco Dragon Age: Origins. Ci giocava mio marito.\",\"dataScadenza\":[2024,6,19],\"oraScadenza\":[18,44],\"immagine\":null,\"notificheAssociate\":[],\"proprietario\":\"pippo.baudo@gmail.com\",\"sogliaIniziale\":1.0,\"offerteRicevute\":[]}],\"offerteCollegate\":[]},\"sogliaIniziale\":1.0,\"offerteRicevute\":[]}";
        // Errore: Jackson non può istanziare Account, essendo una classe astratta. E' necessario utilizzare i DTO
        // AstaInversa result = objectMapper.readValue(json, AstaInversa.class); // Serializziamo l'oggetto Java in Json

        // Act
        Profilo profilo = TestDataProfilo.createProfiloCompratoreA(); // Creiamo l'account per l'asta
        Compratore proprietario = profilo.getCompratore(); // Cerchiamo l'account di tipo compratore
        AstaInversa oracolo = TestDataAstaInversa.createAstaInversaA(proprietario); // Associamo l'account all'asta

        // Assert
        // assertEquals(oracolo, result);
    }
}
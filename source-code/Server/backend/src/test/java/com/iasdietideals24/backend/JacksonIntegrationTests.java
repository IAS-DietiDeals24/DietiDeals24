package com.iasdietideals24.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iasdietideals24.backend.datautil.TestDataAstaInversa;
import com.iasdietideals24.backend.datautil.TestDataCategoriaAsta;
import com.iasdietideals24.backend.datautil.TestDataProfilo;
import com.iasdietideals24.backend.entities.AstaInversa;
import com.iasdietideals24.backend.entities.CategoriaAsta;
import com.iasdietideals24.backend.entities.Compratore;
import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.AstaInversaDto;
import com.iasdietideals24.backend.mapstruct.mappers.AstaInversaMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class JacksonIntegrationTests {

    private final ObjectMapper objectMapper;
    private final AstaInversaMapper astaInversaMapper;

    @Autowired
    public JacksonIntegrationTests(ObjectMapper objectMapper, AstaInversaMapper astaInversaMapper) {
        this.objectMapper = objectMapper;
        this.astaInversaMapper = astaInversaMapper;
    }

    @Test
    void testObjectMapperCreateJsonObjectFromJavaObject() throws JsonProcessingException, InvalidTypeException {
        // Arrange
        Profilo profilo = TestDataProfilo.createProfiloCompratoreA(); // Creiamo l'account per l'asta
        Compratore proprietario = profilo.getCompratore(); // Cerchiamo l'account di tipo compratore
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaE(); // Creiamo la categoria per l'asta
        AstaInversa asta = TestDataAstaInversa.createAstaInversaA(categoriaAsta, proprietario); // Associamo l'account all'asta

        AstaInversaDto astaInversaDto = astaInversaMapper.toDto(asta);

        String result = objectMapper.writeValueAsString(astaInversaDto); // Serializziamo l'oggetto Java in Json

        // Act
        String oracolo = "{\"idAsta\":null,\"categoria\":\"Videogiochi\",\"nome\":\"Dragon Age: Origins Xbox 360\",\"descrizione\":\"Edizione Xbox 360 del videogioco Dragon Age: Origins. Ci giocava mio marito.\",\"dataScadenza\":[2024,6,19],\"oraScadenza\":[18,44],\"immagine\":null,\"notificheAssociateShallow\":[],\"proprietarioShallow\":{\"email\":\"pippo.baudo@gmail.com\",\"tipoAccount\":\"Compratore\"},\"sogliaIniziale\":1.0,\"offerteRicevuteShallow\":[]}";

        // Assert
        assertEquals(oracolo, result);
    }


    void testObjectMapperCreateJavaObjectFromJsonObject() throws JsonProcessingException, InvalidTypeException {
        // Arrange
        String json = "{\"idAsta\":null,\"categoria\":\"Videogiochi\",\"nome\":\"Dragon Age: Origins Xbox 360\",\"descrizione\":\"Edizione Xbox 360 del videogioco Dragon Age: Origins. Ci giocava mio marito.\",\"dataScadenza\":[2024,6,19],\"oraScadenza\":[18,44],\"immagine\":null,\"notificheAssociateShallow\":[],\"proprietarioShallow\":{\"email\":\"pippo.baudo@gmail.com\",\"tipoAccount\":\"Compratore\"},\"sogliaIniziale\":1.0,\"offerteRicevuteShallow\":[]}";
        AstaInversaDto astaInversaDto = objectMapper.readValue(json, AstaInversaDto.class); // Serializziamo l'oggetto Java in Json
        //AstaInversa result = astaInversaMapper.toEntity(astaInversaDto);

        // Act
        Profilo profilo = TestDataProfilo.createProfiloCompratoreA(); // Creiamo l'account per l'asta
        Compratore proprietario = profilo.getCompratore(); // Cerchiamo l'account di tipo compratore
        CategoriaAsta categoriaAsta = TestDataCategoriaAsta.createCategoriaAstaE(); // Creiamo la categoria per l'asta
        AstaInversa oracolo = TestDataAstaInversa.createAstaInversaA(categoriaAsta, proprietario); // Associamo l'account all'asta

        // Assert
        //assertEquals(oracolo, result);
    }
}
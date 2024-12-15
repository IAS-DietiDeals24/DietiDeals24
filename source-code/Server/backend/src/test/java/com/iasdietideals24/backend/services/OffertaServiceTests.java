package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.OffertaDto;
import com.iasdietideals24.backend.services.implementations.OffertaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class OffertaServiceTests {

    private OffertaService offertaService;

    @BeforeEach
    void initUnderTest() {
        offertaService = new OffertaServiceImpl();
    }

    @Test
    void testCheckFieldsValid_idMaggioreDataMinoreOraMinoreValoreMaggiore() {
        // Arrange
        OffertaDto offertaDto = new OffertaDto();
        offertaDto.setIdOfferta(5L);
        offertaDto.setDataInvio(LocalDate.now().minusDays(5));
        offertaDto.setOraInvio(LocalTime.now().minusMinutes(5));
        offertaDto.setValore(BigDecimal.valueOf(1));

        // Act & Assert
        assertDoesNotThrow(() -> offertaService.checkFieldsValid(offertaDto));
    }

    @Test
    void testCheckFieldsValid_idZeroDataAttualeOraAttualeValoreMaggiore() {
        // Arrange
        OffertaDto offertaDto = new OffertaDto();
        offertaDto.setIdOfferta(0L);
        offertaDto.setDataInvio(LocalDate.now());
        offertaDto.setOraInvio(LocalTime.now());
        offertaDto.setValore(BigDecimal.valueOf(2));

        // Act & Assert
        assertDoesNotThrow(() -> offertaService.checkFieldsValid(offertaDto));
    }

    @Test
    void testCheckFieldsValid_idMinoreDataMinoreOraMaggioreValoreMaggiore() {
        // Arrange
        OffertaDto offertaDto = new OffertaDto();
        offertaDto.setIdOfferta(-5L);
        offertaDto.setDataInvio(LocalDate.now().minusDays(5));
        offertaDto.setOraInvio(LocalTime.now().plusMinutes(6));
        offertaDto.setValore(BigDecimal.valueOf(4));

        // Act & Assert
        assertDoesNotThrow(() -> offertaService.checkFieldsValid(offertaDto));
    }

    @Test
    void testCheckFieldsValid_idNullDataAttualeOraMinoreValoreMaggiore() {
        // Arrange
        OffertaDto offertaDto = new OffertaDto();
        offertaDto.setIdOfferta(null);
        offertaDto.setDataInvio(LocalDate.now());
        offertaDto.setOraInvio(LocalTime.now().minusMinutes(6));
        offertaDto.setValore(BigDecimal.valueOf(4));

        // Act & Assert
        assertDoesNotThrow(() -> offertaService.checkFieldsValid(offertaDto));
    }

    @Test
    void testCheckFieldsValid_idMaggioreDataAttualeOraMaggioreValoreMaggiore() {
        // Arrange
        OffertaDto offertaDto = new OffertaDto();
        offertaDto.setIdOfferta(5L);
        offertaDto.setDataInvio(LocalDate.now());
        offertaDto.setOraInvio(LocalTime.now().plusMinutes(5));
        offertaDto.setValore(BigDecimal.valueOf(1));

        // Act & Assert
        assertThrowsExactly(InvalidParameterException.class, () -> offertaService.checkFieldsValid(offertaDto));
    }

    @Test
    void testCheckFieldsValid_idMaggioreDataMaggioreOraMinoreValoreMaggiore() {
        // Arrange
        OffertaDto offertaDto = new OffertaDto();
        offertaDto.setIdOfferta(4L);
        offertaDto.setDataInvio(LocalDate.now().plusDays(3));
        offertaDto.setOraInvio(LocalTime.now().minusMinutes(6));
        offertaDto.setValore(BigDecimal.valueOf(4));

        // Act & Assert
        assertThrowsExactly(InvalidParameterException.class, () -> offertaService.checkFieldsValid(offertaDto));
    }

    @Test
    void testCheckFieldsValid_idMaggioreDataNullOraMinoreValoreMaggiore() {
        // Arrange
        OffertaDto offertaDto = new OffertaDto();
        offertaDto.setIdOfferta(4L);
        offertaDto.setDataInvio(null);
        offertaDto.setOraInvio(LocalTime.now().minusMinutes(6));
        offertaDto.setValore(BigDecimal.valueOf(4));

        // Act & Assert
        assertThrowsExactly(InvalidParameterException.class, () -> offertaService.checkFieldsValid(offertaDto));
    }

    @Test
    void testCheckFieldsValid_idMaggioreDataMinoreOraNullValoreMaggiore() {
        // Arrange
        OffertaDto offertaDto = new OffertaDto();
        offertaDto.setIdOfferta(4L);
        offertaDto.setDataInvio(LocalDate.now().minusDays(5));
        offertaDto.setOraInvio(null);
        offertaDto.setValore(BigDecimal.valueOf(1));

        // Act & Assert
        assertThrowsExactly(InvalidParameterException.class, () -> offertaService.checkFieldsValid(offertaDto));
    }

    @Test
    void testCheckFieldsValid_idMaggioreDataMinoreOraMinoreValoreZero() {
        // Arrange
        OffertaDto offertaDto = new OffertaDto();
        offertaDto.setIdOfferta(5L);
        offertaDto.setDataInvio(LocalDate.now().minusDays(5));
        offertaDto.setOraInvio(LocalTime.now().minusMinutes(5));
        offertaDto.setValore(BigDecimal.valueOf(0));

        // Act & Assert
        assertThrowsExactly(InvalidParameterException.class, () -> offertaService.checkFieldsValid(offertaDto));
    }

    @Test
    void testCheckFieldsValid_idMaggioreDataMinoreOraMinoreValoreMinore() {
        // Arrange
        OffertaDto offertaDto = new OffertaDto();
        offertaDto.setIdOfferta(5L);
        offertaDto.setDataInvio(LocalDate.now().minusDays(5));
        offertaDto.setOraInvio(LocalTime.now().minusMinutes(5));
        offertaDto.setValore(BigDecimal.valueOf(-5));

        // Act & Assert
        assertThrowsExactly(InvalidParameterException.class, () -> offertaService.checkFieldsValid(offertaDto));
    }

    @Test
    void testCheckFieldsValid_idMaggioreDataMinoreOraMinoreValoreNull() {
        // Arrange
        OffertaDto offertaDto = new OffertaDto();
        offertaDto.setIdOfferta(5L);
        offertaDto.setDataInvio(LocalDate.now().minusDays(5));
        offertaDto.setOraInvio(LocalTime.now().minusMinutes(5));
        offertaDto.setValore(null);

        // Act & Assert
        assertThrowsExactly(InvalidParameterException.class, () -> offertaService.checkFieldsValid(offertaDto));
    }
}

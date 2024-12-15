package com.iasdietideals24.backend.services;

import com.iasdietideals24.backend.mapstruct.mappers.TokensAccountMapper;
import com.iasdietideals24.backend.repositories.AccountRepository;
import com.iasdietideals24.backend.services.helper.RelationsConverter;
import com.iasdietideals24.backend.services.implementations.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceTests {

    @MockBean
    private TokensAccountMapper tokensAccountMapper;

    @MockBean
    private RelationsConverter relationsConverter;

    @MockBean
    private AccountRepository accountRepository;


    private AccountServiceImpl accountService;

    @BeforeEach
    void initUnderTest() {
        this.accountService = new AccountServiceImpl(tokensAccountMapper, relationsConverter, accountRepository);
    }

    // Test Black-Box
    @Test
    void testIsEmailAndPasswordValid_EmailNullPasswordNull() {
        // Arrange
        String email = null;
        String password = null;

        boolean result = accountService.isEmailAndPasswordValid(email, password);

        // Act
        boolean oracolo = false;

        // Assert
        assertEquals(oracolo, result);
    }

    @Test
    void testIsEmailAndPasswordValid_EmailBlankPasswordBlank() {
        // Arrange
        String email = "";
        String password = "";

        boolean result = accountService.isEmailAndPasswordValid(email, password);

        // Act
        boolean oracolo = false;

        // Assert
        assertEquals(oracolo, result);
    }

    @Test
    void testIsEmailAndPasswordValid_EmailValidPasswordValid_1() {
        // Arrange
        String email = "abc@def.it";
        String password = "abc";

        boolean result = accountService.isEmailAndPasswordValid(email, password);

        // Act
        boolean oracolo = true;

        // Assert
        assertEquals(oracolo, result);
    }

    @Test
    void testIsEmailAndPasswordValid_EmailNotValidPasswordBlank() {
        // Arrange
        String email = "abc";
        String password = "";

        boolean result = accountService.isEmailAndPasswordValid(email, password);

        // Act
        boolean oracolo = false;

        // Assert
        assertEquals(oracolo, result);
    }

    // Test White-Box
    @Test
    void testIsEmailAndPasswordValid_EmailBlankPasswordNull() {
        // Arrange
        String email = "";
        String password = null;

        boolean result = accountService.isEmailAndPasswordValid(email, password);

        // Act
        boolean oracolo = false;

        // Assert
        assertEquals(oracolo, result);
    }

    @Test
    void testIsEmailAndPasswordValid_EmailNotValidPasswordValid() {
        // Arrange
        String email = "abc";
        String password = "abc";

        boolean result = accountService.isEmailAndPasswordValid(email, password);

        // Act
        boolean oracolo = false;

        // Assert
        assertEquals(oracolo, result);
    }

    @Test
    void testIsEmailAndPasswordValid_EmailValidPasswordNull() {
        // Arrange
        String email = "abc@def.it";
        String password = null;

        boolean result = accountService.isEmailAndPasswordValid(email, password);

        // Act
        boolean oracolo = false;

        // Assert
        assertEquals(oracolo, result);
    }

    @Test
    void testIsEmailAndPasswordValid_EmailValidPasswordValid_2() {
        // Arrange
        String email = "ABc@CDe.A";
        String password = "123";

        boolean result = accountService.isEmailAndPasswordValid(email, password);

        // Act
        boolean oracolo = true;

        // Assert
        assertEquals(oracolo, result);
    }
}

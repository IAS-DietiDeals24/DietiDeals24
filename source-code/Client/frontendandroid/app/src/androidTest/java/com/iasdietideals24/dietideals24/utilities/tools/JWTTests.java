package com.iasdietideals24.dietideals24.utilities.tools;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class JWTTests {

    @Test
    public void test_emailPresente() {
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InRlc3RAdGVzdC5jb20ifQ.sig";
        String email = JWT.Companion.getUserEmail(jwt);
        assertEquals("test@test.com", email);
    }

    @Test
    public void test_emailAssente() {
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.sig";
        String email = JWT.Companion.getUserEmail(jwt);
        assertEquals("", email);
    }

    @Test
    public void test_jwtVuoto() {
        String jwt = "";
        String email = JWT.Companion.getUserEmail(jwt);
        assertEquals("", email);
    }
}

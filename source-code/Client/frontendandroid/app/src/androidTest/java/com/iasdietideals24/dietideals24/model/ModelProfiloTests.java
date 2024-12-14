package com.iasdietideals24.dietideals24.model;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.iasdietideals24.dietideals24.utilities.exceptions.EccezioneCampiNonCompilati;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;

@RunWith(AndroidJUnit4.class)
public class ModelProfiloTests {

    private ModelProfilo viewModel;

    @Before
    public void setup() {
        viewModel = new ModelProfilo();
    }

    @Test
    public void test_TuttiMancanti() {
        assertThrows(EccezioneCampiNonCompilati.class, () -> viewModel.validate("", "", LocalDate.MIN));
    }

    @Test
    public void test_TuttiCompilati() {
        try {
            viewModel.validate("Mario", "Rossi", LocalDate.of(1980, 6, 5));
        } catch (EccezioneCampiNonCompilati e) {
            fail();
        }
    }

    @Test
    public void test_NomeMancante() {
        assertThrows(EccezioneCampiNonCompilati.class, () -> viewModel.validate("", "Rossi", LocalDate.of(1980, 6, 5)));
    }

    @Test
    public void test_CognomeMancante() {
        assertThrows(EccezioneCampiNonCompilati.class, () -> viewModel.validate("Mario", "", LocalDate.of(1980, 6, 5)));
    }

    @Test
    public void test_DataNascitaMancante() {
        assertThrows(EccezioneCampiNonCompilati.class, () -> viewModel.validate("Mario", "Rossi", LocalDate.MIN));
    }

    @Test
    public void test_NomeECognomeMancanti() {
        assertThrows(EccezioneCampiNonCompilati.class, () -> viewModel.validate("", "", LocalDate.of(1980, 6, 5)));
    }

    @Test
    public void test_NomeEDataNascitaMancanti() {
        assertThrows(EccezioneCampiNonCompilati.class, () -> viewModel.validate("", "Rossi", LocalDate.MIN));
    }

    @Test
    public void test_CognomeEDataNascitaMancanti() {
        assertThrows(EccezioneCampiNonCompilati.class, () -> viewModel.validate("Mario", "", LocalDate.MIN));
    }
}
package com.iasdietideals24.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public abstract class Offerta {
    private LocalDate dataInvio;

    private LocalTime oraInvio;

    private BigDecimal valore;
    
}

package com.iasdietideals24.backend.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public abstract class Offerta {
    @NonNull private LocalDate dataInvio;

    @NonNull private LocalTime oraInvio;

    @NonNull private BigDecimal valore;
    
}

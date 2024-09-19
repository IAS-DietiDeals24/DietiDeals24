package com.iasdietideals24.backend.mapstruct.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public abstract class OffertaDto {

    private Long idOfferta;

    private LocalDate dataInvio;

    private LocalTime oraInvio;

    private BigDecimal valore;
}

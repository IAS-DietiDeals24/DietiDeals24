package com.iasdietideals24.backend.mapstruct.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class OffertaDto {

    private Long idOfferta;

    private LocalDate dataInvio;

    private LocalTime oraInvio;

    private BigDecimal valore;
}

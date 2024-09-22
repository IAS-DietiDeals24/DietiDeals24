package com.iasdietideals24.backend.mapstruct.dto.utilities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AnagraficaProfiloDto {

    private String nome;

    private String cognome;

    private LocalDate dataNascita;

    private String areaGeografica;

    private String biografia;
}

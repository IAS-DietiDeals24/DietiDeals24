package com.iasdietideals24.dietideals24.utilities.dto.utilities;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

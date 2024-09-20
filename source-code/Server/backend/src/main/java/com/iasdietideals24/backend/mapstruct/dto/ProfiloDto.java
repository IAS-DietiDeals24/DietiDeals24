package com.iasdietideals24.backend.mapstruct.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nomeUtente")
public class ProfiloDto {

    private String nomeUtente;

    private byte[] profilePicture;

    private String nome;

    private String cognome;

    private LocalDate dataNascita;

    private String areaGeografica;

    private String biografia;

    private String linkPersonale;

    private String linkInstagram;

    private String linkFacebook;

    private String linkGitHub;

    private String linkX;

    private Set<AccountEssentialsDto> accountsEssentials;
}

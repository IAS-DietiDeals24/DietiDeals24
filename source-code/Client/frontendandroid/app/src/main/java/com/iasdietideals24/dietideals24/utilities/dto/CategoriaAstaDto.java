package com.iasdietideals24.dietideals24.utilities.dto;

import com.iasdietideals24.dietideals24.utilities.dto.shallows.AstaShallowDto;

import java.util.HashSet;
import java.util.Set;

public class CategoriaAstaDto {

    private String nome = "";

    private Set<AstaShallowDto> asteAssegnateShallow = new HashSet<>();

    public String getNome() {
        return this.nome;
    }

    public Set<AstaShallowDto> getAsteAssegnateShallow() {
        return this.asteAssegnateShallow;
    }
}

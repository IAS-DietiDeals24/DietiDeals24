package com.iasdietideals24.backend.mapstruct.dto.utilities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokensAccountDto {

    private String idFacebook;

    private String idGoogle;

    private String idX;

    private String idGitHub;
}

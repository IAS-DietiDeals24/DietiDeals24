package com.iasdietideals24.backend.mapstruct.dto.utilities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorDto {

    private String date;

    private String time;

    private String statusCode;

    private String message;
}

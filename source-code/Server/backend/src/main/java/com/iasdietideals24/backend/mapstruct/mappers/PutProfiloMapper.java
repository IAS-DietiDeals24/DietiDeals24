package com.iasdietideals24.backend.mapstruct.mappers;

import com.iasdietideals24.backend.entities.Profilo;
import com.iasdietideals24.backend.exceptions.InvalidTypeException;
import com.iasdietideals24.backend.mapstruct.dto.exceptional.PutProfiloDto;

public interface PutProfiloMapper {

    // PutProfiloDto
    Profilo toEntity(PutProfiloDto putProfiloDto) throws InvalidTypeException;
}

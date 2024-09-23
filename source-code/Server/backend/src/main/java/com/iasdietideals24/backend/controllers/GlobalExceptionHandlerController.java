package com.iasdietideals24.backend.controllers;

import com.iasdietideals24.backend.exceptions.InvalidParameterException;
import com.iasdietideals24.backend.mapstruct.dto.utilities.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {InvalidParameterException.class})
    protected ResponseEntity<Object> handleInvalidParameterException(InvalidParameterException e, HttpServletRequest request) {

        log.info("Request {} raised excelption {}", request.getRequestURI(), e.getClass().getSimpleName());

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ErrorDto errorDto = toDto(e);
        errorDto.setStatusCode(String.valueOf(httpStatus.value()));

        return new ResponseEntity<>(errorDto, httpStatus);
    }

    private ErrorDto toDto(Exception exception) {
        ErrorDto errorDto = new ErrorDto();

        errorDto.setDate(LocalDate.now().toString());
        errorDto.setTime(LocalTime.now().toString());
        errorDto.setMessage(exception.getMessage());

        return errorDto;
    }
}

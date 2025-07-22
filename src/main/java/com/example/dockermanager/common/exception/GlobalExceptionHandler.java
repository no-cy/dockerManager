package com.example.dockermanager.common.exception;

import com.example.dockermanager.common.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DataAlreadyExistsException.class)
    public ResponseDto<String> handleDataAlreadyExists(DataAlreadyExistsException ex) {
        return ResponseDto.of(HttpStatus.NO_CONTENT, ex.getMessage(), null);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseDto<String> handleNotFound(NotFoundException ex) {
        return ResponseDto.of(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }
}

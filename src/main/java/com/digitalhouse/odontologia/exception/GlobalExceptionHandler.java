package com.digitalhouse.odontologia.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> processResourceNotFoundException(ResourceNotFoundException exception) {
        logger.error("Resource not found exception occurred: " + exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> processBadRequestException(BadRequestException exception) {
        logger.error("Bad request exception occurred: " + exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(HandleConflictException.class)
    public ResponseEntity<String> processHandleConflictException(HandleConflictException exception) {
        logger.error("Conflict exception occurred: " + exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }


}

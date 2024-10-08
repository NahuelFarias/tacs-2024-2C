package tacs.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tacs.dto.ErrorResponse;
import tacs.models.domain.exception.SoldOutTicketsException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SoldOutTicketsException.class)
    public ResponseEntity<ErrorResponse> handleSoldOutTicketsException(SoldOutTicketsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCause());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

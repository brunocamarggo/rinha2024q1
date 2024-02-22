package com.rinha2021q1.controller;

import com.rinha2021q1.exception.ClienteNotFoundException;
import com.rinha2021q1.exception.ParametroInvalidoException;
import com.rinha2021q1.exception.SalvoInsuficienteException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({ SalvoInsuficienteException.class, ParametroInvalidoException.class })
    public ResponseEntity<?> saldoInsuficienteExceptionHandler() {
        return ResponseEntity.unprocessableEntity().build();
    }

    @ExceptionHandler({ ClienteNotFoundException.class })
    public ResponseEntity<?> notfound() {
        return ResponseEntity.notFound().build();
    }
}

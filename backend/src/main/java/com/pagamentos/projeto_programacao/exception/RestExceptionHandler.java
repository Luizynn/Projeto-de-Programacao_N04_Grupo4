package com.pagamentos.projeto_programacao.exception;

import com.pagamentos.projeto_programacao.response.Response;
import com.pagamentos.projeto_programacao.users.exceptions.ExceptionInputInvalid;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Response<Object>> handleEntityNotFound (EntityNotFoundException err) {
        Response<Object> response = new Response<>(HttpStatus.NOT_FOUND);
        response.setErr(1);
        response.setMsg(err.getMessage());

        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @ExceptionHandler(ExceptionInputInvalid.class)
    public ResponseEntity<Response<Object>> handleCpfInvalid (ExceptionInputInvalid err) {
        Response<Object> response = new Response<>(HttpStatus.BAD_REQUEST);
        response.setErr(1);
        response.setMsg(err.getMessage());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response<Object>> handleErros (Exception err) {
        Response<Object> response = new Response<>(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setErr(1);
        response.setMsg(err.getMessage());

        return ResponseEntity.status(response.getStatus()).body(response);
    }

}

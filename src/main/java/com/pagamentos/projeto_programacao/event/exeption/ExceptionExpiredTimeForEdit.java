package com.pagamentos.projeto_programacao.event.exeption;

public class ExceptionExpiredTimeForEdit extends RuntimeException {
    public ExceptionExpiredTimeForEdit(String message) {
        super(message);
    }
}

package com.br.vidya.integration.receita.exceptions;

import org.springframework.http.HttpStatusCode;

public class ReceitaException extends RuntimeException {
    public ReceitaException(String message, HttpStatusCode status) {
        super(message);
    }
}

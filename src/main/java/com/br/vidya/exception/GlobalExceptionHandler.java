package com.br.vidya.exception;

import com.br.vidya.integration.receita.exceptions.ReceitaException;
import com.br.vidya.integration.sankhya.exceptions.SankhyaApiException;
import com.br.vidya.integration.sankhya.exceptions.SankhyaAuthorizationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusinessException(BusinessException ex, HttpServletRequest request) {
        log.error("Business Exception: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Business Exception",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(ReceitaException.class)
    public ProblemDetail handleBusinessException(ReceitaException ex, HttpServletRequest request) {
        log.error("Receita Exception: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Receita Exception",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(SankhyaAuthorizationException.class)
    public ProblemDetail handleBusinessException(SankhyaAuthorizationException ex, HttpServletRequest request) {
        log.error("Sankhya Authorization Exception: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.UNAUTHORIZED,
                "Sankhya Authorization Exception",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(SankhyaApiException.class)
    public ProblemDetail handleBusinessException(SankhyaApiException ex, HttpServletRequest request) {
        log.error("Sankhya Resource Exception: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Sankhya Resource Exception",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleBusinessException(MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.error("Parameter Exception: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Parameter exception",
                String.format("Missing parameter exception: %s", ex.getParameterName()),
                request
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.info("Resource Not found Exception: {}", ex.getMessage());
        return buildProblemDetail(
                HttpStatus.NOT_FOUND,
                "Resource not found",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("Validation Exception: {}", extractValidationErrors(ex).values());
        ProblemDetail problemDetail = buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "One or more fields are invalid.",
                request
        );

        problemDetail.setProperty("errors", extractValidationErrors(ex));
        return problemDetail;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthException(AuthenticationException ex, HttpServletRequest request) {
        log.error("Authentication Exception: {}", ex.getMessage());
        return buildProblemDetail(HttpStatus.UNAUTHORIZED, "Authentication failed", "Invalid credentials", request);
    }

    private ProblemDetail buildProblemDetail(
            HttpStatus status,
            String title,
            String detail,
            HttpServletRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    private Map<String, String> extractValidationErrors(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        return errors;
    }
}

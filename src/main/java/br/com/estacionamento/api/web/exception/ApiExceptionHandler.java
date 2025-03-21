package br.com.estacionamento.api.web.exception;

import br.com.estacionamento.api.exception.EntityNotFoundException;
import br.com.estacionamento.api.exception.InvalidCurrentPasswordException;
import br.com.estacionamento.api.exception.PasswordMismatchException;
import br.com.estacionamento.api.exception.UsernameUniqueViolationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.channels.AcceptPendingException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(AcceptPendingException.class)
    public ResponseEntity<ErrorMessage> acceptPendingException(
            AcceptPendingException ex, HttpServletRequest request
    ) {
        log.error("Api Erro: ", ex);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, ex.getMessage()));

    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorMessage> passwordMismatchException(
            RuntimeException ex, HttpServletRequest request
    ) {
        log.error("Api Erro: ", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage()));

    }

    @ExceptionHandler(InvalidCurrentPasswordException.class)
    public ResponseEntity<ErrorMessage> InvalidCurrentPasswordException(
            RuntimeException ex, HttpServletRequest request
    ) {
        log.error("Api Erro: ", ex);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNAUTHORIZED, ex.getMessage()));

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> entityNotFoundExcpetion(
            RuntimeException ex, HttpServletRequest request
    ) {
        log.error("Api Erro: ", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage()));

    }

    @ExceptionHandler(UsernameUniqueViolationException.class)
    public ResponseEntity<ErrorMessage> uniqueViolationException(
            RuntimeException ex, HttpServletRequest request
    ) {
        log.error("Api Erro: ", ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.CONFLICT, ex.getMessage()));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request, BindingResult result
    ) {
        log.error("Api Erro: ", ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) invalido(s).", result));

    }
}

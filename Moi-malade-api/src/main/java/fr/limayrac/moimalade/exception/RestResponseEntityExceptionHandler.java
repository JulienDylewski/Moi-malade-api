package fr.limayrac.moimalade.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public RestResponseEntityExceptionHandler() {
        super();
    }

    //400
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleBadRequest(final ConstraintViolationException ex, final WebRequest request) {
        final String body = "Erreur : violation de contrainte.";
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleBadRequest(final DataIntegrityViolationException ex, final WebRequest request) {
        final String body = "Erreur : violation d'intégrité de données.";
        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<Object> handleBadRequest(final RuntimeException ex, final WebRequest request) {
        String body;

        if (ex.getMessage() == null) {
            body = "Une erreur est survenue lors de la requête, veuillez vérifier les informations envoyées.";
        } else {
            body = ex.getMessage();
        }

        return handleExceptionInternal(ex, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String body = "Erreur : HttpMessage non lisible.";
        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String body = "Erreur : argument dans la méthode non valide.";
        return handleExceptionInternal(ex, body, headers, HttpStatus.BAD_REQUEST, request);
    }

    //401
    @ExceptionHandler(value = { UnauthorizedException.class })
    protected ResponseEntity<Object> handleUnauthorized(final RuntimeException ex, final WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    //404
    @ExceptionHandler(value = { EntityNotFoundException.class, RessourceNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(final RuntimeException ex, final WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    //500
    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        final String bodyOfResponse = "Une erreur est survenue.";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
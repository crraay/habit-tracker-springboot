package com.kostya.habittracker.exception;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.kostya.habittracker.dto.ErrorResponse;
import com.kostya.habittracker.filter.CorrelationIdFilter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        @NonNull MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request
        ) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
    
        HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
        String path = httpRequest.getRequestURI();
        String cid = getCorrelationId();
        log.error("400 Validation error: path={} msg={}", path, message);

        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Bad request")
            .message(message)
            .path(path)
            .correlationId(cid)
            .timestamp(Instant.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequest(BadRequestException e, HttpServletRequest request) {
        String cid = getCorrelationId();
        String path = request.getRequestURI();
        log.error("400 Bad Request: path={} msg={}", path, e.getMessage());

        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Bad request")
            .message(e.getMessage() != null ? e.getMessage() : "Bad request")
            .path(path)
            .correlationId(cid)
            .timestamp(Instant.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> unauthorized(UnauthorizedException e, HttpServletRequest request) {
        String cid = getCorrelationId();
        String path = request.getRequestURI();
        log.error("401 Unauthorized: path={} msg={}", path, e.getMessage());

        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.UNAUTHORIZED.value())
            .error("Unauthorized")
            .message(e.getMessage() != null ? e.getMessage() : "Incorrect authentication info")
            .path(path)
            .correlationId(cid)
            .timestamp(Instant.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> forbidden(ForbiddenException e, HttpServletRequest request) {
        String cid = getCorrelationId();
        String path = request.getRequestURI();
        log.error("403 Forbidden: path={} msg={}", path, e.getMessage());

        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.FORBIDDEN.value())
            .error("Forbidden")
            .message(e.getMessage() != null ? e.getMessage() : "Not allowed")
            .path(path)
            .correlationId(cid)
            .timestamp(Instant.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(NotFoundException e, HttpServletRequest request) {
        String cid = getCorrelationId();
        String path = request.getRequestURI();
        log.error("404 Not Found: path={} msg={}", path, e.getMessage());

        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .error("Not found")
            .message(e.getMessage() != null ? e.getMessage() : "Not found")
            .path(path)
            .correlationId(cid)
            .timestamp(Instant.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> conflict(ConflictException e, HttpServletRequest request) {
        String cid = getCorrelationId();
        String path = request.getRequestURI();
        log.error("409 Conflict: path={} msg={}", path, e.getMessage());

        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.CONFLICT.value())
            .error("Conflict")
            .message(e.getMessage() != null ? e.getMessage() : "Data already exists")
            .path(path)
            .correlationId(cid)
            .timestamp(Instant.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> serverError(Exception e, HttpServletRequest request) {
        String cid = getCorrelationId();
        String path = request.getRequestURI();
        log.error("500 Internal Server Error: path={}", path, e);

        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("Internal server error")
            .message(e.getMessage() != null ? e.getMessage() : "Internal server error")
            .path(path)
            .correlationId(cid)
            .timestamp(Instant.now())
            .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getCorrelationId() {
        return Optional.ofNullable(MDC.get(CorrelationIdFilter.CORRELATION_ID_HEADER)).orElse(null);
    }
}

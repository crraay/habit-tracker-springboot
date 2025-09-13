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
        log.warn("400 Validation error: path={} msg={}", path, message);
        ErrorResponse body = build(HttpStatus.BAD_REQUEST, "Bad request", message, path, cid);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorResponse badRequest(BadRequestException e, HttpServletRequest request) {
        String cid = getCorrelationId();
        String path = request.getRequestURI();
        log.warn("400 Bad Request: path={} msg={}", path, e.getMessage());
        return build(HttpStatus.BAD_REQUEST, "Bad request", e.getMessage(), path, cid);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponse unAuthorized(UnauthorizedException e, HttpServletRequest request) {
        String cid = getCorrelationId();
        String path = request.getRequestURI();
        log.warn("401 Unauthorized: path={} msg={}", path, e.getMessage());
        return build(HttpStatus.UNAUTHORIZED, "Unauthorized", "Incorrect authentication info", path, cid);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ErrorResponse forbidden(ForbiddenException e, HttpServletRequest request) {
        String cid = getCorrelationId();
        String path = request.getRequestURI();
        log.warn("403 Forbidden: path={} msg={}", path, e.getMessage());
        return build(HttpStatus.FORBIDDEN, "Forbidden", "Not allowed", path, cid);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse notFound(NotFoundException e, HttpServletRequest request) {
        String cid = getCorrelationId();
        String path = request.getRequestURI();
        log.warn("404 Not Found: path={} msg={}", path, e.getMessage());
        return build(HttpStatus.NOT_FOUND, "Not found", e.getMessage(), path, cid);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ErrorResponse conflict(ConflictException e, HttpServletRequest request) {
        String cid = getCorrelationId();
        String path = request.getRequestURI();
        log.warn("409 Conflict: path={} msg={}", path, e.getMessage());
        return build(HttpStatus.CONFLICT, "Conflict", "Data already exists", path, cid);
    }

    
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse serverError(Exception e, HttpServletRequest request) {
        String cid = getCorrelationId();
        String path = request.getRequestURI();
        log.error("500 Internal Server Error: path={}", path, e);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", "Internal server error", path, cid);
    }

    private ErrorResponse build(HttpStatus status, String error, String message, String path, String correlationId) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(status.value());
        response.setError(error);
        response.setMessage(message);
        response.setPath(path);
        response.setCorrelationId(correlationId);
        response.setTimestamp(Instant.now());
        return response;
    }

    private String getCorrelationId() {
        return Optional.ofNullable(MDC.get(CorrelationIdFilter.CORRELATION_ID_HEADER)).orElse(null);
    }
}

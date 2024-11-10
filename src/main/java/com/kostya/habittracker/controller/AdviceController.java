package com.kostya.habittracker.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;

import com.kostya.habittracker.exception.BadRequestException;
import com.kostya.habittracker.exception.ConflictException;
import com.kostya.habittracker.exception.ForbiddenException;
import com.kostya.habittracker.exception.NotFoundException;
import com.kostya.habittracker.exception.UnauthorizedException;
import com.kostya.habittracker.model.ErrorResponse;

@RestControllerAdvice
public class AdviceController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ErrorResponse badRequest() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(400);
        response.setError("Bad request");
        response.setMessage("Bad request parameter");

        return response;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ErrorResponse unAuthorized() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(401);
        response.setError("Unauthorized");
        response.setMessage("Incorrect authentication info");

        return response;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenException.class)
    public ErrorResponse forbidden() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(403);
        response.setError("Forbidden");
        response.setMessage("Not allowed");

        return response;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse notFound(NotFoundException e) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(404);
        response.setError("Not found");
        response.setMessage(e.getMessage());

        return response;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public ErrorResponse conflict() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(409);
        response.setError("Conflict");
        response.setMessage("Already exist data");

        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse serverError(Exception e)   {
        e.printStackTrace();
        ErrorResponse response = new ErrorResponse();
        response.setStatus(500);
        response.setError("Internal server error");
        response.setMessage("Internal server error");

        return response;
    }
}

package org.emmek.beu2w3p.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Date;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorPayload handleNotFound(NotFoundException e) {
        return new ErrorPayload(e.getMessage(), new Date());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorPayload handleBadRequest(BadRequestException e) {

        if (e.getErrorsList() != null) {
            List<String> errorsList = e.getErrorsList().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ErrorPayloadWithListDTO(e.getMessage(), new Date(), errorsList);
        } else {
            return new ErrorPayload(e.getMessage(), new Date());
        }
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorPayload handleIllegalArgument(IllegalArgumentException e) {
        return new ErrorPayload("Argument not Valid!", new Date());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorPayload handleUnauthorized(UnauthorizedException e) {
        return new ErrorPayload(e.getMessage(), new Date());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorPayload handleGeneric(Exception e) {
        log.error("Server Error: NERV!", e);
        return new ErrorPayload("Server Error: NERV!", new Date());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public ErrorPayload handleAccessDenied(AccessDeniedException e) {
        return new ErrorPayload(e.getMessage(), new Date());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED) // 405
    public ErrorPayload handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        return new ErrorPayload(e.getMessage(), new Date());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorPayload handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        return new ErrorPayload(e.getMessage(), new Date());
    }

    @ExceptionHandler(ParticipatingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorPayload handleParticipatingException(ParticipatingException e) {
        return new ErrorPayload(e.getMessage(), new Date());
    }

}

package com.amp.accountmanagement.controller;

import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;

import com.amp.accountmanagement.model.dto.ErrorTO;
import com.amp.accountmanagement.service.error.ResourceNotFoundException;
import com.amp.accountmanagement.service.error.ServiceException;
import com.amp.accountmanagement.service.error.ValidationException;
import cz.jirutka.rsql.parser.ParseException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestControllerAdvice
@ResponseBody
public class ErrorHandler {

  private static final String DTO_VALIDATION_MSG_TEMPLATE = "Property %s: %s";
  private static final String INVALID_FILTER_QUERY_MSG = "Invalid filter query syntax.";

  @ExceptionHandler(Exception.class)
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  ErrorTO handleAny(Exception exception) {
    return new ErrorTO(exception.getMessage(), INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ParseException.class)
  @ResponseStatus(BAD_REQUEST)
  ErrorTO handle(ParseException exception) {
    return new ErrorTO(INVALID_FILTER_QUERY_MSG, BAD_REQUEST);
  }

  @ExceptionHandler(ServiceException.class)
  @ResponseStatus(UNPROCESSABLE_ENTITY)
  ErrorTO handle(ServiceException exception) {
    return new ErrorTO(exception.getMessage(), UNPROCESSABLE_ENTITY);
  }

  // BASIC VALIDATION HANDLERS

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(UNPROCESSABLE_ENTITY)
  ErrorTO handle(ValidationException exception) {
    return new ErrorTO(exception.getMessage(), UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(UNPROCESSABLE_ENTITY)
  ErrorTO handle(ConstraintViolationException exception) {
    final ErrorTO errorsTO = new ErrorTO(UNPROCESSABLE_ENTITY);
    errorsTO.setMessages(
        exception
            .getConstraintViolations()
            .stream()
            .map(
                constraintViolation ->
                    String.format(
                        "%s: %s",
                        constraintViolation.getPropertyPath(), constraintViolation.getMessage()))
            .collect(Collectors.toList()));

    return errorsTO;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(UNPROCESSABLE_ENTITY)
  ErrorTO handle(MethodArgumentNotValidException exception) {
    final ErrorTO errorsTO = new ErrorTO(UNPROCESSABLE_ENTITY);
    errorsTO.setMessages(
        exception
            .getBindingResult()
            .getFieldErrors()
            .stream()
            .map(
                fieldError ->
                    String.format(
                        DTO_VALIDATION_MSG_TEMPLATE,
                        fieldError.getField(),
                        fieldError.getDefaultMessage()))
            .collect(Collectors.toList()));

    return errorsTO;
  }

  // BASIC HANDLERS

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(BAD_REQUEST)
  ErrorTO handle(HttpMessageNotReadableException exception) {
    return new ErrorTO(exception.getMessage(), BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(BAD_REQUEST)
  ErrorTO handle(MethodArgumentTypeMismatchException exception) {
    return new ErrorTO(exception.getMessage(), BAD_REQUEST);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(METHOD_NOT_ALLOWED)
  ErrorTO handle(HttpRequestMethodNotSupportedException exception) {
    return new ErrorTO(exception.getMessage(), METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(NOT_FOUND)
  ErrorTO handle(ResourceNotFoundException exception) {
    return new ErrorTO(exception.getMessage(), NOT_FOUND);
  }
}

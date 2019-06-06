package org.art.web.warrior.compiler.config.exchandler;

import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

import static org.art.web.warrior.commons.CommonConstants.*;

@ControllerAdvice
public class ServiceExceptionHandler {

    private static final String VALIDATION_ERROR_PREF = "Validation error: field \"";

    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CompilationResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String validationErrors = e.getBindingResult().getAllErrors().stream()
                .map(er -> {
                    String fieldName = er.getObjectName();
                    String errorMessage = er.getDefaultMessage();
                    return VALIDATION_ERROR_PREF + fieldName + QUOTE_CH + DOT_CH + SPACE_CH + errorMessage;
                }).collect(Collectors.joining(NEW_LINE));
        return CompilationResponse.builder()
                .compilerStatus(ServiceResponseStatus.BAD_REQUEST.getStatusId())
                .compilerStatusCode(ServiceResponseStatus.BAD_REQUEST.getStatusCode())
                .message(validationErrors)
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleTaskNotFoundException(RuntimeException e) {
        return e.getMessage();
    }
}

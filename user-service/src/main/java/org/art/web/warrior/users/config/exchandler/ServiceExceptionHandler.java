package org.art.web.warrior.users.config.exchandler;

import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.common.CommonApiError;
import org.art.web.warrior.users.exception.RoleNotFoundException;
import org.art.web.warrior.users.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ServiceExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(er -> {
            String fieldName = er.getObjectName();
            String errorMessage = er.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class, RoleNotFoundException.class})
    public CommonApiError handleNotFoundException(Exception e) {
        int respStatusCode = ServiceResponseStatus.NOT_FOUND.getStatusCode();
        String respStatus = ServiceResponseStatus.NOT_FOUND.getStatusId();
        String message = e.getMessage();
        return buildApiErrorResponse(respStatusCode, respStatus, message);
    }

    private CommonApiError buildApiErrorResponse(int respStatusCode, String respStatus, String message) {
        return new CommonApiError(respStatusCode, respStatus, message, LocalDateTime.now());
    }
}

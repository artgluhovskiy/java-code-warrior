package org.art.web.warrior.users.config.exchandler;

import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.common.CommonApiError;
import org.art.web.warrior.users.exception.EmailExistsException;
import org.art.web.warrior.users.exception.RoleNotFoundException;
import org.art.web.warrior.users.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static org.art.web.warrior.commons.CommonConstants.NEW_LINE;
import static org.art.web.warrior.commons.CommonConstants.SPACE_CH;

@ResponseBody
@ControllerAdvice
public class ServiceExceptionHandler {

    private static final String VALIDATION_ERROR_PREF = "Validation error.";

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        int respStatusCode = ServiceResponseStatus.BAD_REQUEST.getStatusCode();
        String respStatus = ServiceResponseStatus.BAD_REQUEST.getStatusId();
        String errorsMessage = e.getBindingResult().getAllErrors().stream()
                .map(er -> {
                    String errorMessage = er.getDefaultMessage();
                    return VALIDATION_ERROR_PREF + SPACE_CH + errorMessage + SPACE_CH;
                }).collect(Collectors.joining(NEW_LINE));
        return buildApiErrorResponse(respStatusCode, respStatus, errorsMessage);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class, RoleNotFoundException.class})
    public CommonApiError handleNotFoundException(Exception e) {
        int respStatusCode = ServiceResponseStatus.NOT_FOUND.getStatusCode();
        String respStatus = ServiceResponseStatus.NOT_FOUND.getStatusId();
        String message = e.getMessage();
        return buildApiErrorResponse(respStatusCode, respStatus, message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailExistsException.class)
    public CommonApiError handleEmailExistsException(EmailExistsException e) {
        int respStatusCode = ServiceResponseStatus.BAD_REQUEST.getStatusCode();
        String respStatus = ServiceResponseStatus.BAD_REQUEST.getStatusId();
        String message = e.getMessage();
        return buildApiErrorResponse(respStatusCode, respStatus, message);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonApiError handleException(Exception e) {
        int respStatusCode = ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusCode();
        String respStatus = ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusId();
        String message = "User Service. Internal service error occurred. " + e.getMessage();
        return buildApiErrorResponse(respStatusCode, respStatus, message);
    }

    private CommonApiError buildApiErrorResponse(int respStatusCode, String respStatus, String message) {
        return new CommonApiError(respStatusCode, respStatus, message, LocalDateTime.now());
    }
}

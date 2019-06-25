package org.art.web.warrior.compiler.config.exchandler;

import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.common.CommonApiError;
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

    @ResponseBody
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
        return buildServiceErrorResponse(respStatusCode, respStatus, errorsMessage);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonApiError handleException(Exception e) {
        int respStatusCode = ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusCode();
        String respStatus = ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusId();
        String message = "Compilation Service. Internal service error occurred. " + e.getMessage();
        return buildServiceErrorResponse(respStatusCode, respStatus, message);
    }

    private CommonApiError buildServiceErrorResponse(int respStatusCode, String respStatus, String failedTestMessage) {
        return new CommonApiError(respStatusCode, respStatus, failedTestMessage, LocalDateTime.now());
    }
}

package org.art.web.warrior.exec.config.exchandler;

import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.common.CommonApiError;
import org.art.web.warrior.commons.driver.exception.ClientCodeExecutionException;
import org.art.web.warrior.commons.execution.error.ExecutionError;
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
import static org.art.web.warrior.exec.ServiceCommonConstants.CLIENT_CODE_EXEC_ERROR_MESSAGE;

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
        return buildServiceErrorResponse(respStatusCode, respStatus, errorsMessage);
    }

    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(ClientCodeExecutionException.class)
    public ExecutionError handleClientCodeExecutionException(ClientCodeExecutionException e) {
        int respStatusCode = ServiceResponseStatus.CODE_EXEC_ERROR.getStatusCode();
        String respStatus = ServiceResponseStatus.CODE_EXEC_ERROR.getStatusId();
        String failedTestMessage = e.getMessage();
        return buildExecutionErrorResponse(respStatusCode, respStatus, failedTestMessage);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public CommonApiError handleException(Exception e) {
        int respStatusCode = ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusCode();
        String respStatus = ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusId();
        String message = "Execution Service. Internal service error occurred. " + e.getMessage();
        return buildServiceErrorResponse(respStatusCode, respStatus, message);
    }

    private CommonApiError buildServiceErrorResponse(int respStatusCode, String respStatus, String failedTestMessage) {
        return new CommonApiError(respStatusCode, respStatus, failedTestMessage, LocalDateTime.now());
    }

    private ExecutionError buildExecutionErrorResponse(int respStatusCode, String respStatus, String failedTestMessage) {
        return new ExecutionError(respStatusCode, respStatus, CLIENT_CODE_EXEC_ERROR_MESSAGE, failedTestMessage, LocalDateTime.now());
    }
}

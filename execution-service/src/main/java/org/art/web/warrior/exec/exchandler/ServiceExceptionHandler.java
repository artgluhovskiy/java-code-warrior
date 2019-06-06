package org.art.web.warrior.exec.exchandler;

import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.driver.exception.ClientCodeExecutionException;
import org.art.web.warrior.commons.execution.dto.ExecutionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

import static org.art.web.warrior.commons.CommonConstants.SPACE_CH;
import static org.art.web.warrior.exec.ServiceCommonConstants.CLIENT_CODE_EXEC_ERROR_MESSAGE;
import static org.art.web.warrior.exec.ServiceCommonConstants.CLIENT_CODE_EXEC_INTERNAL_ERROR_MESSAGE;

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
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ClientCodeExecutionException.class)
    public ExecutionResponse handleClientCodeExecutionException(ClientCodeExecutionException e) {
        return ExecutionResponse.builder()
                .respStatus(ServiceResponseStatus.CODE_EXEC_ERROR.getStatusId())
                .message(CLIENT_CODE_EXEC_ERROR_MESSAGE)
                .failedTestMessage(e.getMessage())
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExecutionResponse handleException(RuntimeException e) {
        return ExecutionResponse.builder()
                .respStatus(ServiceResponseStatus.CODE_EXEC_INTERNAL_ERROR.getStatusId())
                .message(CLIENT_CODE_EXEC_INTERNAL_ERROR_MESSAGE + SPACE_CH + e.getMessage())
                .build();
    }
}

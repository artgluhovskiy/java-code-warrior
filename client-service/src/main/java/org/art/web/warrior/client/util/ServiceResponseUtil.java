package org.art.web.warrior.client.util;

import org.apache.commons.collections4.MapUtils;
import org.art.web.warrior.client.dto.ClientServiceResponse;
import org.art.web.warrior.client.dto.CompErrorDetails;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.commons.execution.dto.ExecutionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.art.web.warrior.client.CommonServiceConstants.*;

public class ServiceResponseUtil {

    private ServiceResponseUtil() {
    }

    /* Client Service Utils */

    public static ClientServiceResponse buildClientServiceOkResponse(String message) {
        return ClientServiceResponse.builder()
                .respStatus(ServiceResponseStatus.SUCCESS.getStatusId())
                .message(message)
                .build();
    }

    /* Compilation Service Utils */

    public static boolean isCompServiceErrorResponse(ResponseEntity<CompilationResponse> serviceResp) {
        HttpStatus statusCode = serviceResp.getStatusCode();
        CompilationResponse respBody = serviceResp.getBody();
        return statusCode != HttpStatus.OK
                || respBody == null
                || MapUtils.isEmpty(respBody.getCompUnitResults())
                || respBody.isCompError();
    }

    public static ClientServiceResponse buildCompServiceErrorResponse(ResponseEntity<CompilationResponse> serviceResp) {
        CompilationResponse respBody = serviceResp.getBody();
        if (respBody == null) {
            return buildEmptyBodyResp();
        }
        HttpStatus respStatus = serviceResp.getStatusCode();
        switch (respStatus) {
            case OK:
                return buildSrcCompilationErrorResponse(respBody);
            case UNPROCESSABLE_ENTITY:
            case INTERNAL_SERVER_ERROR:
                return buildCompServiceCommonErrorResponse(respBody);
            default:
                return buildUnexpectedErrorResp();
        }
    }

    private static ClientServiceResponse buildSrcCompilationErrorResponse(CompilationResponse respBody) {
        CompErrorDetails errorDetails = buildCompilationErrorDetails(respBody);
        return ClientServiceResponse.builder()
                .respStatus(ServiceResponseStatus.COMPILATION_ERROR.getStatusId())
                .message(COMPILATION_ERROR_MESSAGE)
                .compErrorDetails(errorDetails)
                .build();
    }

    private static CompErrorDetails buildCompilationErrorDetails(CompilationResponse serviceResp) {
        return CompErrorDetails.builder()
                .compilerErrorCode(serviceResp.getCompilerErrorCode())
                .compilerMessage(serviceResp.getCompilerMessage())
                .errorCodeLine(serviceResp.getErrorCodeLine())
                .errorPosition(serviceResp.getErrorPosition())
                .build();
    }

    private static ClientServiceResponse buildCompServiceCommonErrorResponse(CompilationResponse respBody) {
        return ClientServiceResponse.builder()
                .respStatus(respBody.getCompilerStatus())
                .message(respBody.getMessage())
                .build();
    }

    /* Task Service Utils */

    public static boolean isTaskServiceErrorResponse(ResponseEntity<?> serviceResp) {
        HttpStatus statusCode = serviceResp.getStatusCode();
        return statusCode != HttpStatus.OK || serviceResp.getBody() == null;
    }

    public static ClientServiceResponse buildTaskServiceErrorResp(ResponseEntity<?> serviceResp) {
        Object respBody = serviceResp.getBody();
        if (respBody == null) {
            return buildEmptyBodyResp();
        }
        HttpStatus respStatus = serviceResp.getStatusCode();
        switch (respStatus) {
            case NOT_FOUND:
                return buildTaskNotFoundResponse();
            case UNPROCESSABLE_ENTITY:
                return buildUnprocessableEntityTaskServiceResponse();
            case INTERNAL_SERVER_ERROR:
                return buildInternalTaskServiceErrorResponse();
            default:
                return buildUnexpectedErrorResp();
        }
    }

    private static ClientServiceResponse buildTaskNotFoundResponse() {
        return ClientServiceResponse.builder()
                .respStatus(ServiceResponseStatus.NOT_FOUND.getStatusId())
                .message(TASK_NOT_FOUND_ERROR_MESSAGE)
                .build();
    }

    private static ClientServiceResponse buildUnprocessableEntityTaskServiceResponse() {
        return ClientServiceResponse.builder()
                .respStatus(ServiceResponseStatus.BAD_REQUEST.getStatusId())
                .message(TASK_UNPROCESSABLE_ENTITY_ERROR_MESSAGE)
                .build();
    }

    private static ClientServiceResponse buildInternalTaskServiceErrorResponse() {
        return ClientServiceResponse.builder()
                .respStatus(ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusId())
                .message(INTERNAL_SERVICE_ERROR_MESSAGE)
                .build();
    }

    /* Execution Service Utils */

    public static boolean isExecServiceErrorResponse(ResponseEntity<ExecutionResponse> serviceResponse) {
        HttpStatus statusCode = serviceResponse.getStatusCode();
        ExecutionResponse respBody = serviceResponse.getBody();
        return statusCode != HttpStatus.OK
                || respBody == null
                || !ServiceResponseStatus.SUCCESS.getStatusId().equals(respBody.getRespStatus());
    }

    public static ClientServiceResponse buildExecServiceErrorResp(ResponseEntity<ExecutionResponse> serviceResponse) {
        ExecutionResponse respBody = serviceResponse.getBody();
        if (respBody == null) {
            return buildEmptyBodyResp();
        }
        HttpStatus respStatus = serviceResponse.getStatusCode();
        switch (respStatus) {
            case UNPROCESSABLE_ENTITY:
            case INTERNAL_SERVER_ERROR:
                return buildExecServiceCommonErrorResponse(respBody);
            case EXPECTATION_FAILED:
                return buildUserTaskExecutionResponse(respBody);
            default:
                return buildUnexpectedErrorResp();
        }
    }

    private static ClientServiceResponse buildExecServiceCommonErrorResponse(ExecutionResponse respBody) {
        return ClientServiceResponse.builder()
                .respStatus(respBody.getRespStatus())
                .message(respBody.getMessage())
                .build();
    }

    public static ClientServiceResponse buildUserTaskExecutionResponse(ExecutionResponse serviceResponse) {
        return ClientServiceResponse.builder()
                .respStatus(serviceResponse.getRespStatus())
                .message(serviceResponse.getMessage())
                .execMessage(serviceResponse.getFailedTestMessage())
                .build();
    }

    private static ClientServiceResponse buildEmptyBodyResp() {
        return ClientServiceResponse.builder()
                .respStatus(ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusId())
                .message(INTERNAL_SERVICE_ERROR_MESSAGE)
                .build();
    }

    private static ClientServiceResponse buildUnexpectedErrorResp() {
        return ClientServiceResponse.builder()
                .respStatus(ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusId())
                .message(UNEXPECTED_SERVICE_ERROR_MESSAGE)
                .build();
    }
}

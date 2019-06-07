package org.art.web.warrior.client.util;

import org.apache.commons.collections4.MapUtils;
import org.art.web.warrior.client.dto.ClientServiceResponse;
import org.art.web.warrior.client.dto.CompErrorDetails;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.commons.execution.dto.ExecutionResponse;
import org.art.web.warrior.commons.tasking.dto.TaskDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.art.web.warrior.client.CommonServiceConstants.*;

public class ClientResponseUtil {

    private ClientResponseUtil() {
    }

    public static boolean isCompServiceOkResponse(ResponseEntity<CompilationResponse> serviceResponse) {
        HttpStatus statusCode = serviceResponse.getStatusCode();
        CompilationResponse respBody = serviceResponse.getBody();
        return statusCode == HttpStatus.OK
                && (respBody != null && MapUtils.isNotEmpty(respBody.getCompUnitResults()))
                && respBody.isCompOk();
    }

    public static ClientServiceResponse buildCompServiceErrorResponse(ResponseEntity<CompilationResponse> serviceResponse) {
        CompilationResponse respBody = serviceResponse.getBody();
        if (respBody == null) {
            return buildEmptyBodyResp();
        }
        HttpStatus respStatus = serviceResponse.getStatusCode();
        switch (respStatus) {
            case OK:
                return buildSrcCompilationErrorResponse(respBody);
            case UNPROCESSABLE_ENTITY:
                return buildUnprocessableEntityCompServiceResponse(respBody);
            case INTERNAL_SERVER_ERROR:
                return buildInternalCompServiceErrorResponse(respBody);
            default:
                return buildUnexpectedErrorResp();
        }
    }

    private static ClientServiceResponse buildUnprocessableEntityCompServiceResponse(CompilationResponse respBody) {
        return ClientServiceResponse.builder()
                .respStatus(respBody.getCompilerStatus())
                .message(respBody.getMessage())
                .build();
    }

    private static ClientServiceResponse buildInternalCompServiceErrorResponse(CompilationResponse respBody) {
        return ClientServiceResponse.builder()
                .respStatus(respBody.getCompilerStatus())
                .message(respBody.getMessage())
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

    public static boolean isTaskServiceOkResponse(ResponseEntity<TaskDto> serviceResponse) {
        HttpStatus statusCode = serviceResponse.getStatusCode();
        TaskDto respBody = serviceResponse.getBody();
        return statusCode == HttpStatus.OK && respBody != null;
    }

    public static ClientServiceResponse buildTaskServiceErrorResp(ResponseEntity<TaskDto> serviceResponse) {
        TaskDto respBody = serviceResponse.getBody();
        if (respBody == null) {
            return buildEmptyBodyResp();
        }
        HttpStatus respStatus = serviceResponse.getStatusCode();
        switch (respStatus) {
            case NOT_FOUND:
                return buildTaskNotFoundResponse();
            case UNPROCESSABLE_ENTITY:
                return buildUnprocessableEntityTaskServiceResponse(respBody);
            case INTERNAL_SERVER_ERROR:
                return buildInternalTaskServiceErrorResponse(respBody);
            default:
                return buildUnexpectedErrorResp();
        }
    }

    private static ClientServiceResponse buildInternalTaskServiceErrorResponse(TaskDto respBody) {
        //TODO
        return null;
    }

    private static ClientServiceResponse buildUnprocessableEntityTaskServiceResponse(TaskDto respBody) {
        //TODO
        return null;
    }

    private static ClientServiceResponse buildTaskNotFoundResponse() {
        return ClientServiceResponse.builder()
                .respStatus(ServiceResponseStatus.NOT_FOUND.getStatusId())
                .message(TASK_NOT_FOUND_ERROR_MESSAGE)
                .build();
    }

    public static boolean isExecServiceOkResponse(ResponseEntity<ExecutionResponse> serviceResponse) {
        HttpStatus statusCode = serviceResponse.getStatusCode();
        ExecutionResponse respBody = serviceResponse.getBody();
        return statusCode == HttpStatus.OK && respBody != null && ServiceResponseStatus.SUCCESS.getStatusId().equals(respBody.getRespStatus());
    }

    public static ClientServiceResponse buildExecServiceErrorResp(ResponseEntity<ExecutionResponse> execServiceResponse) {
        TaskDto respBody = serviceResponse.getBody();
        if (respBody == null) {
            return buildEmptyBodyResp();
        }
        HttpStatus respStatus = serviceResponse.getStatusCode();
        switch (respStatus) {
            case NOT_FOUND:
                return buildTaskNotFoundResponse();
            case UNPROCESSABLE_ENTITY:
                return buildUnprocessableEntityTaskServiceResponse(respBody);
            case INTERNAL_SERVER_ERROR:
                return buildInternalTaskServiceErrorResponse(respBody);
            default:
                return buildUnexpectedErrorResp();
        }
    }
}

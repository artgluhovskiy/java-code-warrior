package org.art.web.warrior.client.util;

import org.art.web.warrior.client.dto.ClientServiceResponse;
import org.art.web.warrior.client.dto.CompErrorDetails;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.common.CommonApiError;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.commons.execution.dto.ExecutionResponse;
import org.art.web.warrior.commons.execution.error.ExecutionError;

import static org.art.web.warrior.client.CommonServiceConstants.COMPILATION_ERROR_MESSAGE;

public class ServiceResponseUtil {

    private ServiceResponseUtil() {
    }

    public static ClientServiceResponse buildClientServiceOkResponse(String message) {
        return ClientServiceResponse.builder()
                .respStatus(ServiceResponseStatus.SUCCESS.getStatusId())
                .message(message)
                .build();
    }

    public static ClientServiceResponse buildSrcCompilationErrorResponse(CompilationResponse respBody) {
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

    public static ClientServiceResponse buildUserTaskExecutionResponse(ExecutionResponse serviceResponse) {
        return ClientServiceResponse.builder()
                .respStatus(serviceResponse.getRespStatus())
                .message(serviceResponse.getMessage())
                .execMessage(serviceResponse.getFailedTestMessage())
                .build();
    }

    public static ClientServiceResponse buildExternalServiceInvocationErrorResponse(CommonApiError apiError) {
        ClientServiceResponse clientResponse = ClientServiceResponse.builder()
                .respStatus(apiError.getRespStatus())
                .message(apiError.getMessage())
                .build();
        if (apiError instanceof ExecutionError) {
            ExecutionError execErrorDetails = (ExecutionError) apiError;
            clientResponse.setExecMessage(execErrorDetails.getFailedTestMessage());
        }
        return clientResponse;
    }
}

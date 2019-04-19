package org.art.web.warrior.client.util;

import org.art.web.warrior.client.dto.ClientServiceRequest;
import org.art.web.warrior.client.dto.ClientServiceResponse;
import org.art.web.warrior.client.dto.CompServiceResponse;
import org.art.web.warrior.client.model.ClientResponseStatus;
import org.art.web.warrior.client.model.CompErrorDetails;

public class CompServiceResponseUtil {

    private CompServiceResponseUtil() {
    }

    public static ClientServiceResponse buildEmptyBodyResponse(ClientServiceRequest clientReqData) {
        return ClientServiceResponse.builder()
                .respStatus(ClientResponseStatus.INTERNAL_SERVICE_ERROR.getStatusId())
                .message("Internal service error occurred! Compilation service responded with empty body.")
                .className(clientReqData.getClassName())
                .srcCode(clientReqData.getSrcCode())
                .build();
    }

    public static ClientServiceResponse buildCompErrorResponse(CompServiceResponse serviceResp) {
        CompErrorDetails errorDetails = CompErrorDetails.builder()
                .compilerErrorCode(serviceResp.getCompilerErrorCode())
                .compilerMessage(serviceResp.getCompilerMessage())
                .errorCodeLine(serviceResp.getErrorCodeLine())
                .errorPosition(serviceResp.getErrorPosition())
                .build();
        return ClientServiceResponse.builder()
                .respStatus(ClientResponseStatus.COMPILATION_ERROR.getStatusId())
                .message("Compilation errors occurred while compiling client source code!")
                .className(serviceResp.getClassName())
                .srcCode((String) serviceResp.getSrcCode())
                .compErrorDetails(errorDetails)
                .build();
    }

    public static ClientServiceResponse buildCompOkResponse(CompServiceResponse serviceResp) {
        return ClientServiceResponse.builder()
                .respStatus(ClientResponseStatus.SUCCESS.getStatusId())
                .message("Client source code was successfully compiled!")
                .className(serviceResp.getClassName())
                .srcCode((String) serviceResp.getSrcCode())
                .build();
    }
}

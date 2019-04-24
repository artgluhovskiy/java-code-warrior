package org.art.web.warrior.client.util;

import org.art.web.warrior.client.domain.ClientResponseStatus;
import org.art.web.warrior.client.domain.CompErrorDetails;
import org.art.web.warrior.client.dto.CompUnitRequest;
import org.art.web.warrior.client.dto.ClientServiceResponse;
import org.art.web.warrior.client.dto.CompServiceResponse;
import org.art.web.warrior.client.dto.CompUnitResponse;

import java.util.Map;

import static org.art.web.warrior.client.CommonServiceConstants.*;

public class CompServiceResponseUtil {

    private CompServiceResponseUtil() {
    }

    public static ClientServiceResponse buildUnprocessableEntityResponse(CompUnitRequest clientReqData) {
        return ClientServiceResponse.builder()
                .respStatus(ClientResponseStatus.BAD_REQUEST.getStatusId())
                .message(UNPROCESSABLE_CLIENT_REQUEST_MESSAGE)
                .className(clientReqData.getClassName())
                .srcCode(clientReqData.getSrcCode())
                .build();
    }

    public static ClientServiceResponse buildEmptyBodyResponse(CompUnitRequest clientReqData) {
        return ClientServiceResponse.builder()
                .respStatus(ClientResponseStatus.INTERNAL_SERVICE_ERROR.getStatusId())
                .message(INTERNAL_SERVICE_ERROR_MESSAGE)
                .className(clientReqData.getClassName())
                .srcCode(clientReqData.getSrcCode())
                .build();
    }

    public static ClientServiceResponse buildCompErrorResponse(CompServiceResponse serviceResp, String className) {
        CompErrorDetails errorDetails = CompErrorDetails.builder()
                .compilerErrorCode(serviceResp.getCompilerErrorCode())
                .compilerMessage(serviceResp.getCompilerMessage())
                .errorCodeLine(serviceResp.getErrorCodeLine())
                .errorPosition(serviceResp.getErrorPosition())
                .build();
        Map<String, CompUnitResponse> compUnits = serviceResp.getCompUnitResults();
        CompUnitResponse unitResult = compUnits.get(className);
        return ClientServiceResponse.builder()
                .respStatus(ClientResponseStatus.COMPILATION_ERROR.getStatusId())
                .message(COMPILATION_ERROR_MESSAGE)
                .className(unitResult.getClassName())
                .srcCode(unitResult.getSrcCode())
                .compErrorDetails(errorDetails)
                .build();
    }

    public static ClientServiceResponse buildCompOkResponse(CompServiceResponse serviceResp, String className) {
        Map<String, CompUnitResponse> compUnits = serviceResp.getCompUnitResults();
        CompUnitResponse unitResult = compUnits.get(className);
        return ClientServiceResponse.builder()
                .respStatus(ClientResponseStatus.SUCCESS.getStatusId())
                .message(COMPILATION_OK_MESSAGE)
                .className(unitResult.getClassName())
                .srcCode(unitResult.getSrcCode())
                .build();
    }
}

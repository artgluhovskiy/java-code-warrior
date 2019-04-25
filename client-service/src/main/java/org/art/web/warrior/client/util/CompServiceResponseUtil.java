package org.art.web.warrior.client.util;

import org.art.web.warrior.commons.compiler.ServiceResponseStatus;
import org.art.web.warrior.client.dto.CompErrorDetails;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitRequest;
import org.art.web.warrior.client.dto.ClientServiceResponse;
import org.art.web.warrior.commons.compiler.dto.CompServiceResponse;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitResponse;

import java.util.Map;

import static org.art.web.warrior.client.CommonServiceConstants.*;

public class CompServiceResponseUtil {

    private CompServiceResponseUtil() {
    }

    public static ClientServiceResponse buildUnprocessableEntityResponse(CompServiceUnitRequest clientReqData) {
        return ClientServiceResponse.builder()
                .respStatus(ServiceResponseStatus.BAD_REQUEST.getStatusId())
                .message(UNPROCESSABLE_CLIENT_REQUEST_MESSAGE)
                .className(clientReqData.getClassName())
                .srcCode(clientReqData.getSrcCode())
                .build();
    }

    public static ClientServiceResponse buildEmptyBodyResponse(CompServiceUnitRequest clientReqData) {
        return ClientServiceResponse.builder()
                .respStatus(ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusId())
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
        Map<String, CompServiceUnitResponse> compUnits = serviceResp.getCompUnitResults();
        CompServiceUnitResponse unitResult = compUnits.get(className);
        return ClientServiceResponse.builder()
                .respStatus(ServiceResponseStatus.COMPILATION_ERROR.getStatusId())
                .message(COMPILATION_ERROR_MESSAGE)
                .className(unitResult.getClassName())
                .srcCode(unitResult.getSrcCode())
                .compErrorDetails(errorDetails)
                .build();
    }

    public static ClientServiceResponse buildCompOkResponse(CompServiceResponse serviceResp, String className) {
        Map<String, CompServiceUnitResponse> compUnits = serviceResp.getCompUnitResults();
        CompServiceUnitResponse unitResult = compUnits.get(className);
        return ClientServiceResponse.builder()
                .respStatus(ServiceResponseStatus.SUCCESS.getStatusId())
                .message(COMPILATION_OK_MESSAGE)
                .className(unitResult.getClassName())
                .srcCode(unitResult.getSrcCode())
                .build();
    }
}

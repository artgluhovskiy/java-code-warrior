package org.art.web.warrior.client.util;

import org.art.web.warrior.client.dto.*;
import org.art.web.warrior.commons.compiler.CompServiceRespStatus;
import org.art.web.warrior.commons.compiler.dto.CompServiceResponse;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitResponse;

import java.util.Map;

import static org.art.web.warrior.client.CommonServiceConstants.*;

public class ClientResponseUtil {

    private ClientResponseUtil() {
    }

    public static ClientServiceUserResponse buildUnprocessableUserEntityResponse(UserCodeCompData clientReqData) {
        return ClientServiceUserResponse.builder()
                .respStatus(CompServiceRespStatus.BAD_REQUEST.getStatusId())
                .message(UNPROCESSABLE_CLIENT_REQUEST_MESSAGE)
                .className(clientReqData.getClassName())
                .srcCode(clientReqData.getSrcCode())
                .build();
    }

    public static ClientServiceAdminResponse buildUnprocessableAdminTaskResponse(AdminTaskCompData clientReqData) {
        return ClientServiceAdminResponse.builder()
                .respStatus(CompServiceRespStatus.BAD_REQUEST.getStatusId())
                .message(UNPROCESSABLE_CLIENT_REQUEST_MESSAGE)
                .solutionSrcCode(clientReqData.getSolutionSrcCode())
                .runnerSrcCode(clientReqData.getRunnerSrcCode())
                .build();
    }

    public static ClientServiceUserResponse buildEmptyBodyUserResponse(UserCodeCompData clientReqData) {
        return ClientServiceUserResponse.builder()
                .respStatus(CompServiceRespStatus.INTERNAL_SERVICE_ERROR.getStatusId())
                .message(INTERNAL_SERVICE_ERROR_MESSAGE)
                .className(clientReqData.getClassName())
                .srcCode(clientReqData.getSrcCode())
                .build();
    }

    public static ClientServiceAdminResponse buildEmptyBodyAdminTaskResponse(AdminTaskCompData clientReqData) {
        return ClientServiceAdminResponse.builder()
                .respStatus(CompServiceRespStatus.INTERNAL_SERVICE_ERROR.getStatusId())
                .message(INTERNAL_SERVICE_ERROR_MESSAGE)
                .solutionSrcCode(clientReqData.getSolutionSrcCode())
                .runnerSrcCode(clientReqData.getRunnerSrcCode())
                .build();
    }

    public static ClientServiceUserResponse buildCompErrorUserResponse(CompServiceResponse serviceResp, String className) {
        CompErrorDetails errorDetails = buildCompErrorDetails(serviceResp);
        Map<String, CompServiceUnitResponse> compUnits = serviceResp.getCompUnitResults();
        CompServiceUnitResponse unitResult = compUnits.get(className);
        return ClientServiceUserResponse.builder()
                .respStatus(CompServiceRespStatus.COMPILATION_ERROR.getStatusId())
                .message(COMPILATION_ERROR_MESSAGE)
                .className(unitResult.getClassName())
                .srcCode(unitResult.getSrcCode())
                .compErrorDetails(errorDetails)
                .build();
    }

    public static ClientServiceAdminResponse buildCompErrorAdminTaskResponse(CompServiceResponse serviceResp, String solutionSrcCode, String runnerSrcCode) {
        CompErrorDetails errorDetails = buildCompErrorDetails(serviceResp);
        return ClientServiceAdminResponse.builder()
                .respStatus(CompServiceRespStatus.COMPILATION_ERROR.getStatusId())
                .message(COMPILATION_ERROR_MESSAGE)
                .solutionSrcCode(solutionSrcCode)
                .runnerSrcCode(runnerSrcCode)
                .compErrorDetails(errorDetails)
                .build();
    }

    public static ClientServiceUserResponse buildCompOkUserResponse(CompServiceResponse serviceResp, String className) {
        Map<String, CompServiceUnitResponse> compUnits = serviceResp.getCompUnitResults();
        CompServiceUnitResponse unitResult = compUnits.get(className);
        return ClientServiceUserResponse.builder()
                .respStatus(CompServiceRespStatus.SUCCESS.getStatusId())
                .message(COMPILATION_OK_MESSAGE)
                .className(unitResult.getClassName())
                .srcCode(unitResult.getSrcCode())
                .build();
    }

    public static ClientServiceAdminResponse buildCompOkAdminTaskResponse(String solutionSrcCode, String runnerSrcCode) {
        return ClientServiceAdminResponse.builder()
                .respStatus(CompServiceRespStatus.SUCCESS.getStatusId())
                .message(TASK_PUBLISHING_OK_MESSAGE)
                .solutionSrcCode(solutionSrcCode)
                .runnerSrcCode(runnerSrcCode)
                .build();
    }

    private static CompErrorDetails buildCompErrorDetails(CompServiceResponse serviceResp) {
        return CompErrorDetails.builder()
                .compilerErrorCode(serviceResp.getCompilerErrorCode())
                .compilerMessage(serviceResp.getCompilerMessage())
                .errorCodeLine(serviceResp.getErrorCodeLine())
                .errorPosition(serviceResp.getErrorPosition())
                .build();
    }
}

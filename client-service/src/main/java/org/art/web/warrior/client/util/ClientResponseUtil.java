package org.art.web.warrior.client.util;

import org.art.web.warrior.client.dto.*;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.compiler.dto.CompilationResp;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitResp;
import org.art.web.warrior.commons.execution.dto.ExecutionResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskPublicationResp;

import java.util.Map;

import static org.art.web.warrior.client.CommonServiceConstants.*;

public class ClientResponseUtil {

    private ClientResponseUtil() {
    }

    public static ClientServiceUserResponse buildUserTaskEmptyBodyResp(UserCodeCompData clientReqData) {
        return ClientServiceUserResponse.builder()
                .respStatus(ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusId())
                .message(INTERNAL_SERVICE_ERROR_MESSAGE)
                .className(clientReqData.getClassName())
                .srcCode(clientReqData.getSrcCode())
                .build();
    }

    public static ClientServiceAdminResponse buildAdminTaskEmptyBodyResp(AdminTaskCompData clientReqData) {
        return ClientServiceAdminResponse.builder()
                .respStatus(ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusId())
                .message(INTERNAL_SERVICE_ERROR_MESSAGE)
                .solutionSrcCode(clientReqData.getSolutionSrcCode())
                .runnerSrcCode(clientReqData.getRunnerSrcCode())
                .build();
    }

    public static ClientServiceUserResponse buildUserTaskCompilationErrorResp(CompilationResp serviceResp, String className) {
        CompErrorDetails errorDetails = buildCompilationErrorDetails(serviceResp);
        Map<String, CompilationUnitResp> compUnits = serviceResp.getCompUnitResults();
        CompilationUnitResp unitResult = compUnits.get(className);
        return ClientServiceUserResponse.builder()
                .respStatus(ServiceResponseStatus.COMPILATION_ERROR.getStatusId())
                .message(COMPILATION_ERROR_MESSAGE)
                .className(unitResult.getClassName())
                .srcCode(unitResult.getSrcCode())
                .compErrorDetails(errorDetails)
                .build();
    }

    public static ClientServiceAdminResponse buildAdminTaskCompilationErrorResp(CompilationResp serviceResp, String solutionSrcCode, String runnerSrcCode) {
        CompErrorDetails errorDetails = buildCompilationErrorDetails(serviceResp);
        return ClientServiceAdminResponse.builder()
                .respStatus(ServiceResponseStatus.COMPILATION_ERROR.getStatusId())
                .message(COMPILATION_ERROR_MESSAGE)
                .solutionSrcCode(solutionSrcCode)
                .runnerSrcCode(runnerSrcCode)
                .compErrorDetails(errorDetails)
                .build();
    }

    public static ClientServiceUserResponse buildUserTaskCompilationOkResp(CompilationResp serviceResp, String className) {
        Map<String, CompilationUnitResp> compUnits = serviceResp.getCompUnitResults();
        CompilationUnitResp unitResult = compUnits.get(className);
        return ClientServiceUserResponse.builder()
                .respStatus(ServiceResponseStatus.SUCCESS.getStatusId())
                .message(COMPILATION_OK_MESSAGE)
                .className(unitResult.getClassName())
                .srcCode(unitResult.getSrcCode())
                .build();
    }

    public static ClientServiceAdminResponse buildAdminTaskCompilationOkResp(String solutionSrcCode, String runnerSrcCode) {
        return ClientServiceAdminResponse.builder()
                .respStatus(ServiceResponseStatus.SUCCESS.getStatusId())
                .message(TASK_PUBLISHING_OK_MESSAGE)
                .solutionSrcCode(solutionSrcCode)
                .runnerSrcCode(runnerSrcCode)
                .build();
    }

    public static ClientServiceAdminResponse buildTaskServicePublicationResp(CodingTaskPublicationResp codingTaskPublicationResp, AdminTaskCompData clientRequestData) {
        return ClientServiceAdminResponse.builder()
                .respStatus(codingTaskPublicationResp.getRespStatus())
                .message(codingTaskPublicationResp.getMessage())
                .runnerSrcCode(clientRequestData.getRunnerSrcCode())
                .solutionSrcCode(clientRequestData.getSolutionSrcCode())
                .build();
    }

    private static CompErrorDetails buildCompilationErrorDetails(CompilationResp serviceResp) {
        return CompErrorDetails.builder()
                .compilerErrorCode(serviceResp.getCompilerErrorCode())
                .compilerMessage(serviceResp.getCompilerMessage())
                .errorCodeLine(serviceResp.getErrorCodeLine())
                .errorPosition(serviceResp.getErrorPosition())
                .build();
    }

    public static ClientServiceUserResponse buildUserTaskExecutionResp(ExecutionResp execServiceResp, String className, String srcCode) {
        return ClientServiceUserResponse.builder()
                .respStatus(execServiceResp.getRespStatus())
                .message(execServiceResp.getMessage())
                .execMessage(execServiceResp.getFailedTestMessage())
                .className(className)
                .srcCode(srcCode)
                .build();
    }
}

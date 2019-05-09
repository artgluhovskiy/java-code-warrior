package org.art.web.warrior.client.util;

import org.art.web.warrior.client.dto.*;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.compiler.dto.CompilationResp;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitResp;
import org.art.web.warrior.commons.execution.dto.ExecutionResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskPublicationResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskResp;

import java.util.Map;

import static org.art.web.warrior.client.CommonServiceConstants.*;

public class ClientResponseUtil {

    private ClientResponseUtil() {
    }

    public static ClientServiceUserResp buildUserTaskEmptyBodyResp(UserTaskCodeData clientReqData) {
        return ClientServiceUserResp.builder()
                .respStatus(ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusId())
                .message(INTERNAL_SERVICE_ERROR_MESSAGE)
                .className(clientReqData.getClassName())
                .srcCode(clientReqData.getSrcCode())
                .build();
    }

    public static ClientServiceAdminResp buildAdminTaskEmptyBodyResp(AdminTaskPublicationData clientReqData) {
        return ClientServiceAdminResp.builder()
                .respStatus(ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusId())
                .message(INTERNAL_SERVICE_ERROR_MESSAGE)
                .solutionSrcCode(clientReqData.getSolutionSrcCode())
                .runnerSrcCode(clientReqData.getRunnerSrcCode())
                .build();
    }

    public static ClientServiceUserResp buildUserTaskCompilationErrorResp(UserTaskCodeData userCodeData, CompilationResp serviceResp) {
        CompErrorDetails errorDetails = buildCompilationErrorDetails(serviceResp);
        Map<String, CompilationUnitResp> compUnits = serviceResp.getCompUnitResults();
        CompilationUnitResp unitResult = compUnits.get(userCodeData.getClassName());
        return ClientServiceUserResp.builder()
                .respStatus(ServiceResponseStatus.COMPILATION_ERROR.getStatusId())
                .message(COMPILATION_ERROR_MESSAGE)
                .className(unitResult.getClassName())
                .srcCode(unitResult.getSrcCode())
                .compErrorDetails(errorDetails)
                .build();
    }

    public static ClientServiceAdminResp buildAdminTaskCompilationErrorResp(CompilationResp serviceResp, String solutionSrcCode, String runnerSrcCode) {
        CompErrorDetails errorDetails = buildCompilationErrorDetails(serviceResp);
        return ClientServiceAdminResp.builder()
                .respStatus(ServiceResponseStatus.COMPILATION_ERROR.getStatusId())
                .message(COMPILATION_ERROR_MESSAGE)
                .solutionSrcCode(solutionSrcCode)
                .runnerSrcCode(runnerSrcCode)
                .compErrorDetails(errorDetails)
                .build();
    }

    public static ClientServiceUserResp buildUserTaskCompilationOkResp(CompilationResp serviceResp, String className) {
        Map<String, CompilationUnitResp> compUnits = serviceResp.getCompUnitResults();
        CompilationUnitResp unitResult = compUnits.get(className);
        return ClientServiceUserResp.builder()
                .respStatus(ServiceResponseStatus.SUCCESS.getStatusId())
                .message(COMPILATION_OK_MESSAGE)
                .className(unitResult.getClassName())
                .srcCode(unitResult.getSrcCode())
                .build();
    }

    public static ClientServiceAdminResp buildAdminTaskCompilationOkResp(String solutionSrcCode, String runnerSrcCode) {
        return ClientServiceAdminResp.builder()
                .respStatus(ServiceResponseStatus.SUCCESS.getStatusId())
                .message(TASK_PUBLISHING_OK_MESSAGE)
                .solutionSrcCode(solutionSrcCode)
                .runnerSrcCode(runnerSrcCode)
                .build();
    }

    public static ClientServiceAdminResp buildTaskServicePublicationResp(CodingTaskPublicationResp codingTaskPublicationResp, AdminTaskPublicationData clientRequestData) {
        return ClientServiceAdminResp.builder()
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

    public static ClientServiceUserResp buildUserTaskExecutionResp(UserTaskCodeData userTaskCodeData, ExecutionResp execServiceResp) {
        return ClientServiceUserResp.builder()
                .respStatus(execServiceResp.getRespStatus())
                .message(execServiceResp.getMessage())
                .execMessage(execServiceResp.getFailedTestMessage())
                .className(userTaskCodeData.getClassName())
                .srcCode(userTaskCodeData.getSrcCode())
                .build();
    }

    public static ClientServiceUserResp buildUserTaskServiceErrorResp(UserTaskCodeData userTaskData, CodingTaskResp taskServiceResp) {
        ClientServiceUserResp.ClientServiceUserRespBuilder builder = ClientServiceUserResp.builder();
        builder.className(userTaskData.getClassName())
                .srcCode(userTaskData.getSrcCode())
                .message(TASK_EXECUTION_ERROR_MESSAGE);
        if (ServiceResponseStatus.NOT_FOUND.getStatusId().equals(taskServiceResp.getRespStatus())) {
            builder.respStatus(taskServiceResp.getRespStatus())
                    .execMessage(TASK_NOT_FOUND_ERROR_MESSAGE);
        }
        return builder.build();
    }
}

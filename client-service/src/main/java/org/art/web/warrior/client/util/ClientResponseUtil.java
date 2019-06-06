package org.art.web.warrior.client.util;

import org.art.web.warrior.client.dto.*;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitResp;
import org.art.web.warrior.commons.execution.dto.ExecutionResponse;

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


    public static ClientServiceUserResp buildUserTaskCompilationErrorResp(UserTaskCodeData userCodeData, CompilationResponse serviceResp) {
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

    public static ClientServiceUserResp buildUserTaskCompilationOkResp(CompilationResponse serviceResp, String className) {
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

    public static ClientServiceAdminResp buildClientServiceOkResp(TaskServiceResp taskServiceResp, AdminTaskPublicationData requestData) {
        return ClientServiceAdminResp.builder()
                .respStatus(taskServiceResp.getRespStatus())
                .message(taskServiceResp.getMessage())
                .runnerSrcCode(requestData.getRunnerSrcCode())
                .solutionSrcCode(requestData.getSolutionSrcCode())
                .build();
    }

    public static ClientServiceAdminResp buildTaskForUpdateNotExistResp(String taskNameId) {
        return ClientServiceAdminResp.builder()
                .respStatus(ServiceResponseStatus.NOT_FOUND.getStatusId())
                .message("Coding task with such name ID doesn't exist: " + taskNameId + ". Please, publish it firstly!")
                .build();
    }

    public static ClientServiceUserResp buildUserTaskExecutionResp(UserTaskCodeData userTaskCodeData, ExecutionResponse execServiceResp) {
        return ClientServiceUserResp.builder()
                .respStatus(execServiceResp.getRespStatus())
                .message(execServiceResp.getMessage())
                .execMessage(execServiceResp.getFailedTestMessage())
                .className(userTaskCodeData.getClassName())
                .srcCode(userTaskCodeData.getSrcCode())
                .build();
    }

    public static ClientServiceUserResp buildUserTaskServiceErrorResp(UserTaskCodeData userTaskData, TaskServiceResp taskServiceResp) {
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

    public static ClientServiceAdminResp buildCompilationErrorResp(CompilationResponse serviceResp, AdminTaskPublicationData requestData) {
        if (serviceResp == null) {
            return buildTaskServiceErrorResp(requestData);
        } else {
            return buildTaskCompilationErrorResp(serviceResp, requestData);
        }
    }

    private static ClientServiceAdminResp buildTaskServiceErrorResp(AdminTaskPublicationData requestData) {
        return ClientServiceAdminResp.builder()
                .respStatus(ServiceResponseStatus.INTERNAL_SERVICE_ERROR.getStatusId())
                .message(INTERNAL_SERVICE_ERROR_MESSAGE)
                .solutionSrcCode(requestData.getSolutionSrcCode())
                .runnerSrcCode(requestData.getRunnerSrcCode())
                .build();
    }

    private static ClientServiceAdminResp buildTaskCompilationErrorResp(CompilationResponse serviceResp, AdminTaskPublicationData requestData) {
        CompErrorDetails errorDetails = buildCompilationErrorDetails(serviceResp);
        return ClientServiceAdminResp.builder()
                .respStatus(ServiceResponseStatus.COMPILATION_ERROR.getStatusId())
                .message(COMPILATION_ERROR_MESSAGE)
                .solutionSrcCode(requestData.getSolutionSrcCode())
                .runnerSrcCode(requestData.getRunnerSrcCode())
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
}

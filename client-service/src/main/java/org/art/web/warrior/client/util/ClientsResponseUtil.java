package org.art.web.warrior.client.util;

import org.art.web.warrior.client.dto.*;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitResp;
import org.art.web.warrior.commons.execution.dto.ExecutionResponse;
import org.art.web.warrior.commons.tasking.dto.TaskDto;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.art.web.warrior.client.CommonServiceConstants.*;

public class ClientsResponseUtil {

    private ClientsResponseUtil() {
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

    public static ClientServiceResponse buildUserTaskExecutionResp(UserTaskCodeData userTaskCodeData, ExecutionResponse execServiceResp) {
        return ClientServiceResponse.builder()
                .respStatus(execServiceResp.getRespStatus())
                .message(execServiceResp.getMessage())
                .execMessage(execServiceResp.getFailedTestMessage())
                .className(userTaskCodeData.getClassName())
                .srcCode(userTaskCodeData.getSrcCode())
                .build();
    }


}

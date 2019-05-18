package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.ClientServiceUserResp;
import org.art.web.warrior.client.dto.UserTaskCodeData;
import org.art.web.warrior.client.service.api.CompServiceClient;
import org.art.web.warrior.client.service.api.ExecServiceClient;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.client.util.ClientRequestUtil;
import org.art.web.warrior.client.util.ClientResponseUtil;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.compiler.dto.CompilationReq;
import org.art.web.warrior.commons.compiler.dto.CompilationResp;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitReq;
import org.art.web.warrior.commons.execution.dto.ExecutionReq;
import org.art.web.warrior.commons.execution.dto.ExecutionResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static java.util.Collections.singletonList;

@Slf4j
@RestController
@RequestMapping(
        value = "user",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
)
public class UserCodeSubmissionController {

    private final CompServiceClient compServiceClient;

    private final ExecServiceClient execServiceClient;

    private final TaskServiceClient taskServiceClient;

    @Autowired
    public UserCodeSubmissionController(CompServiceClient compServiceClient, ExecServiceClient execServiceClient, TaskServiceClient taskServiceClient) {
        this.compServiceClient = compServiceClient;
        this.execServiceClient = execServiceClient;
        this.taskServiceClient = taskServiceClient;
    }

    @PostMapping(value = "submit")
    public ClientServiceUserResp submitClientCode(@Valid @RequestBody UserTaskCodeData userTaskData) {
        String className = userTaskData.getClassName();
        String srcCode = userTaskData.getSrcCode();
        String taskNameId = userTaskData.getTaskNameId();
        log.debug("Client code submission request: class name {}, source code {}, task name id {}", className, srcCode, taskNameId);
        CompilationUnitReq requestCompData = new CompilationUnitReq(className, srcCode);
        CompilationReq compRequest = new CompilationReq(singletonList(requestCompData));
        CompilationResp compServiceResp = compServiceClient.compileSrc(compRequest);
        if (compServiceResp == null) {
            log.debug("Internal service error occurred! Compilation service responded with empty body.");
            return ClientResponseUtil.buildUserTaskEmptyBodyResp(userTaskData);
        }
        if (compServiceResp.isCompError()) {
            log.debug("Compilation errors occurred while compiling client source code!");
            return ClientResponseUtil.buildUserTaskCompilationErrorResp(userTaskData, compServiceResp);
        }
        CodingTaskResp taskServiceResp = this.taskServiceClient.getCodingTaskByNameId(taskNameId);
        if (!ServiceResponseStatus.SUCCESS.getStatusId().equals(taskServiceResp.getRespStatus())) {
            return ClientResponseUtil.buildUserTaskServiceErrorResp(userTaskData, taskServiceResp);
        }
        ExecutionReq executionReq = ClientRequestUtil.buildExecutionServiceRequest(compServiceResp, taskServiceResp);
        ExecutionResp execServiceResp = this.execServiceClient.executeCode(executionReq);
        return ClientResponseUtil.buildUserTaskExecutionResp(userTaskData, execServiceResp);
    }
}
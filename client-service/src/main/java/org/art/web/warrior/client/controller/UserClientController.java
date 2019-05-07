package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.ClientServiceUserResponse;
import org.art.web.warrior.client.dto.UserCodeCompData;
import org.art.web.warrior.client.service.api.CompServiceClient;
import org.art.web.warrior.client.service.api.ExecServiceClient;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.client.util.ClientRequestUtil;
import org.art.web.warrior.client.util.ClientResponseUtil;
import org.art.web.warrior.commons.compiler.dto.CompilationResp;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitReq;
import org.art.web.warrior.commons.execution.dto.ExecutionReq;
import org.art.web.warrior.commons.execution.dto.ExecutionResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

import static java.util.Collections.singletonList;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserClientController {

    private final CompServiceClient compServiceClient;

    private final ExecServiceClient execServiceClient;

    private final TaskServiceClient taskServiceClient;

    @Autowired
    public UserClientController(CompServiceClient compServiceClient, ExecServiceClient execServiceClient, TaskServiceClient taskServiceClient) {
        this.compServiceClient = compServiceClient;
        this.execServiceClient = execServiceClient;
        this.taskServiceClient = taskServiceClient;
    }

    @ResponseBody
    @PostMapping(value = "submit", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientServiceUserResponse submitClientCode(@Valid @RequestBody UserCodeCompData userCompData) {
        String className = userCompData.getClassName();
        String srcCode = userCompData.getSrcCode();
        String taskNameId = userCompData.getTaskNameId();
        log.debug("Client code submission request: class name {}, source code {}, task name id {}", className, srcCode, taskNameId);
        CompilationUnitReq requestCompData = new CompilationUnitReq(className, srcCode);
        CompilationResp compServiceResp = compServiceClient.compileSrc(singletonList(requestCompData));
        if (compServiceResp == null) {
            log.debug("Internal service error occurred! Compilation service responded with empty body.");
            return ClientResponseUtil.buildUserTaskEmptyBodyResp(userCompData);
        }
        if (compServiceResp.isCompError()) {
            log.debug("Compilation errors occurred while compiling client source code!");
            return ClientResponseUtil.buildUserTaskCompilationErrorResp(compServiceResp, className);
        }
        CodingTaskResp taskServiceResp = this.taskServiceClient.getCodingTaskByNameId(taskNameId);
        ExecutionReq executionReq = ClientRequestUtil.buildExecutionServiceRequest(compServiceResp, taskServiceResp);
        ExecutionResp execServiceResp = this.execServiceClient.executeCode(executionReq);
        return ClientResponseUtil.buildUserTaskExecutionResp(execServiceResp, className, srcCode);
    }
}
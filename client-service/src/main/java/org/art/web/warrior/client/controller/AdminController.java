package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.config.validation.groups.OnPublishing;
import org.art.web.warrior.client.config.validation.groups.OnUpdate;
import org.art.web.warrior.client.dto.AdminTaskPublicationData;
import org.art.web.warrior.client.dto.ClientServiceResponse;
import org.art.web.warrior.client.service.api.CompServiceClient;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.client.util.ClientRequestUtil;
import org.art.web.warrior.client.util.ClientResponseUtil;
import org.art.web.warrior.client.util.ClientsResponseUtil;
import org.art.web.warrior.commons.compiler.dto.CompilationRequest;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.commons.tasking.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("admin")
public class AdminController {

    private final CompServiceClient compServiceClient;

    private final TaskServiceClient taskServiceClient;

    @Autowired
    public AdminController(CompServiceClient compServiceClient, TaskServiceClient taskServiceClient) {
        this.compServiceClient = compServiceClient;
        this.taskServiceClient = taskServiceClient;
    }

    @Validated(OnPublishing.class)
    @PostMapping(value = "submit", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientServiceResponse publishNewCodingTask(@Valid AdminTaskPublicationData requestData) {
        log.info("Publishing a new Coding Task. Task name ID: {}", requestData.getTaskNameId());
        ResponseEntity<CompilationResponse> compServiceResponse = callCompilerService(requestData);
        if (!ClientResponseUtil.isCompServiceOkResponse(compServiceResponse)) {
            return ClientResponseUtil.buildCompServiceErrorResponse(compServiceResponse);
        }


        TaskDto taskDto = ClientRequestUtil.buildTaskServiceReq(requestData, compilationResponse);
        TaskServiceResp taskServiceResp = taskServiceClient.publishCodingTask(taskDto);
        return ClientsResponseUtil.buildClientServiceOkResp(taskServiceResp, requestData);
    }

    @Validated(OnUpdate.class)
    @PutMapping(value = "submit", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientServiceResponse updateCodingTask(@Valid AdminTaskPublicationData requestData) {
        String taskNameId = requestData.getTaskNameId();
        log.info("Updating the Coding Task. Task name ID: {}", taskNameId);
        TaskServiceResp taskServiceResp = taskServiceClient.getCodingTaskByNameId(taskNameId);
        if (taskServiceResp == null || taskServiceResp.getTask() == null) {
            return ClientsResponseUtil.buildTaskForUpdateNotExistResp(taskNameId);
        }

        ResponseEntity<CompilationResponse> compServiceResponse = callCompilerService(requestData);
        if (!ClientResponseUtil.isCompServiceOkResponse(compServiceResponse)) {
            return ClientResponseUtil.buildCompServiceErrorResponse(compServiceResponse);
        }

        TaskDto taskDto = ClientRequestUtil.buildTaskServiceReq(requestData, compilationResponse);
        taskServiceResp = taskServiceClient.updateCodingTask(taskDto);
        return ClientsResponseUtil.buildClientServiceOkResp(taskServiceResp, requestData);

    }

    private ResponseEntity<CompilationResponse> callCompilerService(AdminTaskPublicationData requestData) {
        log.debug("Compiling coding task source data.");
        CompilationRequest compilationRequestData = ClientRequestUtil.buildCompilationServiceReq(requestData);
        return compServiceClient.compileSrc(compilationRequestData);
    }
}

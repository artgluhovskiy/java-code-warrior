package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.config.validation.groups.OnPublishing;
import org.art.web.warrior.client.config.validation.groups.OnUpdate;
import org.art.web.warrior.client.dto.AdminTaskDto;
import org.art.web.warrior.client.dto.ClientServiceResponse;
import org.art.web.warrior.client.service.api.CompServiceClient;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.client.util.ServiceRequestUtil;
import org.art.web.warrior.client.util.ServiceResponseUtil;
import org.art.web.warrior.commons.compiler.dto.CompilationRequest;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitDto;
import org.art.web.warrior.commons.tasking.dto.TaskDescriptorDto;
import org.art.web.warrior.commons.tasking.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static org.art.web.warrior.client.CommonServiceConstants.TASK_PUBLICATION_OK_MESSAGE;
import static org.art.web.warrior.client.CommonServiceConstants.TASK_UPDATE_OK_MESSAGE;
import static org.art.web.warrior.commons.CommonConstants.RUNNER_CLASS_NAME;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final CompServiceClient compServiceClient;

    private final TaskServiceClient taskServiceClient;

    @Autowired
    public AdminController(CompServiceClient compServiceClient, TaskServiceClient taskServiceClient) {
        this.compServiceClient = compServiceClient;
        this.taskServiceClient = taskServiceClient;
    }

    @Validated(OnPublishing.class)
    @PostMapping(value = "/submit", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientServiceResponse publishNewCodingTask(@Valid @RequestBody AdminTaskDto adminTaskData) {
        log.info("Publishing the new Coding Task. Task name ID: {}", adminTaskData.getTaskNameId());
        ResponseEntity<CompilationResponse> compServiceResponse = callCompilerService(adminTaskData);
        if (ServiceResponseUtil.isCompServiceErrorResponse(compServiceResponse)) {
            return ServiceResponseUtil.buildCompServiceErrorResponse(compServiceResponse);
        }
        CompilationResponse compResponse = compServiceResponse.getBody();
        TaskDto taskDto = ServiceRequestUtil.buildTaskServicePublicationRequest(adminTaskData, getRunnerClassDataFromResponse(compResponse));
        ResponseEntity<TaskDescriptorDto> taskServiceResponse = taskServiceClient.publishCodingTask(taskDto);
        if (ServiceResponseUtil.isTaskServiceErrorResponse(taskServiceResponse)) {
            return ServiceResponseUtil.buildTaskServiceErrorResp(taskServiceResponse);
        }
        return ServiceResponseUtil.buildClientServiceOkResp(TASK_PUBLICATION_OK_MESSAGE);
    }

    @Validated(OnUpdate.class)
    @PutMapping(value = "/submit", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientServiceResponse updateCodingTask(@Valid @RequestBody AdminTaskDto adminTaskData) {
        String taskNameId = adminTaskData.getTaskNameId();
        log.info("Updating the Coding Task. Task name ID: {}", taskNameId);
        ResponseEntity<TaskDto> taskServiceResponse = taskServiceClient.getCodingTaskByNameId(taskNameId);
        if (ServiceResponseUtil.isTaskServiceErrorResponse(taskServiceResponse)) {
            return ServiceResponseUtil.buildTaskServiceErrorResp(taskServiceResponse);
        }
        ResponseEntity<CompilationResponse> compServiceResponse = callCompilerService(adminTaskData);
        if (ServiceResponseUtil.isCompServiceErrorResponse(compServiceResponse)) {
            return ServiceResponseUtil.buildCompServiceErrorResponse(compServiceResponse);
        }
        CompilationResponse compResponse = compServiceResponse.getBody();
        TaskDto taskDto = ServiceRequestUtil.buildTaskServicePublicationRequest(adminTaskData, getRunnerClassDataFromResponse(compResponse));
        ResponseEntity<TaskDescriptorDto> taskServiceUpdateResponse = taskServiceClient.updateCodingTask(taskDto);
        if (ServiceResponseUtil.isTaskServiceErrorResponse(taskServiceUpdateResponse)) {
            return ServiceResponseUtil.buildTaskServiceErrorResp(taskServiceUpdateResponse);
        }
        return ServiceResponseUtil.buildClientServiceOkResp(TASK_UPDATE_OK_MESSAGE);
    }

    private ResponseEntity<CompilationResponse> callCompilerService(AdminTaskDto taskData) {
        log.debug("Compiling coding task source data.");
        CompilationRequest compilationRequestData = ServiceRequestUtil.buildCompilationServiceReq(taskData);
        return compServiceClient.compileSrc(compilationRequestData);
    }

    private byte[] getRunnerClassDataFromResponse(CompilationResponse compResponse) {
        Map<String, CompilationUnitDto> compData = compResponse.getCompUnitResults();
        return compData.get(RUNNER_CLASS_NAME).getCompiledClassBytes();
    }
}

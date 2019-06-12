package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.config.validation.groups.OnDelete;
import org.art.web.warrior.client.config.validation.groups.OnPublishOrUpdate;
import org.art.web.warrior.client.dto.AdminTaskDto;
import org.art.web.warrior.client.dto.ClientServiceResponse;
import org.art.web.warrior.client.service.client.api.CompServiceClient;
import org.art.web.warrior.client.service.client.api.TaskServiceClient;
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

import java.util.Map;

import static org.art.web.warrior.client.CommonServiceConstants.*;
import static org.art.web.warrior.commons.CommonConstants.RUNNER_CLASS_NAME;

@Slf4j
@RestController
@RequestMapping("/admin/submit")
public class AdminController {

    private final CompServiceClient compServiceClient;

    private final TaskServiceClient taskServiceClient;

    @Autowired
    public AdminController(CompServiceClient compServiceClient, TaskServiceClient taskServiceClient) {
        this.compServiceClient = compServiceClient;
        this.taskServiceClient = taskServiceClient;
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientServiceResponse publishNewCodingTask(@Validated(OnPublishOrUpdate.class) @RequestBody AdminTaskDto adminTaskData) {
        log.info("Publishing the new Coding Task. Task name ID: {}", adminTaskData.getTaskNameId());
        ResponseEntity<CompilationResponse> compServiceResponse = callCompilationService(adminTaskData);
        if (ServiceResponseUtil.isCompServiceErrorResponse(compServiceResponse)) {
            return ServiceResponseUtil.buildCompServiceErrorResponse(compServiceResponse);
        }
        CompilationResponse compResponse = compServiceResponse.getBody();
        TaskDto taskDto = ServiceRequestUtil.buildTaskServicePublicationRequest(adminTaskData, getRunnerClassDataFromResponse(compResponse));
        ResponseEntity<TaskDescriptorDto> taskServiceResponse = taskServiceClient.publishCodingTask(taskDto);
        if (ServiceResponseUtil.isTaskServiceErrorResponse(taskServiceResponse)) {
            return ServiceResponseUtil.buildTaskServiceErrorResp(taskServiceResponse);
        }
        return ServiceResponseUtil.buildClientServiceOkResponse(TASK_PUBLICATION_OK_MESSAGE);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientServiceResponse updateCodingTask(@Validated(OnPublishOrUpdate.class) @RequestBody AdminTaskDto adminTaskData) {
        String taskNameId = adminTaskData.getTaskNameId();
        log.info("Updating the Coding Task. Task name ID: {}", taskNameId);
        ResponseEntity<TaskDto> taskServiceResponse = taskServiceClient.getCodingTaskByNameId(taskNameId);
        if (ServiceResponseUtil.isTaskServiceErrorResponse(taskServiceResponse)) {
            return ServiceResponseUtil.buildTaskServiceErrorResp(taskServiceResponse);
        }
        ResponseEntity<CompilationResponse> compServiceResponse = callCompilationService(adminTaskData);
        if (ServiceResponseUtil.isCompServiceErrorResponse(compServiceResponse)) {
            return ServiceResponseUtil.buildCompServiceErrorResponse(compServiceResponse);
        }
        CompilationResponse compResponse = compServiceResponse.getBody();
        TaskDto taskDto = ServiceRequestUtil.buildTaskServicePublicationRequest(adminTaskData, getRunnerClassDataFromResponse(compResponse));
        ResponseEntity<TaskDescriptorDto> taskServiceUpdateResponse = taskServiceClient.updateCodingTask(taskDto);
        if (ServiceResponseUtil.isTaskServiceErrorResponse(taskServiceUpdateResponse)) {
            return ServiceResponseUtil.buildTaskServiceErrorResp(taskServiceUpdateResponse);
        }
        return ServiceResponseUtil.buildClientServiceOkResponse(TASK_UPDATE_OK_MESSAGE);
    }

    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientServiceResponse deleteCodingTask(@Validated(OnDelete.class) @RequestBody AdminTaskDto adminTaskData) {
        String taskNameId = adminTaskData.getTaskNameId();
        log.info("Deleting the Coding Task. Task name ID: {}", taskNameId);
        taskServiceClient.deleteTask(taskNameId);
        return ServiceResponseUtil.buildClientServiceOkResponse(TASK_DELETE_OK_MESSAGE);
    }

    private ResponseEntity<CompilationResponse> callCompilationService(AdminTaskDto taskData) {
        log.debug("Compiling coding task source data.");
        CompilationRequest compilationRequestData = ServiceRequestUtil.buildCompilationServiceRequest(taskData);
        return compServiceClient.compileSrc(compilationRequestData);
    }

    private byte[] getRunnerClassDataFromResponse(CompilationResponse compResponse) {
        Map<String, CompilationUnitDto> compData = compResponse.getCompUnitResults();
        return compData.get(RUNNER_CLASS_NAME).getCompiledClassBytes();
    }
}

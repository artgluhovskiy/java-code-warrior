package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.config.validation.groups.OnPublishing;
import org.art.web.warrior.client.config.validation.groups.OnUpdate;
import org.art.web.warrior.client.dto.AdminTaskPublicationData;
import org.art.web.warrior.client.dto.ClientServiceAdminResp;
import org.art.web.warrior.client.service.api.CompServiceClient;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.client.util.ClientRequestUtil;
import org.art.web.warrior.client.util.ClientResponseUtil;
import org.art.web.warrior.commons.compiler.dto.CompServiceReq;
import org.art.web.warrior.commons.compiler.dto.CompServiceResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskDto;
import org.art.web.warrior.commons.tasking.dto.TaskServiceResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    public ClientServiceAdminResp publishNewCodingTask(@Valid AdminTaskPublicationData requestData) {
        log.info("Publishing a new Coding Task. Task name ID: {}", requestData.getTaskNameId());
        CompServiceResp compServiceResp = callCompilerService(requestData);
        if (compServiceResp == null || compServiceResp.hasErrors()) {
            log.debug("Some errors occurred while task src compilation! Request data: {}", requestData);
            return ClientResponseUtil.buildCompilationErrorResp(compServiceResp, requestData);
        }
        CodingTaskDto taskDto = ClientRequestUtil.buildTaskServiceReq(requestData, compServiceResp);
        TaskServiceResp taskServiceResp = taskServiceClient.publishNewCodingTask(taskDto);
        return ClientResponseUtil.buildClientServiceOkResp(taskServiceResp, requestData);
    }

    @Validated(OnUpdate.class)
    @PutMapping(value = "submit", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientServiceAdminResp updateCodingTask(@Valid AdminTaskPublicationData requestData) {
        String taskNameId = requestData.getTaskNameId();
        log.info("Updating the Coding Task. Task name ID: {}", taskNameId);
        TaskServiceResp taskServiceResp = taskServiceClient.getCodingTaskByNameId(taskNameId);
        if (taskServiceResp == null || taskServiceResp.getTask() == null) {
            return ClientResponseUtil.buildTaskForUpdateNotExistResp(taskNameId);
        }
        CompServiceResp compServiceResp = callCompilerService(requestData);
        if (compServiceResp == null || compServiceResp.hasErrors()) {
            log.debug("Some errors occurred while task src compilation! Request data: {}", requestData);
            return ClientResponseUtil.buildCompilationErrorResp(compServiceResp, requestData);
        }
        CodingTaskDto taskDto = ClientRequestUtil.buildTaskServiceReq(requestData, compServiceResp);
        TaskServiceResp taskServiceResp = taskServiceClient.updateCodingTask(taskDto);
        return ClientResponseUtil.buildClientServiceOkResp(taskServiceResp, requestData);

    }

    private CompServiceResp callCompilerService(AdminTaskPublicationData requestData) {
        log.debug("Compiling coding task source data.");
        CompServiceReq compServiceReqData = ClientRequestUtil.buildCompilationServiceReq(requestData);
        return compServiceClient.compileSrc(compServiceReqData);
    }
}

package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.AdminTaskPublicationData;
import org.art.web.warrior.client.dto.ClientServiceAdminResp;
import org.art.web.warrior.client.service.api.CompServiceClient;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.client.util.ClientRequestUtil;
import org.art.web.warrior.client.util.ClientResponseUtil;
import org.art.web.warrior.commons.compiler.dto.CompilationReq;
import org.art.web.warrior.commons.compiler.dto.CompilationResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskPublicationReq;
import org.art.web.warrior.commons.tasking.dto.CodingTaskPublicationResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    private final CompServiceClient compServiceClient;

    private final TaskServiceClient taskServiceClient;

    @Autowired
    public AdminController(CompServiceClient compServiceClient, TaskServiceClient taskServiceClient) {
        this.compServiceClient = compServiceClient;
        this.taskServiceClient = taskServiceClient;
    }

    @ResponseBody
    @PostMapping(value = "submit", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientServiceAdminResp publishNewTask(@Valid @RequestBody AdminTaskPublicationData clientRequestData) {
        log.info("Publishing a new Coding Task. Task name ID: {}", clientRequestData.getTaskNameId());
        String solutionSrcCode = clientRequestData.getSolutionSrcCode();
        String runnerSrcCode = clientRequestData.getRunnerSrcCode();
        log.debug("Compiling class data.");
        CompilationReq compServiceReqData = ClientRequestUtil.buildCompilationServiceReq(clientRequestData);
        CompilationResp serviceResp = compServiceClient.compileSrc(compServiceReqData);
        if (serviceResp == null) {
            log.debug("Internal service error occurred! Compilation service responded with an empty body.");
            return ClientResponseUtil.buildAdminTaskEmptyBodyResp(clientRequestData);
        }
        if (serviceResp.isCompError()) {
            log.debug("Compilation errors occurred while compiling client source code!");
            return ClientResponseUtil.buildAdminTaskCompilationErrorResp(serviceResp, solutionSrcCode, runnerSrcCode);
        }
        CodingTaskPublicationReq taskPublicationReq = ClientRequestUtil.buildTaskServicePublicationReq(clientRequestData, serviceResp);
        CodingTaskPublicationResp taskPublicationResp = taskServiceClient.publishNewCodingTask(taskPublicationReq);
        return ClientResponseUtil.buildTaskServicePublicationResp(taskPublicationResp, clientRequestData);
    }
}

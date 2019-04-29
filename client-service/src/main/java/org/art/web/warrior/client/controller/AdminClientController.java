package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.AdminTaskCompData;
import org.art.web.warrior.client.dto.ClientServiceAdminResponse;
import org.art.web.warrior.commons.tasking.CodingTask;
import org.art.web.warrior.client.service.api.CompServiceClient;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.client.util.ClientRequestUtil;
import org.art.web.warrior.client.util.ClientResponseUtil;
import org.art.web.warrior.commons.compiler.dto.CompServiceResponse;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitRequest;
import org.art.web.warrior.commons.util.ParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminClientController {

    private final CompServiceClient compServiceClient;

    private final TaskServiceClient taskServiceClient;

    @Autowired
    public AdminClientController(CompServiceClient compServiceClient, TaskServiceClient taskServiceClient) {
        this.compServiceClient = compServiceClient;
        this.taskServiceClient = taskServiceClient;
    }

    @ResponseBody
    @PostMapping(value = "submit", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientServiceAdminResponse publishNewTask(@RequestBody AdminTaskCompData clientRequestData) {
        log.info("Publishing New Coding Task: {}", clientRequestData);
        String solutionSrcCode = clientRequestData.getSolutionSrcCode();
        String runnerSrcCode = clientRequestData.getRunnerSrcCode();
        if (!clientRequestData.isValid()) {
            log.debug("Admin task code cannot be processed: solution src code: {}, runner src code: {}", solutionSrcCode, runnerSrcCode);
            return ClientResponseUtil.buildUnprocessableAdminTaskResponse(clientRequestData);
        }
        log.debug("Compiling class data.");
        List<CompServiceUnitRequest> compServiceReqData = prepareCompServiceRequestData(clientRequestData);
        CompServiceResponse serviceResp = compServiceClient.callCompilationService(compServiceReqData);
        if (serviceResp == null) {
            log.debug("Internal service error occurred! Compilation service responded with empty body.");
            return ClientResponseUtil.buildEmptyBodyAdminTaskResponse(clientRequestData);
        }
        if (serviceResp.isCompError()) {
            log.debug("Compilation errors occurred while compiling client source code!");
            return ClientResponseUtil.buildCompErrorAdminTaskResponse(serviceResp, solutionSrcCode, runnerSrcCode);
        }
        CodingTask codingTask = ClientRequestUtil.buildTaskServiceRequest(clientRequestData, serviceResp);
        taskServiceClient.publishNewCodingTask(codingTask);

        return ClientResponseUtil.buildCompOkAdminTaskResponse(solutionSrcCode, runnerSrcCode);
    }

    private List<CompServiceUnitRequest> prepareCompServiceRequestData(AdminTaskCompData requestData) {
        String solutionSrcCode = requestData.getSolutionSrcCode();
        String solutionClassName = ParserUtil.parseClassNameFromSrcString(solutionSrcCode);
        CompServiceUnitRequest solutionCompUnit = new CompServiceUnitRequest(solutionClassName, solutionSrcCode);
        String runnerSrcCode = requestData.getRunnerSrcCode();
        String runnerClassName = ParserUtil.parseClassNameFromSrcString(runnerSrcCode);
        CompServiceUnitRequest runnerCompUnit = new CompServiceUnitRequest(runnerClassName, runnerSrcCode);
        return Arrays.asList(solutionCompUnit, runnerCompUnit);
    }

    @GetMapping
    public String admin() {
        return "admin/admin";
    }
}

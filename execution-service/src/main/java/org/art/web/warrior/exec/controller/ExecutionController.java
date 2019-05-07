package org.art.web.warrior.exec.controller;

import org.art.web.warrior.commons.driver.exception.ClientCodeExecutionException;
import org.art.web.warrior.commons.execution.dto.ExecutionReq;
import org.art.web.warrior.commons.execution.dto.ExecutionResp;
import org.art.web.warrior.exec.domain.CommonMethodDescriptor;
import org.art.web.warrior.exec.domain.api.MethodDescriptor;
import org.art.web.warrior.exec.service.api.MethodInvocationService;
import org.art.web.warrior.exec.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.art.web.warrior.commons.CommonConstants.RUNNER_RUN_METHOD_NAME;
import static org.art.web.warrior.exec.ServiceCommonConstants.EXECUTION_SERVICE_OK_MESSAGE;

@RestController
@RequestMapping("/execute")
public class ExecutionController {

    private final MethodInvocationService invocationService;

    @Autowired
    public ExecutionController(MethodInvocationService invocationService) {
        this.invocationService = invocationService;
    }

    @PostMapping
    public ExecutionResp executeClientCode(@Valid @RequestBody ExecutionReq requestData) {
        Object runnerInstance = ServiceUtil.buildRunnerInstance(requestData);
        MethodDescriptor methodDescriptor = new CommonMethodDescriptor(runnerInstance, RUNNER_RUN_METHOD_NAME);
        try {
            this.invocationService.invokeMethod(methodDescriptor);
            return ServiceUtil.buildExecutionServiceOkResp();
        } catch (ClientCodeExecutionException e) {
            return ServiceUtil.buildExecutionServiceClientCodeErrorResp(e);
        } catch (Exception e) {
            return ServiceUtil.buildExecutionServiceInternalErrorResp(e);
        }
    }

    @GetMapping("/ping")
    public String ping() {
        return EXECUTION_SERVICE_OK_MESSAGE;
    }
}

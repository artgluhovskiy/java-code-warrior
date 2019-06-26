package org.art.web.warrior.exec.controller;

import org.art.web.warrior.commons.execution.dto.ExecutionRequest;
import org.art.web.warrior.commons.execution.dto.ExecutionResponse;
import org.art.web.warrior.exec.domain.CommonMethodDescriptor;
import org.art.web.warrior.exec.domain.api.MethodDescriptor;
import org.art.web.warrior.exec.service.api.MethodInvocationService;
import org.art.web.warrior.exec.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.art.web.warrior.commons.CommonConstants.KRYO_CONTENT_TYPE;
import static org.art.web.warrior.commons.CommonConstants.RUNNER_RUN_METHOD_NAME;

@RestController
@RequestMapping("/execute")
public class ExecutionController {

    private final MethodInvocationService invocationService;

    @Autowired
    public ExecutionController(MethodInvocationService invocationService) {
        this.invocationService = invocationService;
    }

    @PostMapping(consumes = KRYO_CONTENT_TYPE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ExecutionResponse executeClientCode(@Valid @RequestBody ExecutionRequest requestData) {
        Object runnerInstance = ServiceUtil.buildRunnerInstance(requestData);
        MethodDescriptor methodDescriptor = new CommonMethodDescriptor(runnerInstance, RUNNER_RUN_METHOD_NAME);
        this.invocationService.invokeMethod(methodDescriptor);
        return ServiceUtil.buildExecutionServiceOkResp();
    }
}

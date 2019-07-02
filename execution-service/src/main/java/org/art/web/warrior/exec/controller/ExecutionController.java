package org.art.web.warrior.exec.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.commons.execution.dto.ExecutionRequest;
import org.art.web.warrior.commons.execution.dto.ExecutionResponse;
import org.art.web.warrior.exec.domain.CommonMethodDescriptor;
import org.art.web.warrior.exec.domain.api.MethodDescriptor;
import org.art.web.warrior.exec.service.api.MethodInvocationService;
import org.art.web.warrior.exec.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.art.web.warrior.commons.CommonConstants.*;

@Slf4j
@Controller
@RequestMapping("/execute")
public class ExecutionController {

    private final Environment env;

    private final MethodInvocationService invocationService;

    @Autowired
    public ExecutionController(Environment env, MethodInvocationService invocationService) {
        this.env = env;
        this.invocationService = invocationService;
    }

    @ResponseBody
    @PostMapping(consumes = KRYO_CONTENT_TYPE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ExecutionResponse executeClientCode(@Valid @RequestBody ExecutionRequest requestData) {
        log.debug("Making the execution request. Execution request data: {}", requestData);
        Object runnerInstance = ServiceUtil.buildRunnerInstance(requestData);
        MethodDescriptor methodDescriptor = new CommonMethodDescriptor(runnerInstance, RUNNER_RUN_METHOD_NAME);
        this.invocationService.invokeMethod(methodDescriptor);
        return ServiceUtil.buildExecutionServiceOkResp();
    }

    @ResponseBody
    @GetMapping
    public ExecutionResponse getServiceInfo() {
        log.debug("Making the info request");
        if (4 > 3) {
            throw new RuntimeException("Oh, shit!");
        }
        String localPort = env.getProperty(SPRING_LOCAL_PORT_PROP_NAME);
        String appName = env.getProperty(SPRING_APPLICATION_NAME_PROP_NAME);
        String appInfo = env.getProperty(SPRING_INFO_APP_PROP_NAME);
        return ServiceUtil.buildExecutionServiceInfoResp(localPort, appName, appInfo);
    }
}

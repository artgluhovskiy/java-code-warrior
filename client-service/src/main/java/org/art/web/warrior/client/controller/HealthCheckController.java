package org.art.web.warrior.client.controller;

import org.art.web.warrior.client.service.client.api.CompServiceClient;
import org.art.web.warrior.client.service.client.api.ExecServiceClient;
import org.art.web.warrior.client.service.client.feign.CompServiceFeignClient;
import org.art.web.warrior.client.service.client.feign.ExecServiceFeignClient;
import org.art.web.warrior.client.service.client.feign.TaskServiceFeignClient;
import org.art.web.warrior.client.service.client.feign.UserServiceFeignClient;
import org.art.web.warrior.commons.CommonConstants;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.commons.execution.dto.ExecutionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.art.web.warrior.client.CommonServiceConstants.*;

@RestController
@RequestMapping("/admin/health")
public class HealthCheckController {

    private static final String INSTANCES_PREF = "Instances: ";

    private DiscoveryClient discoveryClient;

    private final UserServiceFeignClient userServiceFeignClient;

    private final ExecServiceClient execServiceRestTempClient;

    private final ExecServiceFeignClient execServiceFeignClient;

    private final CompServiceClient compServiceRestTempClient;

    private final CompServiceFeignClient compServiceFeignClient;

    private final TaskServiceFeignClient taskServiceFeignClient;

    @Autowired
    public HealthCheckController(UserServiceFeignClient userServiceFeignClient,
                                 ExecServiceClient execServiceRestTempClient,
                                 ExecServiceFeignClient execServiceFeignClient,
                                 CompServiceClient compServiceRestTempClient,
                                 CompServiceFeignClient compServiceFeignClient,
                                 TaskServiceFeignClient taskServiceFeignClient,
                                 DiscoveryClient discoveryClient) {
        this.userServiceFeignClient = userServiceFeignClient;
        this.execServiceRestTempClient = execServiceRestTempClient;
        this.execServiceFeignClient = execServiceFeignClient;
        this.compServiceRestTempClient = compServiceRestTempClient;
        this.compServiceFeignClient = compServiceFeignClient;
        this.taskServiceFeignClient = taskServiceFeignClient;
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/users")
    public String pingUserService() {
        String message = "Ping message from the Users Service: ";
        message = message + userServiceFeignClient.getPingMessage() + CommonConstants.NEW_LINE;
        message = message + INSTANCES_PREF + stringifyInstances(USER_SERVICE_NAME);
        return message;
    }

    @GetMapping("/exec")
    public String pingExecService() {
        String message = "Ping message from the Execution Service: ";
        message = message + execServiceFeignClient.getPingMessage() + CommonConstants.NEW_LINE;
        message = message + INSTANCES_PREF + stringifyInstances(EXECUTION_SERVICE_NAME);
        return message;
    }

    @GetMapping("/exec/info")
    public ExecutionResponse getExecServiceInfo() {
        return execServiceRestTempClient.getServiceInfo();
    }

    @GetMapping("/comp")
    public String pingCompService() {
        String message = "Ping message from the Compiler Service: ";
        message = message + compServiceFeignClient.getPingMessage() + CommonConstants.NEW_LINE;
        message = message + INSTANCES_PREF + stringifyInstances(COMPILER_SERVICE_NAME);
        return message;
    }

    @GetMapping("/comp/info")
    public CompilationResponse getCompServiceInfo() {
        return compServiceRestTempClient.getServiceInfo();
    }

    @GetMapping("/tasks")
    public String pingTaskService() {
        String message = "Ping message from the Tasks Service: ";
        message = message + taskServiceFeignClient.getPingMessage() + CommonConstants.NEW_LINE;
        message = message + INSTANCES_PREF + stringifyInstances(TASK_SERVICE_NAME);
        return message;
    }

    private String stringifyInstances(String serviceName) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        return instances.stream()
                .map(ServiceInstance::getInstanceId)
                .collect(joining(", "));
    }
}

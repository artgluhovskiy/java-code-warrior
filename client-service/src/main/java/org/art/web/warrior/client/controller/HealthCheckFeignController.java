package org.art.web.warrior.client.controller;

import org.art.web.warrior.client.service.client.feign.CompServiceFeignClient;
import org.art.web.warrior.client.service.client.feign.ExecServiceFeignClient;
import org.art.web.warrior.client.service.client.feign.TaskServiceFeignClient;
import org.art.web.warrior.client.service.client.feign.UserServiceFeignClient;
import org.art.web.warrior.commons.CommonConstants;
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
public class HealthCheckFeignController {

    private static final String INSTANCES_PREF = "Instances: ";

    private DiscoveryClient discoveryClient;

    private final UserServiceFeignClient userServiceClient;

    private final ExecServiceFeignClient execServiceClient;

    private final CompServiceFeignClient compServiceClient;

    private final TaskServiceFeignClient taskServiceClient;

    @Autowired
    public HealthCheckFeignController(UserServiceFeignClient userServiceClient,
                                      ExecServiceFeignClient execServiceClient,
                                      CompServiceFeignClient compServiceClient,
                                      TaskServiceFeignClient taskServiceClient,
                                      DiscoveryClient discoveryClient) {
        this.userServiceClient = userServiceClient;
        this.execServiceClient = execServiceClient;
        this.compServiceClient = compServiceClient;
        this.taskServiceClient = taskServiceClient;
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/users")
    public String pingUserService() {
        String message = "Ping message from the Users Service: ";
        message = message + userServiceClient.getPingMessage() + CommonConstants.NEW_LINE;
        message = message + INSTANCES_PREF + stringifyInstances(USER_SERVICE_NAME);
        return message;
    }

    @GetMapping("/exec")
    public String pingExecService() {
        String message = "Ping message from the Execution Service: ";
        message = message + execServiceClient.getPingMessage() + CommonConstants.NEW_LINE;
        message = message + INSTANCES_PREF + stringifyInstances(EXECUTION_SERVICE_NAME);
        return message;
    }

    @GetMapping("/comp")
    public String pingCompService() {
        String message = "Ping message from the Compiler Service: ";
        message = message + compServiceClient.getPingMessage() + CommonConstants.NEW_LINE;
        message = message + INSTANCES_PREF + stringifyInstances(COMPILER_SERVICE_NAME);
        return message;
    }

    @GetMapping("/tasks")
    public String pingTaskService() {
        String message = "Ping message from the Tasks Service: ";
        message = message + taskServiceClient.getPingMessage() + CommonConstants.NEW_LINE;
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

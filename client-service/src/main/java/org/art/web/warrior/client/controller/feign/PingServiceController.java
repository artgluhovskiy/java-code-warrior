package org.art.web.warrior.client.controller.feign;

import org.art.web.warrior.client.service.client.feign.CompServiceFeignClient;
import org.art.web.warrior.client.service.client.feign.ExecServiceFeignClient;
import org.art.web.warrior.client.service.client.feign.TaskServiceFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/ping")
public class PingServiceController {

    private final ExecServiceFeignClient execServiceClient;

    private final CompServiceFeignClient compServiceClient;

    private final TaskServiceFeignClient taskServiceClient;

    @Autowired
    public PingServiceController(ExecServiceFeignClient execServiceClient, CompServiceFeignClient compServiceClient, TaskServiceFeignClient taskServiceClient) {
        this.execServiceClient = execServiceClient;
        this.compServiceClient = compServiceClient;
        this.taskServiceClient = taskServiceClient;
    }

    @GetMapping("/exec")
    public String pingExecService() {
        return "Ping message from the Execution Service: " + execServiceClient.getPingMessage();
    }

    @GetMapping("/comp")
    public String pingCompService() {
        return "Ping message from the Compiler Service: " + compServiceClient.getPingMessage();
    }

    @GetMapping("/task")
    public String pingTaskService() {
        return "Ping message from the Task Service: " + taskServiceClient.getPingMessage();
    }
}

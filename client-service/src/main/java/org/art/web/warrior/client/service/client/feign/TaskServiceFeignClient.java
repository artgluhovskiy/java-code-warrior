package org.art.web.warrior.client.service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("task-service")
public interface TaskServiceFeignClient {

    @GetMapping("/tasking/tasks/ping")
    String getPingMessage();
}

package org.art.web.warrior.client.service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import static org.art.web.warrior.client.CommonServiceConstants.TASK_SERVICE_NAME;

@FeignClient(TASK_SERVICE_NAME)
public interface TaskServiceFeignClient {

    @GetMapping("/tasking/tasks/ping")
    String getPingMessage();
}

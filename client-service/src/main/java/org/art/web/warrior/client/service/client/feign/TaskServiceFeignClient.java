package org.art.web.warrior.client.service.client.feign;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import static org.art.web.warrior.client.CommonServiceConstants.TASK_SERVICE_NAME;

@FeignClient(TASK_SERVICE_NAME)
@RibbonClient(TASK_SERVICE_NAME)
public interface TaskServiceFeignClient {

    @GetMapping("/actuator/health")
    String getPingMessage();
}

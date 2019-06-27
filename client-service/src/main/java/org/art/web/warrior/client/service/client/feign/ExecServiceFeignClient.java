package org.art.web.warrior.client.service.client.feign;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import static org.art.web.warrior.client.CommonServiceConstants.EXECUTION_SERVICE_NAME;

@FeignClient(EXECUTION_SERVICE_NAME)
@RibbonClient(EXECUTION_SERVICE_NAME)
public interface ExecServiceFeignClient {

    @GetMapping("/executor/actuator/health")
    String getPingMessage();
}

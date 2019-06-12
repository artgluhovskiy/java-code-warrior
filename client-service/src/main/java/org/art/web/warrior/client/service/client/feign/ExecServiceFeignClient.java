package org.art.web.warrior.client.service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("executor-service")
public interface ExecServiceFeignClient {

    @GetMapping("/executor/execute/ping")
    String getPingMessage();
}

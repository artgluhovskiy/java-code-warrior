package org.art.web.warrior.client.service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("compiler-service")
public interface CompServiceFeignClient {

    @GetMapping("/compiler/compile/ping")
    String getPingMessage();
}

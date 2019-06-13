package org.art.web.warrior.client.service.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import static org.art.web.warrior.client.CommonServiceConstants.COMPILER_SERVICE_NAME;

@FeignClient(COMPILER_SERVICE_NAME)
public interface CompServiceFeignClient {

    @GetMapping("/compiler/compile/ping")
    String getPingMessage();
}

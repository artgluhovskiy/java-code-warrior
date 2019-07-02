package org.art.web.warrior.client;

import org.art.web.warrior.client.config.cloud.RibbonConfig;
import org.art.web.warrior.client.config.properties.ServiceConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

import static org.art.web.warrior.client.CommonServiceConstants.*;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@RibbonClients(value = {
        @RibbonClient(name = USER_SERVICE_NAME),
        @RibbonClient(name = TASK_SERVICE_NAME),
        @RibbonClient(name = COMPILER_SERVICE_NAME),
        @RibbonClient(name = EXECUTION_SERVICE_NAME)},
        defaultConfiguration = RibbonConfig.class)
@EnableCircuitBreaker
@EnableConfigurationProperties(ServiceConfigProperties.class)
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }
}

package org.art.web.warrior.client;

import org.art.web.warrior.client.config.converter.KryoHttpMessageConverter;
import org.art.web.warrior.client.config.exchandler.CustomRestTemplateErrorHandler;
import org.art.web.warrior.client.config.interceptor.RequestLogger;
import org.art.web.warrior.client.config.properties.ServiceConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(ServiceConfigProperties.class)
public class ClientApplication {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Bean
    public RequestLogger requestLogger() {
        return new RequestLogger();
    }

    @Bean
    public RestTemplate restTemplate() {
        return restTemplateBuilder
            .additionalInterceptors(requestLogger())
            .additionalMessageConverters(new KryoHttpMessageConverter())
            .errorHandler(new CustomRestTemplateErrorHandler())
            .build();
    }
}

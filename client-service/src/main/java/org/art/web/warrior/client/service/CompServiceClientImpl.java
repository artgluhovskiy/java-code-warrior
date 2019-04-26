package org.art.web.warrior.client.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.client.config.converter.KryoHttpMessageConverter;
import org.art.web.warrior.client.config.interceptor.RequestProcessingLogger;
import org.art.web.warrior.client.service.api.CompServiceClient;
import org.art.web.warrior.commons.compiler.dto.CompServiceResponse;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.art.web.warrior.client.CommonServiceConstants.*;
import static org.art.web.warrior.commons.CommonConstants.*;

@Slf4j
@Service
public class CompServiceClientImpl implements CompServiceClient {

    private Environment env;

    private RestTemplate restTemplate;

    @Autowired
    public CompServiceClientImpl(Environment env, RestTemplateBuilder restTemplateBuilder) {
        this.env = env;
        this.restTemplate = restTemplateBuilder
                .additionalInterceptors(new RequestProcessingLogger())
                .additionalMessageConverters(new KryoHttpMessageConverter())
                .build();
    }

    public CompServiceResponse callCompilationService(List<CompServiceUnitRequest> compRequestData) {
        String compServiceEndpoint = getCompServiceEndpoint();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.set(HttpHeaders.ACCEPT, KRYO_CONTENT_TYPE);
        HttpEntity<List<CompServiceUnitRequest>> reqEntity = new HttpEntity<>(compRequestData, headers);
        log.debug("Making the request to the Compilation service. Endpoint: {}, request data: {}", compServiceEndpoint, compRequestData);
        ResponseEntity<CompServiceResponse> compServiceResponse = restTemplate.postForEntity(compServiceEndpoint, reqEntity, CompServiceResponse.class);
        return compServiceResponse.getBody();
    }

    private String getCompServiceEndpoint() {
        String activeProfile = env.getProperty(SPRING_ACTIVE_PROFILE_ENV_PROP_NAME);
        if (StringUtils.isNotBlank(activeProfile) && ACTIVE_PROFILE_CONTAINER.equals(activeProfile)) {
            String compHostName = env.getProperty(COMPILER_SERVICE_HOST_ENV_PROP_NAME);
            String compHostPort = env.getProperty(COMPILER_SERVICE_PORT_ENV_PROP_NAME);
            return COMPILATION_SERVICE_ENDPOINT_FORMAT.format(new Object[]{compHostName, compHostPort});
        } else {
            return COMPILATION_SERVICE_ENDPOINT_FORMAT.format(new Object[]{LOCALHOST, COMP_SERVICE_PORT_NO_PROFILE});
        }
    }
}

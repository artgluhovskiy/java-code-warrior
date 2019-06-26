package org.art.web.warrior.client.service.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.client.service.client.api.CompServiceClient;
import org.art.web.warrior.commons.compiler.dto.CompilationRequest;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.art.web.warrior.client.CommonServiceConstants.*;
import static org.art.web.warrior.commons.CommonConstants.*;

@Slf4j
@Service
public class CompServiceClientImpl implements CompServiceClient {

    private Environment env;

    private RestTemplate restTemplate;

    private String serviceEndpointBase;

    @Autowired
    public CompServiceClientImpl(Environment env, RestTemplate restTemplate) {
        this.env = env;
        this.restTemplate = restTemplate;
        this.serviceEndpointBase = getServiceEndpointBase();
    }

    @Override
    public CompilationResponse compileSrc(CompilationRequest compRequestData) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.set(HttpHeaders.ACCEPT, KRYO_CONTENT_TYPE);
        HttpEntity<CompilationRequest> reqEntity = new HttpEntity<>(compRequestData, headers);
        log.debug("Making the request to the Compilation service. Endpoint: {}, request data: {}", serviceEndpointBase, compRequestData);
        return restTemplate.postForEntity(serviceEndpointBase, reqEntity, CompilationResponse.class).getBody();
    }

    private String getServiceEndpointBase() {
        String activeProfile = env.getProperty(SPRING_ACTIVE_PROFILE_ENV_PROP_NAME);
        if (StringUtils.isNotBlank(activeProfile) && ACTIVE_PROFILE_CONTAINER.equals(activeProfile)) {
            String compServiceHostName = env.getProperty(COMPILER_SERVICE_HOST_ENV_PROP_NAME);
            String compServiceHostPort = env.getProperty(COMPILER_SERVICE_PORT_ENV_PROP_NAME);
            return COMPILATION_SERVICE_ENDPOINT_FORMAT.format(new Object[]{compServiceHostName, compServiceHostPort});
        } else {
            return COMPILATION_SERVICE_ENDPOINT_FORMAT.format(new Object[]{LOCALHOST, COMP_SERVICE_PORT_NO_PROFILE});
        }
    }
}

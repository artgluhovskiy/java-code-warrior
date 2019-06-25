package org.art.web.warrior.client.service.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.client.service.client.api.ExecServiceClient;
import org.art.web.warrior.commons.execution.dto.ExecutionRequest;
import org.art.web.warrior.commons.execution.dto.ExecutionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.art.web.warrior.client.CommonServiceConstants.*;
import static org.art.web.warrior.commons.CommonConstants.*;

@Slf4j
@Service
public class ExecServiceClientImpl implements ExecServiceClient {

    private Environment env;

    private RestTemplate restTemplate;

    private String serviceEndpointBase;

    @Autowired
    public ExecServiceClientImpl(Environment env, RestTemplate restTemplate) {
        this.env = env;
        this.restTemplate = restTemplate;
        this.serviceEndpointBase = getServiceEndpointBase();
    }

    @Override
    public ExecutionResponse executeCode(ExecutionRequest execRequestData) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE);
        HttpEntity<ExecutionRequest> reqEntity = new HttpEntity<>(execRequestData, headers);
        log.debug("Making the request to the Executor service. Request data: {}, endpoint: {}", execRequestData, serviceEndpointBase);
        return restTemplate.postForEntity(serviceEndpointBase, reqEntity, ExecutionResponse.class).getBody();
    }

    private String getServiceEndpointBase() {
        String activeProfile = env.getProperty(SPRING_ACTIVE_PROFILE_ENV_PROP_NAME);
        if (StringUtils.isNotBlank(activeProfile) && ACTIVE_PROFILE_CONTAINER.equals(activeProfile)) {
            String execHostName = env.getProperty(EXECUTION_SERVICE_HOST_ENV_PROP_NAME);
            String execHostPort = env.getProperty(EXECUTION_SERVICE_PORT_ENV_PROP_NAME);
            return EXECUTION_SERVICE_ENDPOINT_FORMAT.format(new Object[]{execHostName, execHostPort});
        } else {
            return EXECUTION_SERVICE_ENDPOINT_FORMAT.format(new Object[]{LOCALHOST, EXEC_SERVICE_PORT_NO_PROFILE});
        }
    }
}

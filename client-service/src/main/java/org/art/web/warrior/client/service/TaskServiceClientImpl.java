package org.art.web.warrior.client.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.client.config.converter.KryoHttpMessageConverter;
import org.art.web.warrior.client.config.interceptor.RequestProcessingLogger;
import org.art.web.warrior.client.dto.ExecServiceResponse;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.commons.tasking.dto.TaskServicePubRequest;
import org.art.web.warrior.commons.tasking.dto.TaskServicePubResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.art.web.warrior.client.CommonServiceConstants.*;
import static org.art.web.warrior.commons.CommonConstants.*;

@Slf4j
@Service
public class TaskServiceClientImpl implements TaskServiceClient {

    private Environment env;

    private RestTemplate restTemplate;

    @Autowired
    public TaskServiceClientImpl(Environment env, RestTemplateBuilder restTemplateBuilder) {
        this.env = env;
        this.restTemplate = restTemplateBuilder
                .additionalInterceptors(new RequestProcessingLogger())
                .additionalMessageConverters(new KryoHttpMessageConverter())
                .build();
    }

    @Override
    public TaskServicePubResponse publishNewCodingTask(TaskServicePubRequest taskData) {
        String taskServiceEndpoint = getExecServiceEndpoint();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE);
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<TaskServicePubRequest> reqEntity = new HttpEntity<>(taskData, headers);
        log.debug("Making the request to the Task service. Endpoint: {}, request data: {}", taskServiceEndpoint, taskData);
        ResponseEntity<TaskServicePubResponse> taskServiceResponse = restTemplate.postForEntity(taskServiceEndpoint, reqEntity, TaskServicePubResponse.class);
        return taskServiceResponse.getBody();
    }

    private String getExecServiceEndpoint() {
        String activeProfile = env.getProperty(SPRING_ACTIVE_PROFILE_ENV_PROP_NAME);
        if (StringUtils.isNotBlank(activeProfile) && ACTIVE_PROFILE_CONTAINER.equals(activeProfile)) {
            String taskServiceHostName = env.getProperty(TASK_SERVICE_HOST_ENV_PROP_NAME);
            String taskServiceHostPort = env.getProperty(TASK_SERVICE_PORT_ENV_PROP_NAME);
            return TASK_SERVICE_ENDPOINT_FORMAT.format(new Object[]{taskServiceHostName, taskServiceHostPort});
        } else {
            return TASK_SERVICE_ENDPOINT_FORMAT.format(new Object[]{LOCALHOST, TASK_SERVICE_PORT_NO_PROFILE});
        }
    }
}

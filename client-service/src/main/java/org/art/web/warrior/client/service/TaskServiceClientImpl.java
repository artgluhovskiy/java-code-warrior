package org.art.web.warrior.client.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.commons.tasking.dto.TaskDescriptorDto;
import org.art.web.warrior.commons.tasking.dto.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.art.web.warrior.client.CommonServiceConstants.*;
import static org.art.web.warrior.commons.CommonConstants.*;

@Slf4j
@Service
public class TaskServiceClientImpl implements TaskServiceClient {

    private static final String DESCRIPTORS = "descriptors";

    private Environment env;

    private RestTemplate restTemplate;

    private String serviceEndpointBase;

    @Autowired
    public TaskServiceClientImpl(Environment env, RestTemplate restTemplate) {
        this.env = env;
        this.restTemplate = restTemplate;
        this.serviceEndpointBase = getServiceEndpointBase();
    }

    @Override
    public ResponseEntity<TaskDescriptorDto> publishCodingTask(TaskDto task) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE);
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<TaskDto> reqEntity = new HttpEntity<>(task, headers);
        log.debug("Making task publication request to the Task Service. Request data: {}, endpoint: {}", task, serviceEndpointBase);
        return restTemplate.postForEntity(serviceEndpointBase, reqEntity, TaskDescriptorDto.class);
    }

    @Override
    public ResponseEntity<TaskDto> getCodingTaskByNameId(String nameId) {
        String serviceEndpoint = serviceEndpointBase + SLASH_CH + nameId;
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, KRYO_CONTENT_TYPE);
        HttpEntity<?> reqEntity = new HttpEntity<>(headers);
        log.debug("Making the request for the task by name id to the Task Service. Task name id: {}, endpoint: {}", nameId, serviceEndpoint);
        return restTemplate.exchange(serviceEndpoint, HttpMethod.GET, reqEntity, TaskDto.class);
    }

    @Override
    public ResponseEntity<TaskDescriptorDto> updateCodingTask(TaskDto task) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE);
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<?> reqEntity = new HttpEntity<>(task, headers);
        log.debug("Making the request for the task update to the Task Service. Request data: {}, endpoint: {}", task, serviceEndpointBase);
        return restTemplate.exchange(serviceEndpointBase, HttpMethod.PUT, reqEntity, TaskDescriptorDto.class);
    }

    @Override
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, KRYO_CONTENT_TYPE);
        HttpEntity<?> reqEntity = new HttpEntity<>(headers);
        log.debug("Making the request for all tasks to the Task Service. Endpoint: {}", serviceEndpointBase);
        return restTemplate.exchange(serviceEndpointBase, HttpMethod.GET, reqEntity, new ParameterizedTypeReference<List<TaskDto>>() {
        });
    }

    @Override
    public ResponseEntity<List<TaskDescriptorDto>> getCodingTaskDescriptors() {
        String serviceEndpoint = this.serviceEndpointBase + SLASH_CH + DESCRIPTORS;
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<?> reqEntity = new HttpEntity<>(headers);
        log.debug("Making the request for coding task descriptors to the Task Service. Endpoint: {}", serviceEndpoint);
        return restTemplate.exchange(serviceEndpoint, HttpMethod.GET, reqEntity, new ParameterizedTypeReference<List<TaskDescriptorDto>>() {
        });
    }

    @Override
    public void deleteTask(String nameId) {
        String serviceEndpoint = serviceEndpointBase + SLASH_CH + nameId;
        log.debug("Making the request for coding task deletion to the Task Service. Task name id: {}, endpoint: {}", nameId, serviceEndpoint);
        restTemplate.delete(serviceEndpoint);
    }

    private String getServiceEndpointBase() {
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

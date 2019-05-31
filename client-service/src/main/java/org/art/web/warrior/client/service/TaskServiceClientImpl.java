package org.art.web.warrior.client.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.commons.tasking.dto.CodingTaskDescriptorsResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskPublicationReq;
import org.art.web.warrior.commons.tasking.dto.CodingTaskPublicationResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.art.web.warrior.client.CommonServiceConstants.*;
import static org.art.web.warrior.commons.CommonConstants.*;

@Slf4j
@Service
public class TaskServiceClientImpl implements TaskServiceClient {

    private static final String TASK = "task";
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
    public CodingTaskPublicationResp publishNewCodingTask(CodingTaskPublicationReq taskData) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE);
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<CodingTaskPublicationReq> reqEntity = new HttpEntity<>(taskData, headers);
        log.debug("Making task publication request to the Task Service. Endpoint: {}, request data: {}", this.serviceEndpointBase + TASK, taskData);
        ResponseEntity<CodingTaskPublicationResp> serviceResponse = restTemplate.postForEntity(this.serviceEndpointBase, reqEntity, CodingTaskPublicationResp.class);
        return serviceResponse.getBody();
    }

    @Override
    public CodingTaskResp getCodingTaskByNameId(String nameId) {
        String serviceEndpoint = this.serviceEndpointBase + TASK + SLASH_CH + nameId;
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, KRYO_CONTENT_TYPE);
        HttpEntity reqEntity = new HttpEntity<>(headers);
        log.debug("Making the request for the task by name id to the Task Service. Endpoint: {}", serviceEndpoint);
        ResponseEntity<CodingTaskResp> serviceResponse = restTemplate.exchange(serviceEndpoint, HttpMethod.GET, reqEntity, CodingTaskResp.class);
        return serviceResponse.getBody();
    }

    @Override
    public CodingTaskDescriptorsResp getCodingTaskDescriptors() {
        String serviceEndpoint = this.serviceEndpointBase + TASK + SLASH_CH + DESCRIPTORS;
        log.debug("Making the request for coding task descriptors to the Task Service. Endpoint: {}", serviceEndpoint);
        ResponseEntity<CodingTaskDescriptorsResp> serviceResponse = restTemplate.getForEntity(serviceEndpoint, CodingTaskDescriptorsResp.class);
        return serviceResponse.getBody();
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

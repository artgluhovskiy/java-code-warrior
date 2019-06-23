package org.art.web.warrior.client.service.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.commons.users.dto.TaskOrderDto;
import org.art.web.warrior.commons.users.dto.UserDto;
import org.art.web.warrior.client.exception.EmailExistsException;
import org.art.web.warrior.client.service.client.api.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserServiceClientImpl implements UserServiceClient {

    private Environment env;

    private RestTemplate restTemplate;

    private String serviceEndpointBase;

    @Autowired
    public UserServiceClientImpl(Environment env, RestTemplate restTemplate) {
        this.env = env;
        this.restTemplate = restTemplate;
        this.serviceEndpointBase = getServiceEndpointBase();
    }

    @Override
    public ResponseEntity<UserDto> registerNewUserAccount(UserDto userDto) throws EmailExistsException {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<UserDto> reqEntity = new HttpEntity<>(userDto, headers);
        log.debug("Making user registration request to the User Service. Request data: {}, endpoint: {}", userDto, serviceEndpointBase);
        return restTemplate.postForEntity(serviceEndpointBase, reqEntity, UserDto.class);
    }

    @Override
    public ResponseEntity<UserDto> findUserByEmail(String email) {
        String serviceEndpoint = serviceEndpointBase + SLASH_CH + email;
        log.debug("Making the request to the User Service for the user by its email. User email: {}, endpoint: {}", email, serviceEndpoint);
        return restTemplate.getForEntity(serviceEndpointBase, UserDto.class);
    }

    @Override
    public void updateUser(UserDto userDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<UserDto> reqEntity = new HttpEntity<>(userDto, headers);
        log.debug("Making the request to the User Service for user update. Request data: {}, endpoint: {}", userDto, serviceEndpointBase);
        restTemplate.put(serviceEndpointBase, reqEntity);
    }

    @Override
    public void addTaskOrder(String email, TaskOrderDto taskOrderDto) {
        String serviceEndpoint = serviceEndpointBase + SLASH_CH + email;
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        HttpEntity<TaskOrderDto> reqEntity = new HttpEntity<>(taskOrderDto, headers);
        log.debug("Making the request to the User Service for user task orders update. Request data: {}, endpoint: {}", taskOrderDto, serviceEndpoint);
        restTemplate.put(serviceEndpoint, reqEntity);
    }

    @Override
    public void deleteUserByEmail(String email) {
        String serviceEndpoint = serviceEndpointBase + SLASH_CH + email;
        log.debug("Making the request to the User Service for the user by its email. User email: {}, endpoint: {}", email, serviceEndpoint);
        restTemplate.delete(serviceEndpointBase, UserDto.class);
    }

    private String getServiceEndpointBase() {
        String activeProfile = env.getProperty(SPRING_ACTIVE_PROFILE_ENV_PROP_NAME);
        if (StringUtils.isNotBlank(activeProfile) && ACTIVE_PROFILE_CONTAINER.equals(activeProfile)) {
            String taskServiceHostName = env.getProperty(USER_SERVICE_HOST_ENV_PROP_NAME);
            String taskServiceHostPort = env.getProperty(USER_SERVICE_PORT_ENV_PROP_NAME);
            return USER_SERVICE_ENDPOINT_FORMAT.format(new Object[]{taskServiceHostName, taskServiceHostPort});
        } else {
            return USER_SERVICE_ENDPOINT_FORMAT.format(new Object[]{LOCALHOST, USER_SERVICE_PORT_NO_PROFILE});
        }
    }
}

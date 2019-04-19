package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.client.config.converter.KryoHttpMessageConverter;
import org.art.web.warrior.client.config.interceptor.RequestProcessingLogger;
import org.art.web.warrior.client.dto.ClientServiceRequest;
import org.art.web.warrior.client.dto.ClientServiceResponse;
import org.art.web.warrior.client.dto.CompServiceRequest;
import org.art.web.warrior.client.dto.CompServiceResponse;
import org.art.web.warrior.client.service.CustomByteClassLoader;
import org.art.web.warrior.client.util.CompServiceResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import static org.art.web.warrior.client.service.CommonServiceConstants.*;

@Slf4j
@Controller
@RequestMapping("/submit")
public class ClientAppController {

    private Environment env;

    private RestTemplate restTemplate;

    @Autowired
    public ClientAppController(Environment env, RestTemplateBuilder restTemplateBuilder) {
        this.env = env;
        this.restTemplate = restTemplateBuilder
                .additionalInterceptors(new RequestProcessingLogger())
                .additionalMessageConverters(new KryoHttpMessageConverter())
                .build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ClientServiceResponse submitClientCode(@RequestBody ClientServiceRequest clientReqData) {
        String className = clientReqData.getClassName();
        String srcCode = clientReqData.getSrcCode();
        log.debug("Client code submission request: class name {}, source code {}", className, srcCode);
        ResponseEntity<CompServiceResponse> response = callCompilationService(clientReqData);
        CompServiceResponse serviceResp = response.getBody();
        if (serviceResp == null) {
            log.debug("Internal service error occurred! Compilation service responded with empty body.");
            return CompServiceResponseUtil.buildEmptyBodyResponse(clientReqData);
        }
        if (serviceResp.isCompError()) {
            log.debug("Compilation errors occurred while compiling client source code!");
            return CompServiceResponseUtil.buildCompErrorResponse(serviceResp);
        } else {
            return CompServiceResponseUtil.buildCompOkResponse(serviceResp);
        }
    }

    private ResponseEntity<CompServiceResponse> callCompilationService(ClientServiceRequest clientReqData) {
        String compServiceEndpoint = getCompServiceEndpoint();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        String className = clientReqData.getClassName();
        String code = clientReqData.getSrcCode();
        CompServiceRequest serviceReqData = new CompServiceRequest(className, code);
        HttpEntity<CompServiceRequest> reqEntity = new HttpEntity<>(serviceReqData, headers);
        log.debug("Making the request to the Compilation service. Endpoint: {}, request data: {}", compServiceEndpoint, serviceReqData);
        return restTemplate.postForEntity(compServiceEndpoint, reqEntity, CompServiceResponse.class);
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

    private Class<?> loadCompiledClass(String className, byte[] classData) {
        log.debug("Loading the compiled class. Class name: {}, class data byte array length: {}", className, classData.length);
        try {
            CustomByteClassLoader loader = new CustomByteClassLoader();
            loader.addClassData(className, classData);
            return loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            log.info("Cannot find and load the compiled class. Class name: {}, class data byte array length: {}", className, classData.length);
            return null;
        }
    }
}
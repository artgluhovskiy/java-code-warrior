package org.art.web.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.client.config.converter.KryoHttpMessageConverter;
import org.art.web.client.config.interceptor.RequestProcessingLogger;
import org.art.web.client.dto.ServiceRequestDto;
import org.art.web.client.dto.ServiceResponseDto;
import org.art.web.client.model.CompilationRequest;
import org.art.web.client.model.CompilationResponse;
import org.art.web.client.service.CustomByteClassLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
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

@Slf4j
@Controller
@RequestMapping("/compile")
public class CompilerController {

    private static final String COMP_SERVICE_ENDPOINT = "http://localhost:8080/compile/src/entity";

    private RestTemplate restTemplate;

    @Autowired
    public CompilerController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .additionalInterceptors(new RequestProcessingLogger())
                .additionalMessageConverters(new KryoHttpMessageConverter())
                .build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public CompilationResponse compile(@RequestBody CompilationRequest reqData) {
        String className = reqData.getClassName();
        String srcCode = reqData.getCode();
        log.debug("Client compilation request: class name {}, source code {}", className, srcCode);
        ResponseEntity<ServiceResponseDto> response = executeCompilationRequest(reqData);
        ServiceResponseDto body = response.getBody();
        //TODO: Process response dto !!!
        if (body == null) {
            log.info("Failed to process response entity. Response body is empty!");
            return new CompilationResponse(className, srcCode, "Errors occurred while source code compilation!");
        }
        log.debug("Compilation service responded with the status code: {}", response.getStatusCodeValue());
        Class<?> loadedClass = loadCompiledClass(className, body.getCompiledClass());
        if (loadedClass == null) {
            return new CompilationResponse(className, srcCode, "Some problems occurred while loading the compiled class");
        }
        return new CompilationResponse(loadedClass.getSimpleName(), srcCode, "Client source code was successfully compiled!");
    }

    private ResponseEntity<ServiceResponseDto> executeCompilationRequest(CompilationRequest reqData) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        ServiceRequestDto serviceReqData = new ServiceRequestDto(reqData.getClassName(), reqData.getCode());
        HttpEntity<ServiceRequestDto> entity = new HttpEntity<>(serviceReqData, headers);
        log.debug("Making the request to a Compilation service. Endpoint: {}, request data: {}", COMP_SERVICE_ENDPOINT, serviceReqData);
        return restTemplate.postForEntity(COMP_SERVICE_ENDPOINT, entity, ServiceResponseDto.class);
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
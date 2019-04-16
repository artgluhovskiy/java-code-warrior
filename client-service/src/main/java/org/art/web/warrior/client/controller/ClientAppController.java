package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.config.converter.KryoHttpMessageConverter;
import org.art.web.warrior.client.config.interceptor.RequestProcessingLogger;
import org.art.web.warrior.client.dto.ClientServiceRequest;
import org.art.web.warrior.client.dto.ClientServiceResponse;
import org.art.web.warrior.client.dto.CompServiceRequest;
import org.art.web.warrior.client.dto.CompServiceResponse;
import org.art.web.warrior.client.service.CustomByteClassLoader;
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
@RequestMapping("/client/submit")
public class ClientAppController {

    private static final String COMP_SERVICE_ENDPOINT = "http://localhost:8080/compile/src/entity";

    private RestTemplate restTemplate;

    @Autowired
    public ClientAppController(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .additionalInterceptors(new RequestProcessingLogger())
                .additionalMessageConverters(new KryoHttpMessageConverter())
                .build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ClientServiceResponse submitClientCode(@RequestBody ClientServiceRequest clientReqData) {
        String className = clientReqData.getClassName();
        String srcCode = clientReqData.getCode();
        log.debug("Client code submission request: class name {}, source code {}", className, srcCode);
        ResponseEntity<CompServiceResponse> response = callCompilationService(clientReqData);
        CompServiceResponse body = response.getBody();
        if (body == null) {
            log.info("Failed to process compilation service response entity. Response body is empty!");
            return new ClientServiceResponse(className, srcCode, "Errors occurred while source code compilation!");
        }
        //TODO
        return null;

    }

    private ResponseEntity<CompServiceResponse> callCompilationService(ClientServiceRequest clientReqData) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        String className = clientReqData.getClassName();
        String code = clientReqData.getCode();
        CompServiceRequest serviceReqData = new CompServiceRequest(className, code);
        HttpEntity<CompServiceRequest> reqEntity = new HttpEntity<>(serviceReqData, headers);
        log.debug("Making the request to the Compilation service. Endpoint: {}, request data: {}", COMP_SERVICE_ENDPOINT, serviceReqData);
        return restTemplate.postForEntity(COMP_SERVICE_ENDPOINT, reqEntity, CompServiceResponse.class);
    }

//    private ClientServiceResponse processServiceResponse(ResponseEntity<CompServiceResponse> response, ClientServiceRequest reqData) {
//
//        //TODO: Process response dto !!!
//
//        log.debug("Compilation service responded with the status code: {}", response.getStatusCodeValue());
//        Class<?> loadedClass = loadCompiledClass(className, body.getCompiledClass());
//        if (loadedClass == null) {
//            return new ClientServiceResponse(className, srcCode, "Some problems occurred while loading the compiled class");
//        }
//        return new ClientServiceResponse(loadedClass.getSimpleName(), srcCode, "Client source code was successfully compiled!");
//    }

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
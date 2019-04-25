package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.art.web.warrior.client.config.converter.KryoHttpMessageConverter;
import org.art.web.warrior.client.config.interceptor.RequestProcessingLogger;
import org.art.web.warrior.client.dto.*;
import org.art.web.warrior.client.util.CompServiceResponseUtil;
import org.art.web.warrior.commons.util.FileReaderUtil;
import org.art.web.warrior.commons.compiler.dto.CompServiceResponse;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitRequest;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitResponse;
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

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.art.web.warrior.client.CommonServiceConstants.*;
import static org.art.web.warrior.commons.CommonConstants.*;

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
    public ClientServiceResponse submitClientCode(@RequestBody CompServiceUnitRequest clientRequestData) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String className = clientRequestData.getClassName();
        String srcCode = clientRequestData.getSrcCode();
        if (!clientRequestData.isValid()) {
            log.debug("Client code cannot be processed: class name {}, source code {}", className, srcCode);
            return CompServiceResponseUtil.buildUnprocessableEntityResponse(clientRequestData);
        }
        log.debug("Client code submission request: class name {}, source code {}", className, srcCode);
        ResponseEntity<CompServiceResponse> response = callCompilationService(singletonList(clientRequestData));
        CompServiceResponse serviceResp = response.getBody();
        if (serviceResp == null) {
            log.debug("Internal service error occurred! Compilation service responded with empty body.");
            return CompServiceResponseUtil.buildEmptyBodyResponse(clientRequestData);
        }
        if (serviceResp.isCompError()) {
            log.debug("Compilation errors occurred while compiling client source code!");
            return CompServiceResponseUtil.buildCompErrorResponse(serviceResp, className);
        }
        //TODO: Getting the compiled "test-wrapper" for the coding problem (from MySQL). As a stub, use additional
        //request to the compiler service to compile the dummy solution with the test wrapper (maybe already compiled
        //test wrapper should be store in the database, need to check here)

//        String solutionClassName = "Solution";
//        String runnerClassName = "Runner";
//        String solutionFilePath = "/tasks/src/int-palindrom/Solution.java";
//        String runnerFilePath = "/tasks/src/int-palindrom/Runner.java";
//        String solutionSrc = FileReaderUtil.readFileIntoString(solutionFilePath);
//        String runnerSrc = FileReaderUtil.readFileIntoString(runnerFilePath);
//        CompServiceUnitRequest solutionRequest = new CompServiceUnitRequest(solutionClassName, solutionSrc);
//        CompServiceUnitRequest runnerRequest = new CompServiceUnitRequest(runnerClassName, runnerSrc);
//        CompServiceResponse serviceResponse = callCompilationService(Arrays.asList(solutionRequest, runnerRequest)).getBody();
//        CompServiceUnitResponse runnerResponse = serviceResponse.getCompUnitResults().get(runnerClassName);
//
//        byte[] runnerClassBytes = runnerResponse.getCompiledClassBytes();
//        byte[] solutionClassBytes = serviceResp.getCompUnitResults().get(solutionClassName).getCompiledClassBytes();

//        ExecServiceRequest execServiceRequest = new ExecServiceRequest(solutionClassName, solutionClassBytes, runnerClassName, runnerClassBytes);
//        ResponseEntity<ExecServiceResponse> execServiceResponse = callExecutorService(execServiceRequest);

        //Todo: send class data to the Executor service

//        CustomByteClassLoader classLoader = new CustomByteClassLoader();
//        classLoader.addClassData(solutionClassName, solutionClassBytes);
//        classLoader.addClassData(runnerClassName, runnerClassBytes);
//
//        Class<?> solutionClass = classLoader.loadClass(solutionClassName);
//        Class<?> runnerClass = classLoader.loadClass(runnerClassName);
//
//        Object runnerInstance = runnerClass.newInstance();
//
//        Method solutionSetter = runnerClass.getDeclaredMethod("setSolution", solutionClass);
//
//        solutionSetter.invoke(runnerInstance, solutionClass.cast(solutionClass.newInstance()));
//
//        Method runMethod = runnerClass.getDeclaredMethod("run");
//
//        runMethod.invoke(runnerInstance);

        return CompServiceResponseUtil.buildCompOkResponse(serviceResp, className);

    }

    private ResponseEntity<CompServiceResponse> callCompilationService(List<CompServiceUnitRequest> compRequestData) {
        String compServiceEndpoint = getCompServiceEndpoint();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.set(HttpHeaders.ACCEPT, KRYO_CONTENT_TYPE);
        HttpEntity<List<CompServiceUnitRequest>> reqEntity = new HttpEntity<>(compRequestData, headers);
        log.debug("Making the request to the Compilation service. Endpoint: {}, request data: {}", compServiceEndpoint, compRequestData);
        return restTemplate.postForEntity(compServiceEndpoint, reqEntity, CompServiceResponse.class);
    }

    private ResponseEntity<ExecServiceResponse> callExecutorService(ExecServiceRequest execRequestData) {
        String execServiceEndpoint = getExecServiceEndpoint();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, KRYO_CONTENT_TYPE);
        HttpEntity<ExecServiceRequest> reqEntity = new HttpEntity<>(execRequestData, headers);
        log.debug("Making the request to the Executor service. Endpoint: {}, request data: {}", execServiceEndpoint, execRequestData);
        return restTemplate.postForEntity(execServiceEndpoint, reqEntity, ExecServiceResponse.class);
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

    private String getExecServiceEndpoint() {
        String activeProfile = env.getProperty(SPRING_ACTIVE_PROFILE_ENV_PROP_NAME);
        if (StringUtils.isNotBlank(activeProfile) && ACTIVE_PROFILE_CONTAINER.equals(activeProfile)) {
            String execHostName = env.getProperty(EXECUTION_SERVICE_HOST_ENV_PROP_NAME);
            String execHostPort = env.getProperty(EXECUTION_SERVICE_PORT_ENV_PROP_NAME);
            return INVOCATION_SERVICE_ENDPOINT_FORMAT.format(new Object[] {execHostName, execHostPort});
        } else {
            return INVOCATION_SERVICE_ENDPOINT_FORMAT.format(new Object[] {LOCALHOST, EXEC_SERVICE_PORT_NO_PROFILE});
        }
    }
}
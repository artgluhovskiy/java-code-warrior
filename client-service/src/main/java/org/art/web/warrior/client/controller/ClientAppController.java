package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.ClientServiceResponse;
import org.art.web.warrior.client.service.api.CodingTaskService;
import org.art.web.warrior.client.service.api.CompServiceClient;
import org.art.web.warrior.client.service.api.ExecServiceClient;
import org.art.web.warrior.client.util.ClientResponseUtil;
import org.art.web.warrior.commons.compiler.dto.CompServiceResponse;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.InvocationTargetException;

import static java.util.Collections.singletonList;

@Slf4j
@Controller
@RequestMapping("/submit")
public class ClientAppController {

    private final CompServiceClient compServiceClient;

    private final ExecServiceClient execServiceClient;

    private final CodingTaskService taskService;

    @Autowired
    public ClientAppController(CompServiceClient compServiceClient, ExecServiceClient execServiceClient, CodingTaskService taskService) {
        this.compServiceClient = compServiceClient;
        this.execServiceClient = execServiceClient;
        this.taskService = taskService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ClientServiceResponse submitClientCode(@RequestBody CompServiceUnitRequest clientRequestData) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String className = clientRequestData.getClassName();
        String srcCode = clientRequestData.getSrcCode();
        if (!clientRequestData.isValid()) {
            log.debug("Client code cannot be processed: class name {}, source code {}", className, srcCode);
            return ClientResponseUtil.buildUnprocessableEntityResponse(clientRequestData);
        }
        log.debug("Client code submission request: class name {}, source code {}", className, srcCode);
        CompServiceResponse serviceResp = compServiceClient.callCompilationService(singletonList(clientRequestData));
        if (serviceResp == null) {
            log.debug("Internal service error occurred! Compilation service responded with empty body.");
            return ClientResponseUtil.buildEmptyBodyResponse(clientRequestData);
        }
        if (serviceResp.isCompError()) {
            log.debug("Compilation errors occurred while compiling client source code!");
            return ClientResponseUtil.buildCompErrorResponse(serviceResp, className);
        }
        //TODO: Getting the compiled "test-wrapper" for the coding problem (from MySQL). As a stub, use additional
        //request to the compiler service to compile the dummy solution with the test wrapper (maybe already compiled
        //test wrapper should be store in the database, need to check here)



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

        return ClientResponseUtil.buildCompOkResponse(serviceResp, className);
    }
}
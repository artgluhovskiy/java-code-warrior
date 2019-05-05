package org.art.web.warrior.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.client.dto.ClientServiceUserResponse;
import org.art.web.warrior.client.dto.UserCodeCompData;
import org.art.web.warrior.client.service.api.CompServiceClient;
import org.art.web.warrior.client.service.api.ExecServiceClient;
import org.art.web.warrior.client.service.api.TaskServiceClient;
import org.art.web.warrior.client.util.ClientResponseUtil;
import org.art.web.warrior.commons.compiler.dto.CompServiceResponse;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonList;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserClientController {

    private final CompServiceClient compServiceClient;

    private final ExecServiceClient execServiceClient;

    private final TaskServiceClient taskService;

    @Autowired
    public UserClientController(CompServiceClient compServiceClient, ExecServiceClient execServiceClient, TaskServiceClient taskService) {
        this.compServiceClient = compServiceClient;
        this.execServiceClient = execServiceClient;
        this.taskService = taskService;
    }

    @ResponseBody
    @PostMapping(value = "submit", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ClientServiceUserResponse submitClientCode(@Valid @RequestBody UserCodeCompData userCompData) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String className = userCompData.getClassName();
        String srcCode = userCompData.getSrcCode();
        log.debug("Client code submission request: class name {}, source code {}", className, srcCode);
        CompServiceUnitRequest requestCompData = new CompServiceUnitRequest(userCompData.getClassName(), userCompData.getSrcCode());
        CompServiceResponse serviceResp = compServiceClient.callCompilationService(singletonList(requestCompData));
        if (serviceResp == null) {
            log.debug("Internal service error occurred! Compilation service responded with empty body.");
            return ClientResponseUtil.buildEmptyBodyUserResponse(userCompData);
        }
        if (serviceResp.isCompError()) {
            log.debug("Compilation errors occurred while compiling client source code!");
            return ClientResponseUtil.buildCompErrorUserResponse(serviceResp, className);
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

        return ClientResponseUtil.buildCompOkUserResponse(serviceResp, className);
    }
}
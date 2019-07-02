package org.art.web.warrior.exec.util;

import lombok.extern.slf4j.Slf4j;
import org.art.web.warrior.commons.CustomByteClassLoader;
import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.execution.dto.ExecutionRequest;
import org.art.web.warrior.commons.execution.dto.ExecutionResponse;

import java.lang.reflect.Method;

import static org.art.web.warrior.commons.CommonConstants.SOLUTION_SETTER_METHOD_NAME;
import static org.art.web.warrior.exec.ServiceCommonConstants.CLIENT_CODE_EXEC_OK_MESSAGE;

@Slf4j
public class ServiceUtil {

    private ServiceUtil() {
    }

    public static Object buildRunnerInstance(ExecutionRequest requestData) {
        String solutionClassName = requestData.getSolutionClassName();
        byte[] solutionClassBytes = requestData.getSolutionClassData();
        String runnerClassName = requestData.getRunnerClassName();
        byte[] runnerClassBytes = requestData.getRunnerClassData();
        CustomByteClassLoader classLoader = new CustomByteClassLoader();
        classLoader.addClassData(solutionClassName, solutionClassBytes);
        classLoader.addClassData(runnerClassName, runnerClassBytes);
        Object runnerInstance = null;
        try {
            Class<?> solutionClass = classLoader.loadClass(solutionClassName);
            Class<?> runnerClass = classLoader.loadClass(runnerClassName);
            runnerInstance = runnerClass.newInstance();
            Method solutionSetter = runnerClass.getDeclaredMethod(SOLUTION_SETTER_METHOD_NAME, solutionClass);
            solutionSetter.invoke(runnerInstance, solutionClass.cast(solutionClass.newInstance()));
        } catch (Exception e) {
            log.warn("Error occurred while creating Runner instance. Returning null. Request data: {}", requestData);
        }
        return runnerInstance;
    }

    public static ExecutionResponse buildExecutionServiceOkResp() {
        return ExecutionResponse.builder()
                .respStatus(ServiceResponseStatus.SUCCESS.getStatusId())
                .message(CLIENT_CODE_EXEC_OK_MESSAGE)
                .build();
    }

    public static ExecutionResponse buildExecutionServiceInfoResp(String localPort, String appName, String appInfo) {
        String message = "Application name: " + appName + ". Local server port: " + localPort + ". Application info: " + appInfo;
        return ExecutionResponse.builder()
                .respStatus(ServiceResponseStatus.SUCCESS.getStatusId())
                .message(message)
                .build();
    }
}

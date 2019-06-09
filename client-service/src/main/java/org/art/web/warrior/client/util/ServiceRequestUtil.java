package org.art.web.warrior.client.util;

import org.art.web.warrior.client.dto.AdminTaskDto;
import org.art.web.warrior.commons.compiler.dto.CompilationRequest;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitDto;
import org.art.web.warrior.commons.execution.dto.ExecutionRequest;
import org.art.web.warrior.commons.tasking.dto.TaskDescriptorDto;
import org.art.web.warrior.commons.tasking.dto.TaskDto;
import org.art.web.warrior.commons.util.ParserUtil;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.art.web.warrior.commons.CommonConstants.RUNNER_CLASS_NAME;
import static org.art.web.warrior.commons.CommonConstants.SOLUTION_CLASS_NAME;

public class ServiceRequestUtil {

    private ServiceRequestUtil() {
    }

    public static TaskDto buildTaskServicePublicationRequest(AdminTaskDto requestData, byte[] runnerClassData) {
        TaskDescriptorDto descriptorDto = TaskDescriptorDto.builder()
            .nameId(requestData.getTaskNameId())
            .name(requestData.getTaskName())
            .description(requestData.getTaskDescription())
            .build();
        return TaskDto.builder()
            .descriptor(descriptorDto)
            .methodSign(requestData.getTaskMethodSign())
            .runnerClassData(runnerClassData)
            .build();
    }

    public static CompilationRequest buildCompilationServiceReq(AdminTaskDto requestData) {
        String solutionSrcCode = requestData.getSolutionSrcCode();
        String solutionClassName = ParserUtil.parseClassNameFromSrcString(solutionSrcCode);
        CompilationUnitDto solutionCompUnit = new CompilationUnitDto(solutionClassName, solutionSrcCode);
        String runnerSrcCode = requestData.getRunnerSrcCode();
        String runnerClassName = ParserUtil.parseClassNameFromSrcString(runnerSrcCode);
        CompilationUnitDto runnerCompUnit = new CompilationUnitDto(runnerClassName, runnerSrcCode);
        List<CompilationUnitDto> compUnits = Arrays.asList(solutionCompUnit, runnerCompUnit);
        return new CompilationRequest(compUnits);
    }

    public static ExecutionRequest buildExecutionServiceRequest(byte[] solutionClassData, byte[] runnerClassData) {
        return ExecutionRequest.builder()
            .solutionClassName(SOLUTION_CLASS_NAME)
            .solutionClassData(solutionClassData)
            .runnerClassName(RUNNER_CLASS_NAME)
            .runnerClassData(runnerClassData)
            .build();
    }

    public static CompilationRequest buildCompilationServiceRequest(String className, String srcCode) {
        CompilationUnitDto compilationData = new CompilationUnitDto(className, srcCode);
        return new CompilationRequest(singletonList(compilationData));
    }
}

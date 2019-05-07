package org.art.web.warrior.client.util;

import org.art.web.warrior.client.dto.AdminTaskCompData;
import org.art.web.warrior.commons.execution.dto.ExecutionReq;
import org.art.web.warrior.commons.CommonConstants;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitReq;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskPublicationReq;
import org.art.web.warrior.commons.compiler.dto.CompilationResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskResp;
import org.art.web.warrior.commons.util.ParserUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.art.web.warrior.commons.CommonConstants.RUNNER_CLASS_NAME;

public class ClientRequestUtil {

    private ClientRequestUtil() {
    }

    public static CodingTaskPublicationReq buildTaskServicePublicationReq(AdminTaskCompData clientRequestData, CompilationResp serviceResp) {
        Map<String, CompilationUnitResp> compResults = serviceResp.getCompUnitResults();
        byte[] compiledRunnerClass = compResults.get(RUNNER_CLASS_NAME).getCompiledClassBytes();
        return CodingTaskPublicationReq.builder()
                .nameId(clientRequestData.getTaskNameId())
                .name(clientRequestData.getTaskName())
                .description(clientRequestData.getTaskDescription())
                .methodSign(clientRequestData.getTaskMethodSign())
                .runnerClassData(compiledRunnerClass)
                .build();
    }

    public static List<CompilationUnitReq> buildCompilationServiceReq(AdminTaskCompData requestData) {
        String solutionSrcCode = requestData.getSolutionSrcCode();
        String solutionClassName = ParserUtil.parseClassNameFromSrcString(solutionSrcCode);
        CompilationUnitReq solutionCompUnit = new CompilationUnitReq(solutionClassName, solutionSrcCode);
        String runnerSrcCode = requestData.getRunnerSrcCode();
        String runnerClassName = ParserUtil.parseClassNameFromSrcString(runnerSrcCode);
        CompilationUnitReq runnerCompUnit = new CompilationUnitReq(runnerClassName, runnerSrcCode);
        return Arrays.asList(solutionCompUnit, runnerCompUnit);
    }

    public static ExecutionReq buildExecutionServiceRequest(CompilationResp compServiceResp, CodingTaskResp taskServiceResp) {
        CompilationUnitResp solutionClassData = compServiceResp.getCompUnitResults().get(CommonConstants.SOLUTION_CLASS_NAME);
        return ExecutionReq.builder()
                .solutionClassName(solutionClassData.getClassName())
                .solutionClassData(solutionClassData.getCompiledClassBytes())
                .runnerClassName(RUNNER_CLASS_NAME)
                .runnerClassData(taskServiceResp.getRunnerClassData())
                .build();
    }
}

package org.art.web.warrior.client.util;

import org.art.web.warrior.client.dto.AdminTaskPublicationData;
import org.art.web.warrior.commons.CommonConstants;
import org.art.web.warrior.commons.compiler.dto.CompilationRequest;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitDto;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitResp;
import org.art.web.warrior.commons.execution.dto.ExecutionReq;
import org.art.web.warrior.commons.tasking.dto.TaskDto;
import org.art.web.warrior.commons.util.ParserUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.art.web.warrior.commons.CommonConstants.RUNNER_CLASS_NAME;

public class ClientRequestUtil {

    private ClientRequestUtil() {
    }

    public static TaskDto buildTaskServiceReq(AdminTaskPublicationData requestData, CompilationResponse serviceResp) {
        Map<String, CompilationUnitResp> compResults = serviceResp.getCompUnitResults();
        byte[] compiledRunnerClass = compResults.get(RUNNER_CLASS_NAME).getCompiledClassBytes();
        return TaskDto.builder()
                .nameId(requestData.getTaskNameId())
                .name(requestData.getTaskName())
                .description(requestData.getTaskDescription())
                .methodSign(requestData.getTaskMethodSign())
                .runnerClassData(compiledRunnerClass)
                .build();
    }

    public static CompilationRequest buildCompilationServiceReq(AdminTaskPublicationData requestData) {
        String solutionSrcCode = requestData.getSolutionSrcCode();
        String solutionClassName = ParserUtil.parseClassNameFromSrcString(solutionSrcCode);
        CompilationUnitDto solutionCompUnit = new CompilationUnitDto(solutionClassName, solutionSrcCode);
        String runnerSrcCode = requestData.getRunnerSrcCode();
        String runnerClassName = ParserUtil.parseClassNameFromSrcString(runnerSrcCode);
        CompilationUnitDto runnerCompUnit = new CompilationUnitDto(runnerClassName, runnerSrcCode);
        List<CompilationUnitDto> compUnits = Arrays.asList(solutionCompUnit, runnerCompUnit);
        return new CompilationRequest(compUnits);
    }

    public static ExecutionReq buildExecutionServiceRequest(CompilationResponse compilationResponse, TaskServiceResp taskServiceResp) {
        CompilationUnitResp solutionClassData = compilationResponse.getCompUnitResults().get(CommonConstants.SOLUTION_CLASS_NAME);
        return ExecutionReq.builder()
                .solutionClassName(solutionClassData.getClassName())
                .solutionClassData(solutionClassData.getCompiledClassBytes())
                .runnerClassName(RUNNER_CLASS_NAME)
                .runnerClassData(taskServiceResp.getRunnerClassData())
                .build();
    }
}

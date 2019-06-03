package org.art.web.warrior.client.util;

import org.art.web.warrior.client.dto.AdminTaskPublicationData;
import org.art.web.warrior.commons.CommonConstants;
import org.art.web.warrior.commons.compiler.dto.CompServiceReq;
import org.art.web.warrior.commons.compiler.dto.CompServiceResp;
import org.art.web.warrior.commons.compiler.dto.CompilationUnit;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitResp;
import org.art.web.warrior.commons.execution.dto.ExecutionReq;
import org.art.web.warrior.commons.tasking.dto.CodingTaskDto;
import org.art.web.warrior.commons.tasking.dto.TaskServiceResp;
import org.art.web.warrior.commons.util.ParserUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.art.web.warrior.commons.CommonConstants.RUNNER_CLASS_NAME;

public class ClientRequestUtil {

    private ClientRequestUtil() {
    }

    public static CodingTaskDto buildTaskServiceReq(AdminTaskPublicationData requestData, CompServiceResp serviceResp) {
        Map<String, CompilationUnitResp> compResults = serviceResp.getCompUnitResults();
        byte[] compiledRunnerClass = compResults.get(RUNNER_CLASS_NAME).getCompiledClassBytes();
        return CodingTaskDto.builder()
                .nameId(requestData.getTaskNameId())
                .name(requestData.getTaskName())
                .description(requestData.getTaskDescription())
                .methodSign(requestData.getTaskMethodSign())
                .runnerClassData(compiledRunnerClass)
                .build();
    }

    public static CompServiceReq buildCompilationServiceReq(AdminTaskPublicationData requestData) {
        String solutionSrcCode = requestData.getSolutionSrcCode();
        String solutionClassName = ParserUtil.parseClassNameFromSrcString(solutionSrcCode);
        CompilationUnit solutionCompUnit = new CompilationUnit(solutionClassName, solutionSrcCode);
        String runnerSrcCode = requestData.getRunnerSrcCode();
        String runnerClassName = ParserUtil.parseClassNameFromSrcString(runnerSrcCode);
        CompilationUnit runnerCompUnit = new CompilationUnit(runnerClassName, runnerSrcCode);
        List<CompilationUnit> compUnits = Arrays.asList(solutionCompUnit, runnerCompUnit);
        return new CompServiceReq(compUnits);
    }

    public static ExecutionReq buildExecutionServiceRequest(CompServiceResp compServiceResp, TaskServiceResp taskServiceResp) {
        CompilationUnitResp solutionClassData = compServiceResp.getCompUnitResults().get(CommonConstants.SOLUTION_CLASS_NAME);
        return ExecutionReq.builder()
                .solutionClassName(solutionClassData.getClassName())
                .solutionClassData(solutionClassData.getCompiledClassBytes())
                .runnerClassName(RUNNER_CLASS_NAME)
                .runnerClassData(taskServiceResp.getRunnerClassData())
                .build();
    }
}

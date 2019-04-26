package org.art.web.warrior.client.service;

import org.art.web.warrior.client.model.CodingTask;
import org.art.web.warrior.client.service.api.CodingTaskService;
import org.art.web.warrior.client.service.api.CompServiceClient;
import org.art.web.warrior.commons.compiler.dto.CompServiceResponse;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitRequest;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitResponse;
import org.art.web.warrior.commons.util.FileReaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class CodingTaskServiceDummy implements CodingTaskService {

    private final CompServiceClient compServiceClient;

    private final Map<Long, CodingTask> codingTaskStorage = new HashMap<>();

    @Autowired
    public CodingTaskServiceDummy(CompServiceClient compServiceClient) {
        this.compServiceClient = compServiceClient;
        initCodingStorage();
    }

    private Map<Long, CodingTask> initCodingStorage() {
        CodingTask.CodingTaskBuilder builder = CodingTask.builder()
                .nameId("intPalindrome")
                .name("Int Palindrome")
                .description("Int Palindrome Task")
                .methodSign("public boolean isPalindrome(int x) {");

        String solutionClassName = "Solution";
        String runnerClassName = "Runner";
        String solutionFilePath = "/tasks/src/int-palindrom/Solution.java";
        String runnerFilePath = "/tasks/src/int-palindrom/Runner.java";
        String solutionSrc = FileReaderUtil.readFileIntoString(solutionFilePath);
        String runnerSrc = FileReaderUtil.readFileIntoString(runnerFilePath);
        CompServiceUnitRequest solutionRequest = new CompServiceUnitRequest(solutionClassName, solutionSrc);

        CompServiceUnitRequest runnerRequest = new CompServiceUnitRequest(runnerClassName, runnerSrc);
        CompServiceResponse serviceResponse = compServiceClient.callCompilationService(Arrays.asList(solutionRequest, runnerRequest));
        CompServiceUnitResponse runnerResponse = serviceResponse.getCompUnitResults().get(runnerClassName);

        byte[] runnerClassBytes = runnerResponse.getCompiledClassBytes();

        return null;

    }

    @Override
    public CodingTask getCodingTaskByUserId(long userId) {
        return null;
    }
}

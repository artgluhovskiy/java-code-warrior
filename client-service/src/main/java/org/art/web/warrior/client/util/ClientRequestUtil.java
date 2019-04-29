package org.art.web.warrior.client.util;

import org.art.web.warrior.client.dto.AdminTaskCompData;
import org.art.web.warrior.commons.tasking.dto.TaskServicePubRequest;
import org.art.web.warrior.commons.compiler.dto.CompServiceResponse;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitResponse;

import java.util.Map;

import static org.art.web.warrior.commons.CommonConstants.RUNNER_CLASS_NAME;

public class ClientRequestUtil {

    private ClientRequestUtil() {
    }

    public static TaskServicePubRequest buildTaskServiceRequest(AdminTaskCompData clientRequestData, CompServiceResponse serviceResp) {
        Map<String, CompServiceUnitResponse> compResults = serviceResp.getCompUnitResults();
        byte[] compiledRunnerClass = compResults.get(RUNNER_CLASS_NAME).getCompiledClassBytes();
        return TaskServicePubRequest.builder()
                .nameId(clientRequestData.getTaskNameId())
                .name(clientRequestData.getTaskName())
                .description(clientRequestData.getTaskDescription())
                .methodSign(clientRequestData.getTaskMethodSign())
                .runnerClassData(compiledRunnerClass)
                .build();
    }
}

package org.art.web.warrior.tasking.util;

import org.art.web.warrior.commons.tasking.dto.CodingTaskDto;
import org.art.web.warrior.tasking.model.CodingTask;

public class ServiceRequestUtil {

    private ServiceRequestUtil() {
    }

    public static CodingTask buildCodingTaskFromReqData(CodingTaskDto requestData) {
        return CodingTask.builder()
                .nameId(requestData.getNameId())
                .name(requestData.getName())
                .description(requestData.getDescription())
                .methodSign(requestData.getMethodSign())
                .runnerClassData(requestData.getRunnerClassData())
                .build();
    }
}

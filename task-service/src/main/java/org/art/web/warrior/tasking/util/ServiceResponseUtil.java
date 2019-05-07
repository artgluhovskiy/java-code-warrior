package org.art.web.warrior.tasking.util;

import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.tasking.dto.CodingTaskPublicationResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskResp;
import org.art.web.warrior.tasking.model.CodingTask;

import java.util.List;
import java.util.stream.Collectors;

import static org.art.web.warrior.tasking.ServiceCommonConstants.TASK_PUBLICATION_ERROR_RESPONSE;
import static org.art.web.warrior.tasking.ServiceCommonConstants.TASK_PUBLICATION_OK_RESPONSE;

public class ServiceResponseUtil {

    private ServiceResponseUtil() {
    }

    public static CodingTaskPublicationResp buildCodingTaskPublicationResp(CodingTask task) {
        CodingTaskPublicationResp pubResponse = new CodingTaskPublicationResp();
        if (task != null) {
            pubResponse.setTaskId(task.getId());
            pubResponse.setMessage(TASK_PUBLICATION_OK_RESPONSE);
            pubResponse.setRespStatus(ServiceResponseStatus.SUCCESS.getStatusId());
        } else {
            pubResponse.setMessage(TASK_PUBLICATION_ERROR_RESPONSE);
            pubResponse.setRespStatus(ServiceResponseStatus.TASK_PUBLICATION_ERROR.getStatusId());
        }
        return pubResponse;
    }

    public static CodingTaskResp buildCodingTaskResp(CodingTask task) {
        CodingTaskResp.CodingTaskRespBuilder builder = CodingTaskResp.builder();
        if (task != null) {
            builder.respStatus(ServiceResponseStatus.SUCCESS.getStatusId())
                    .nameId(task.getNameId())
                    .name(task.getName())
                    .description(task.getDescription())
                    .methodSign(task.getMethodSign())
                    .runnerClassData(task.getRunnerClassData())
                    .build();
        } else {
            builder.respStatus(ServiceResponseStatus.NOT_FOUND.getStatusId());
        }
        return builder.build();
    }

    public static List<CodingTaskResp> buildAllCodingTasksResp(List<CodingTask> codingTasks) {
        return codingTasks.stream()
                .map(task -> CodingTaskResp.builder()
                        .nameId(task.getNameId())
                        .name(task.getName())
                        .description(task.getDescription())
                        .methodSign(task.getMethodSign())
                        .build())
                .collect(Collectors.toList());
    }
}

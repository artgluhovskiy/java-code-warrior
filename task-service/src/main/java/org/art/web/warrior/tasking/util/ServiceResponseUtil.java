package org.art.web.warrior.tasking.util;

import org.art.web.warrior.commons.ServiceResponseStatus;
import org.art.web.warrior.commons.tasking.dto.TaskServicePubResponse;
import org.art.web.warrior.tasking.model.CodingTask;

import static org.art.web.warrior.tasking.ServiceCommonConstants.TASK_PUBLICATION_ERROR_RESPONSE;
import static org.art.web.warrior.tasking.ServiceCommonConstants.TASK_PUBLICATION_OK_RESPONSE;

public class ServiceResponseUtil {

    private ServiceResponseUtil() {
    }

    public static TaskServicePubResponse buildTaskPubResponse(CodingTask codingTask) {
        TaskServicePubResponse pubResponse = new TaskServicePubResponse();
        if (codingTask != null) {
            pubResponse.setTaskId(codingTask.getId());
            pubResponse.setMessage(TASK_PUBLICATION_OK_RESPONSE);
            pubResponse.setRespStatus(ServiceResponseStatus.SUCCESS.getStatusId());
        } else {
            pubResponse.setMessage(TASK_PUBLICATION_ERROR_RESPONSE);
            pubResponse.setRespStatus(ServiceResponseStatus.TASK_PUBLICATION_ERROR.getStatusId());
        }
        return pubResponse;
    }
}

package org.art.web.warrior.client.service.api;

import org.art.web.warrior.commons.tasking.dto.CodingTaskDescriptorsResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskDto;
import org.art.web.warrior.commons.tasking.dto.TaskServiceResp;

public interface TaskServiceClient {

    TaskServiceResp publishNewCodingTask(CodingTaskDto task);

    TaskServiceResp getCodingTaskByNameId(String nameId);

    TaskServiceResp updateCodingTask(CodingTaskDto task);

    CodingTaskDescriptorsResp getCodingTaskDescriptors();
}

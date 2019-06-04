package org.art.web.warrior.client.service.api;

import org.art.web.warrior.commons.tasking.dto.TaskDto;

public interface TaskServiceClient {

    TaskServiceResp publishNewCodingTask(TaskDto task);

    TaskServiceResp getCodingTaskByNameId(String nameId);

    TaskServiceResp updateCodingTask(TaskDto task);

    CodingTaskDescriptorsResp getCodingTaskDescriptors();
}

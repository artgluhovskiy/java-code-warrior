package org.art.web.warrior.client.service.api;

import org.art.web.warrior.commons.tasking.dto.CodingTaskDescriptorsResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskPublicationReq;
import org.art.web.warrior.commons.tasking.dto.CodingTaskPublicationResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskResp;

public interface TaskServiceClient {

    CodingTaskPublicationResp publishNewCodingTask(CodingTaskPublicationReq task);

    CodingTaskResp getCodingTaskByNameId(String nameId);

    CodingTaskDescriptorsResp getCodingTaskDescriptors();
}

package org.art.web.warrior.client.service.api;

import org.art.web.warrior.commons.tasking.dto.CodingTaskDescriptor;
import org.art.web.warrior.commons.tasking.dto.CodingTaskPublicationReq;
import org.art.web.warrior.commons.tasking.dto.CodingTaskPublicationResp;
import org.art.web.warrior.commons.tasking.dto.CodingTaskResp;

import java.util.List;

public interface TaskServiceClient {

    CodingTaskPublicationResp publishCodingTask(CodingTaskPublicationReq task);

    CodingTaskResp getCodingTaskByNameId(String nameId);

    List<CodingTaskDescriptor> getAllCodingTaskDescriptors();
}

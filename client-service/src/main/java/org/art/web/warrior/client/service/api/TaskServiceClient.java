package org.art.web.warrior.client.service.api;

import org.art.web.warrior.commons.tasking.dto.TaskServicePubRequest;
import org.art.web.warrior.commons.tasking.dto.TaskServicePubResponse;

public interface TaskServiceClient {

    TaskServicePubResponse publishNewCodingTask(TaskServicePubRequest task);

}

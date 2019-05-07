package org.art.web.warrior.client.service.api;

import org.art.web.warrior.commons.execution.dto.ExecutionReq;
import org.art.web.warrior.commons.execution.dto.ExecutionResp;

public interface ExecServiceClient {

    ExecutionResp executeCode(ExecutionReq execRequestData);
}

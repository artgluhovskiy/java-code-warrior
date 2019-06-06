package org.art.web.warrior.client.service.api;

import org.art.web.warrior.commons.execution.dto.ExecutionRequest;
import org.art.web.warrior.commons.execution.dto.ExecutionResponse;

public interface ExecServiceClient {

    ExecutionResponse executeCode(ExecutionRequest execRequestData);
}

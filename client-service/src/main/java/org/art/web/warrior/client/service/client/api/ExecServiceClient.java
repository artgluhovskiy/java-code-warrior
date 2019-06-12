package org.art.web.warrior.client.service.client.api;

import org.art.web.warrior.commons.execution.dto.ExecutionRequest;
import org.art.web.warrior.commons.execution.dto.ExecutionResponse;
import org.springframework.http.ResponseEntity;

public interface ExecServiceClient {

    ResponseEntity<ExecutionResponse> executeCode(ExecutionRequest execRequestData);
}

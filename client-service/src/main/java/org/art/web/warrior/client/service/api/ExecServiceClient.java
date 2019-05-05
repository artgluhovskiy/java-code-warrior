package org.art.web.warrior.client.service.api;

import org.art.web.warrior.client.dto.ExecServiceRequest;
import org.art.web.warrior.client.dto.ExecServiceResponse;

public interface ExecServiceClient extends ServiceClient {

    ExecServiceResponse callExecutorService(ExecServiceRequest execRequestData);
}

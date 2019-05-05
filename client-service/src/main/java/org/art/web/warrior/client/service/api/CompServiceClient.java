package org.art.web.warrior.client.service.api;

import org.art.web.warrior.commons.compiler.dto.CompServiceResponse;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitRequest;

import java.util.List;

public interface CompServiceClient extends ServiceClient {

    CompServiceResponse callCompilationService(List<CompServiceUnitRequest> compRequestData);
}

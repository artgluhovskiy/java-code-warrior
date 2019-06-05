package org.art.web.warrior.client.service.api;

import org.art.web.warrior.commons.compiler.dto.CompilationRequest;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;

public interface CompServiceClient {

    CompilationResponse compileSrc(CompilationRequest compRequestData);
}

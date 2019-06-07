package org.art.web.warrior.client.service.api;

import org.art.web.warrior.commons.compiler.dto.CompilationRequest;
import org.art.web.warrior.commons.compiler.dto.CompilationResponse;
import org.springframework.http.ResponseEntity;

public interface CompServiceClient {

    ResponseEntity<CompilationResponse> compileSrc(CompilationRequest compRequestData);
}

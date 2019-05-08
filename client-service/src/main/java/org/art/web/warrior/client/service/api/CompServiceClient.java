package org.art.web.warrior.client.service.api;

import org.art.web.warrior.commons.compiler.dto.CompilationReq;
import org.art.web.warrior.commons.compiler.dto.CompilationResp;

public interface CompServiceClient {

    CompilationResp compileSrc(CompilationReq compRequestData);
}

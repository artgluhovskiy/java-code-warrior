package org.art.web.warrior.client.service.api;

import org.art.web.warrior.commons.compiler.dto.CompilationResp;
import org.art.web.warrior.commons.compiler.dto.CompilationUnitReq;

import java.util.List;

public interface CompServiceClient {

    CompilationResp compileSrc(List<CompilationUnitReq> compRequestData);
}

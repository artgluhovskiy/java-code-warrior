package org.art.web.warrior.client.service.api;

import org.art.web.warrior.commons.compiler.dto.CompServiceReq;
import org.art.web.warrior.commons.compiler.dto.CompServiceResp;

public interface CompServiceClient {

    CompServiceResp compileSrc(CompServiceReq compRequestData);
}

package org.art.web.warrior.client.dto;

import lombok.*;
import org.art.web.warrior.client.domain.ClientResponseStatus;

import java.util.Map;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompServiceResponse {

    private int compilerStatusCode;

    private String compilerStatus;

    private String message;

    private String compilerMessage;

    private String compilerErrorCode;

    private long errorCodeLine;

    private long errorColumnNumber;

    private long errorPosition;

    Map<String, UnitResult> compUnitResults;

    public boolean isCompError() {
        return ClientResponseStatus.COMPILATION_ERROR.getStatusId().equals(compilerStatus);
    }

    public boolean isCompOk() {
        return ClientResponseStatus.SUCCESS.getStatusId().equals(compilerStatus);
    }
}

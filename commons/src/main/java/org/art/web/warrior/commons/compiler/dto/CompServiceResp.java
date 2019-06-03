package org.art.web.warrior.commons.compiler.dto;

import lombok.*;
import org.art.web.warrior.commons.ServiceResponseStatus;

import java.util.Map;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompServiceResp {

    private int compilerStatusCode;

    private String compilerStatus;

    private String message;

    private String compilerMessage;

    private String compilerErrorCode;

    private long errorCodeLine;

    private long errorColumnNumber;

    private long errorPosition;

    private Map<String, CompilationUnitResp> compUnitResults;

    public boolean isCompError() {
        return ServiceResponseStatus.COMPILATION_ERROR.getStatusId().equals(compilerStatus);
    }

    public boolean isCompOk() {
        return ServiceResponseStatus.SUCCESS.getStatusId().equals(compilerStatus);
    }

    public boolean hasErrors() {
        return !isCompOk();
    }
}

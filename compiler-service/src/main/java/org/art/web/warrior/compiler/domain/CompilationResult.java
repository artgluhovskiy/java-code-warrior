package org.art.web.warrior.compiler.domain;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.art.web.warrior.commons.compiler.CompServiceRespStatus;
import org.art.web.warrior.commons.compiler.dto.CompServiceUnitResponse;

import java.util.Map;

/**
 * Represents a model for compilation result.
 * Contains information related to status, diagnostics and compiled class data.
 */
@ToString
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class CompilationResult {

    private final CompServiceRespStatus status;

    private CompilationMessage message;

    private Map<String, CompServiceUnitResponse> compUnitResults;

    public CompServiceRespStatus getCompStatus() {
        return status;
    }

    public CompilationMessage getMessage() {
        return message;
    }

    public Map<String, CompServiceUnitResponse> getCompUnitResults() {
        return compUnitResults;
    }
}

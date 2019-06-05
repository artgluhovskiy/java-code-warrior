package org.art.web.warrior.compiler.domain;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.art.web.warrior.commons.ServiceResponseStatus;

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

    private final ServiceResponseStatus status;

    private CompilationMessage message;

    private Map<String, CompilationUnit> compUnitResults;

    public ServiceResponseStatus getCompStatus() {
        return status;
    }

    public CompilationMessage getMessage() {
        return message;
    }

    public Map<String, CompilationUnit> getCompUnitResults() {
        return compUnitResults;
    }
}

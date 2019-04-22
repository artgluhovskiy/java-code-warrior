package org.art.web.warrior.compiler.domain;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    private final CompilationStatus status;

    private CompilationMessage message;

    private Map<String, UnitResult> compUnitResults;

    public CompilationStatus getCompStatus() {
        return status;
    }

    public CompilationMessage getMessage() {
        return message;
    }

    public Map<String, UnitResult> getCompUnitResults() {
        return compUnitResults;
    }
}

package org.art.web.warrior.compiler.domain;

import lombok.Data;
import org.art.web.warrior.commons.ServiceResponseStatus;

import java.util.Map;

/**
 * Represents a model for compilation result.
 * Contains information related to status, diagnostics and compiled class data.
 */

@Data
public class CompilationResult {

    private final ServiceResponseStatus status;

    private CompilationMessage message;

    private Map<String, CompilationUnit> compUnitResults;
}

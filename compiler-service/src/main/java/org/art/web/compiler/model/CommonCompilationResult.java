package org.art.web.compiler.model;

import lombok.Setter;
import lombok.ToString;
import org.art.web.compiler.model.api.CompilationMessage;
import org.art.web.compiler.model.api.CompilationResult;
import org.art.web.compiler.model.api.CompilationStatus;

import java.util.Map;

/**
 * Represents a model for compilation result.
 * Contains information related to status, diagnostics and compiled class data.
 */
@ToString
@Setter
public class CommonCompilationResult implements CompilationResult {

    private final CompilationStatus status;

    private CompilationMessage message;

    private Map<String, byte[]> compiledClassData;

    public CommonCompilationResult(CompilationStatus status) {
        this.status = status;
    }

    @Override
    public CompilationStatus getStatus() {
        return status;
    }

    @Override
    public CompilationMessage getMessage() {
        return message;
    }

    @Override
    public Map<String, byte[]> getCompiledClassData() {
        return compiledClassData;
    }
}

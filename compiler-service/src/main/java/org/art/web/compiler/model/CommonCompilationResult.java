package org.art.web.compiler.model;

import lombok.Setter;
import lombok.ToString;
import org.art.web.compiler.model.api.CompilationMessage;
import org.art.web.compiler.model.api.CompilationResult;
import org.art.web.compiler.model.api.CompilationStatus;

/**
 * Represents a model for compilation result.
 * Contains information related to status, diagnostics and compiled class data.
 */
@ToString
@Setter
public class CommonCompilationResult implements CompilationResult {

    private String className;

    private Object srcCode;

    private final CompilationStatus status;

    private CompilationMessage message;

    private byte[] compiledClassBytes;

    public CommonCompilationResult(CompilationStatus status) {
        this.status = status;
    }

    @Override
    public String getClassName() {
        return className;
    }

    @Override
    public Object getSrcCode() {
        return srcCode;
    }

    @Override
    public CompilationStatus getCompStatus() {
        return status;
    }

    @Override
    public CompilationMessage getMessage() {
        return message;
    }

    @Override
    public byte[] getCompiledClassBytes() {
        return compiledClassBytes;
    }
}

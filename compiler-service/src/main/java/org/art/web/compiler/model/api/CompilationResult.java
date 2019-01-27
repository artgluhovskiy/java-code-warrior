package org.art.web.compiler.model.api;

import org.art.web.compiler.service.api.CompilationService;

/**
 * Represents API for compilation result model produced by {@link CompilationService}.
 */
public interface CompilationResult {

    CompilationStatus getStatus();

    CompilationMessage getMessage();

    Class<?> getCompiledClass();
}

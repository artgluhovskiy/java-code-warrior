package org.art.web.warrior.compiler.model.api;

import org.art.web.warrior.common.compiler.CompilationStatus;
import org.art.web.warrior.compiler.service.api.CompilationService;

/**
 * Represents API for compilation result model produced by {@link CompilationService}.
 */
public interface CompilationResult {

    String getClassName();

    Object getSrcCode();

    CompilationStatus getCompStatus();

    CompilationMessage getMessage();

    byte[] getCompiledClassBytes();
}

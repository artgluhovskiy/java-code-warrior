package org.art.web.compiler.model.api;

import org.art.web.compiler.service.api.CompilationService;

/**
 * Represents API for compilation unit model consumed by {@link CompilationService}.
 * @param <T> type of source code content.
 */
public interface CompilationUnit<T> {

    String getClassName();

    T getSrcCode();

    boolean isValid();
}

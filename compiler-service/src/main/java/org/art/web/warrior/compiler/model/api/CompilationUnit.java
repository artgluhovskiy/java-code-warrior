package org.art.web.warrior.compiler.model.api;

import org.art.web.warrior.compiler.service.api.CompilationService;

/**
 * Represents API for compilation unit model consumed by {@link CompilationService}.
 * @param <T> type of source code content.
 */
public interface CompilationUnit<T> {

    String getClassName();

    T getSrcCode();

    boolean isValid();
}

package org.art.web.compiler.model.api;

import javax.tools.Diagnostic;

/**
 * Represents API for compilation message model used by {@link CompilationResult}.
 */
public interface CompilationMessage {

    Diagnostic.Kind getKind();

    String getErrorCode();

    String getCauseMessage();

    long getCodeLine();

    long getColumnNumber();

    long getPosition();
}

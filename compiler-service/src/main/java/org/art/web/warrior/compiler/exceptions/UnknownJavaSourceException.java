package org.art.web.warrior.compiler.exceptions;

import org.art.web.warrior.compiler.service.api.CompilationService;

/**
 * Indicates that a java source content type is not supported by {@link CompilationService}.
 */
public class UnknownJavaSourceException extends RuntimeException {

    private Class<?> srcType;

    public UnknownJavaSourceException(String message, Class<?> srcType) {
        super(message);
        this.srcType = srcType;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " Source type: " + this.srcType;
    }
}

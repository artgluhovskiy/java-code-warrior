package org.art.web.compiler.model;

import lombok.*;

/**
 * Simple POJO class, which contains results of source unit compilation.
 */
@Data
public class CompilationResult {

    private final CompilationStatus status;

    private CompilationMessage message;

    private Class<?> compiledClass;
}

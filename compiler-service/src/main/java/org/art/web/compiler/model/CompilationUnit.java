package org.art.web.compiler.model;

import lombok.*;

/**
 * Represents a model for compilation task, which contains all necessary information.
 * Contains java source code as a character sequence.
 */
@Data
public class CompilationUnit {

    private final String className;

    private final CharSequence srcCode;
}

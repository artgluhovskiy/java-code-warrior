package org.art.web.warrior.compiler.domain;

import lombok.Data;

/**
 * Represents a model for compilation task, which contains all necessary information.
 * Contains java source code with a character sequence type.
 */
@Data
public class CompilationUnit {

    private final String className;

    private final CharSequence srcCode;

    private byte[] compiledClassBytes;
}

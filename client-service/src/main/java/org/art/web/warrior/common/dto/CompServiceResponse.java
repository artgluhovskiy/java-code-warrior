package org.art.web.warrior.common.dto;

import lombok.*;
import org.art.web.warrior.common.compiler.CompilationStatus;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CompServiceResponse {

    private int compilerStatusCode;

    private CompilationStatus compilerStatus;

    private String message;

    private String compilerMessage;

    private String compilerErrorCode;

    private long errorCodeLine;

    private long errorColumnNumber;

    private long errorPosition;

    private String className;

    private Object srcCode;

    private byte[] compiledClass;

    public boolean isCompError() {
        return compilerStatus == CompilationStatus.ERROR;
    }

    public boolean isCompOk() {
        return compilerStatus == CompilationStatus.SUCCESS;
    }
}

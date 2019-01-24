package org.art.web.compiler.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.tools.Diagnostic;

import static org.art.web.compiler.service.ServiceConstants.*;

/**
 * Contains information related to the java source unit compilation.
 */
@Data
@Builder
public class CompilationMessage {

    @NonNull
    private Diagnostic.Kind kind;

    @NonNull
    private String errorCode;

    @NonNull
    private String causeMessage;

    private long codeLine;

    private long columnNumber;

    private long position;

    @Override
    public String toString() {
        return kind + " occurred!" + NEW_LINE_CH +
                "At line: " + codeLine + COMMA_CH + NEW_LINE_CH +
                "at column: " + columnNumber + COMMA_CH + NEW_LINE_CH +
                "at position: " + position + DOT_CH + NEW_LINE_CH +
                "Error message: " + causeMessage + DOT_CH + NEW_LINE_CH;
    }
}

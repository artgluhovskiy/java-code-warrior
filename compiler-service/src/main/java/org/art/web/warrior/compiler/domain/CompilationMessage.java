package org.art.web.warrior.compiler.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.tools.Diagnostic;

import static org.art.web.warrior.commons.CommonConstants.*;

/**
 * Contains information related to the java source unit compilation.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CompilationMessage {

    private Diagnostic.Kind kind;

    private String errorCode;

    private String causeMessage;

    private long codeLine;

    private long columnNumber;

    private long position;

    public CompilationMessage(Diagnostic.Kind kind, String errorCode, String causeMessage) {
        this.kind = kind;
        this.errorCode = errorCode;
        this.causeMessage = causeMessage;
    }

    public Diagnostic.Kind getKind() {
        return kind;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getCauseMessage() {
        return causeMessage;
    }

    public long getCodeLine() {
        return codeLine;
    }

    public long getColumnNumber() {
        return columnNumber;
    }

    public long getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return kind + " occurred!" + NEW_LINE_CH +
                "At line: " + codeLine + COMMA_CH + NEW_LINE_CH +
                "at column: " + columnNumber + COMMA_CH + NEW_LINE_CH +
                "at position: " + position + DOT_CH + NEW_LINE_CH +
                "Error message: " + causeMessage + DOT_CH + NEW_LINE_CH;
    }
}

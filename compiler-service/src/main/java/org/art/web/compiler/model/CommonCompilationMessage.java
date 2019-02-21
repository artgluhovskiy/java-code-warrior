package org.art.web.compiler.model;

import lombok.*;
import org.art.web.compiler.model.api.CompilationMessage;

import javax.tools.Diagnostic;

import static org.art.web.compiler.service.ServiceCommonConstants.*;

/**
 * Contains information related to the java source unit compilation.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CommonCompilationMessage implements CompilationMessage {

    @NonNull
    private Diagnostic.Kind kind;

    @NonNull
    private String errorCode;

    @NonNull
    private String causeMessage;

    private long codeLine;

    private long columnNumber;

    private long position;

    public CommonCompilationMessage(@NonNull Diagnostic.Kind kind, @NonNull String errorCode, @NonNull String causeMessage) {
        this.kind = kind;
        this.errorCode = errorCode;
        this.causeMessage = causeMessage;
    }

    @Override
    public Diagnostic.Kind getKind() {
        return kind;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getCauseMessage() {
        return causeMessage;
    }

    @Override
    public long getCodeLine() {
        return codeLine;
    }

    @Override
    public long getColumnNumber() {
        return columnNumber;
    }

    @Override
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

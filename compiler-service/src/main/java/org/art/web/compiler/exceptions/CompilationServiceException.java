package org.art.web.compiler.exceptions;

import org.art.web.compiler.model.api.CompilationUnit;
import org.art.web.compiler.service.api.CompilationService;

import static org.art.web.compiler.service.ServiceCommonConstants.DOT_CH;
import static org.art.web.compiler.service.ServiceCommonConstants.NEW_LINE_CH;

/**
 * Thrown to indicate that {@link CompilationService}
 * failed to compile compilation unit for some internal reasons.
 */
public class CompilationServiceException extends Exception {

    private CompilationUnit<?> unit;

    public CompilationServiceException(String message, CompilationUnit<?> unit) {
        super(message);
        this.unit = unit;
    }

    public CompilationServiceException(String message, CompilationUnit<?> unit, Throwable cause) {
        super(message, cause);
        this.unit = unit;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (this.unit != null) {
            StringBuilder sb = new StringBuilder(message);
            sb.append(" Compilation unit: class name - ").append(this.unit.getClassName()).append(NEW_LINE_CH)
                    .append("source code - ").append(this.unit.getSrcCode()).append(DOT_CH).append(NEW_LINE_CH);
            message = sb.toString();
        }
        return message;
    }
}

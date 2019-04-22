package org.art.web.warrior.compiler.exception;

import org.art.web.warrior.compiler.domain.CompilationUnit;
import org.art.web.warrior.compiler.service.api.CompilationService;

import java.util.List;

import static org.art.web.warrior.compiler.ServiceCommonConstants.DOT_CH;
import static org.art.web.warrior.compiler.ServiceCommonConstants.NEW_LINE_CH;

/**
 * Indicates that {@link CompilationService} failed to compile compilation unit for some internal reasons.
 */
public class CompilationServiceException extends Exception {

    private final List<CompilationUnit> units;

    public CompilationServiceException(String message, List<CompilationUnit> units) {
        super(message);
        this.units = units;
    }

    public CompilationServiceException(String message, List<CompilationUnit> units, Throwable cause) {
        super(message, cause);
        this.units = units;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (this.units != null) {
            StringBuilder sb = new StringBuilder(message);
            units.forEach(unit -> sb.append(" Compilation unit: class name - ")
                    .append(unit.getClassName()).append(NEW_LINE_CH)
                    .append("source code - ").append(unit.getSrcCode()).append(DOT_CH).append(NEW_LINE_CH));
            message = sb.toString();
        }
        return message;
    }
}

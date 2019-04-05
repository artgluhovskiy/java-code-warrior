package org.art.web.compiler.exceptions;

import org.art.web.compiler.model.api.MethodDescriptor;

import static org.art.web.compiler.service.ServiceCommonConstants.DOT_CH;
import static org.art.web.compiler.service.ServiceCommonConstants.NEW_LINE_CH;

/**
 * Thrown to indicate that {@link org.art.web.compiler.service.api.MethodInvocationService}
 * failed to invoke target method for some internal reasons.
 */
public class MethodInvocationException extends Exception {

    private MethodDescriptor mDescriptor;

    public MethodInvocationException(String message, MethodDescriptor mDescriptor, Throwable cause) {
        super(message, cause);
        this.mDescriptor = mDescriptor;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (this.mDescriptor != null) {
            StringBuilder sb = new StringBuilder(message);
            sb.append("Method descriptor: target instance class - ").append(this.mDescriptor.getTargetInstance().getClass()).append(NEW_LINE_CH)
                    .append("method name - ").append(this.mDescriptor.getMethodName()).append(NEW_LINE_CH)
                    .append("return type - ").append(this.mDescriptor.getReturnType()).append(NEW_LINE_CH)
                    .append("arguments - ").append(this.mDescriptor.getArgs()).append(DOT_CH).append(NEW_LINE_CH);
        }
        return message;
    }
}

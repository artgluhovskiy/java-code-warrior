package org.art.web.warrior.invoke.exceptions;

import org.art.web.warrior.invoke.domain.api.MethodDescriptor;
import org.art.web.warrior.invoke.service.api.MethodInvocationService;

import static org.art.web.warrior.invoke.ServiceCommonConstants.DOT_CH;
import static org.art.web.warrior.invoke.ServiceCommonConstants.NEW_LINE_CH;

/**
 * Thrown to indicate that {@link MethodInvocationService}
 * failed to invoke target method for some internal reasons.
 */
public class MethodInvocationException extends Exception {

    private final MethodDescriptor mDescriptor;

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

package org.art.web.warrior.exec.exception;

import org.art.web.warrior.exec.domain.api.MethodDescriptor;
import org.art.web.warrior.exec.service.api.MethodInvocationService;

import static org.art.web.warrior.commons.CommonConstants.DOT_CH;
import static org.art.web.warrior.commons.CommonConstants.NEW_LINE_CH;

/**
 * Thrown to indicate that {@link MethodInvocationService}
 * failed to invoke target method for some internal reasons.
 */
public class MethodInvocationException extends RuntimeException {

    private final MethodDescriptor mDescriptor;

    public MethodInvocationException(String message, MethodDescriptor mDescriptor) {
        super(message);
        this.mDescriptor = mDescriptor;
    }

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

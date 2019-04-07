package org.art.web.invoke.exceptions;

import org.art.web.invoke.model.api.MethodDescriptor;
import org.art.web.invoke.service.ServiceCommonConstants;
import org.art.web.invoke.service.api.MethodInvocationService;

/**
 * Thrown to indicate that {@link MethodInvocationService}
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
            sb.append("Method descriptor: target instance class - ").append(this.mDescriptor.getTargetInstance().getClass()).append(ServiceCommonConstants.NEW_LINE_CH)
                    .append("method name - ").append(this.mDescriptor.getMethodName()).append(ServiceCommonConstants.NEW_LINE_CH)
                    .append("return type - ").append(this.mDescriptor.getReturnType()).append(ServiceCommonConstants.NEW_LINE_CH)
                    .append("arguments - ").append(this.mDescriptor.getArgs()).append(ServiceCommonConstants.DOT_CH).append(ServiceCommonConstants.NEW_LINE_CH);
        }
        return message;
    }
}

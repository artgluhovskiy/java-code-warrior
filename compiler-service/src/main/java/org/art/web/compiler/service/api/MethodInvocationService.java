package org.art.web.compiler.service.api;

import org.art.web.compiler.exceptions.MethodInvocationException;
import org.art.web.compiler.model.api.MethodDescriptor;

/**
 * Method Invocation Service API.
 * Core service interface, which provides simple API to invoke java method
 * based on information, encapsulated in {@link MethodDescriptor}.
 */
public interface MethodInvocationService {

    Object invokeMethod(MethodDescriptor descriptor) throws MethodInvocationException;
}

package org.art.web.warrior.exec.service.api;

import org.art.web.warrior.exec.domain.api.MethodDescriptor;

/**
 * Method Invocation Service API.
 * Core service interface, which provides simple API to invoke java method
 * based on information, encapsulated in {@link MethodDescriptor}.
 */
public interface MethodInvocationService {

    Object invokeMethod(MethodDescriptor descriptor);
}

package org.art.web.compiler.service;

import org.art.web.compiler.model.MethodDescriptor;

public interface MethodInvocationService {

    Object invokeVirtual(MethodDescriptor descriptor) throws Throwable;
}

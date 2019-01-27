package org.art.web.compiler.model.api;

import org.apache.commons.lang3.tuple.Pair;
import org.art.web.compiler.service.api.MethodInvocationService;

import java.util.List;

/**
 * Represents API for method descriptor model consumed by {@link MethodInvocationService}.
 */
public interface MethodDescriptor {

    Object getTargetInstance();

    String getMethodName();

    Class<?> getReturnType();

    List<Pair<Object, Class<?>>> getArgs();
}

package org.art.web.warrior.exec.domain.api;

import org.apache.commons.lang3.tuple.Pair;
import org.art.web.warrior.exec.service.api.MethodInvocationService;

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

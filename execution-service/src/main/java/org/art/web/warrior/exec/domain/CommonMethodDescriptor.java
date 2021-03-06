package org.art.web.warrior.exec.domain;

import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.art.web.warrior.exec.domain.api.MethodDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a model, which contains all the information related to method to invoke.
 */
@Data
public class CommonMethodDescriptor implements MethodDescriptor {

    private final Object targetInstance;

    private final String methodName;

    private Class<?> returnType;

    private List<Pair<Object, Class<?>>> args;

    @Override
    public Object getTargetInstance() {
        return targetInstance;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public Class<?> getReturnType() {
        return returnType;
    }

    @Override
    public List<Pair<Object, Class<?>>> getArgs() {
        return args != null ? new ArrayList<>(args) : Collections.emptyList();
    }
}

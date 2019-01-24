package org.art.web.compiler.model;

import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Represents a model, which contains all the information related to method to invoke.
 */
@Data
public class MethodDescriptor {

    private final Object targetInstance;

    private final String methodName;

    private Class<?> returnType;

    private List<Pair<Object, Class<?>>> args;
}

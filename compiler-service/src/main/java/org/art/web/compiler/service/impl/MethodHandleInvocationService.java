package org.art.web.compiler.service.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.art.web.compiler.model.MethodDescriptor;
import org.art.web.compiler.service.MethodInvocationService;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MethodHandleInvocationService implements MethodInvocationService {

    private static final Logger LOG = LogManager.getLogger(MethodHandleInvocationService.class);

    private static final MethodHandles.Lookup PUBLIC_LOOKUP = MethodHandles.publicLookup();

    @Override
    public Object invokeVirtual(MethodDescriptor descriptor) throws Throwable {
        Objects.requireNonNull(descriptor, "Method descriptor should not be null!");
        Object instance = descriptor.getTargetInstance();
        Class<?> clazz = instance.getClass();
        String methodName = descriptor.getMethodName();
        List<Pair<Object, Class<?>>> args = descriptor.getArgs();
        Class<?> retType = descriptor.getReturnType();
        LOG.debug("Method invocation. Class: {}, method: {}, ret type: {}, args: {}",
                clazz.getName(), methodName, retType, args);
        List<Class<?>> inTypes = retrieveInputTypes(args);
        List<Object> inArgValues = retrieveInputArgValues(args);
        MethodType methodType = MethodType.methodType(retType, inTypes);
        MethodHandle methodHan = PUBLIC_LOOKUP.findVirtual(clazz, methodName, methodType);
        MethodHandle methodHandle = methodHan.bindTo(instance);
        return methodHandle.invokeWithArguments(inArgValues);
    }

    private List<Class<?>> retrieveInputTypes(List<Pair<Object, Class<?>>> args) {
        List<Class<?>> inputTypes = Collections.emptyList();
        if (args != null && !args.isEmpty()) {
            inputTypes = args.stream()
                    .map(Pair::getRight)
                    .collect(Collectors.toList());
        }
        return inputTypes;
    }

    private List<Object> retrieveInputArgValues(List<Pair<Object, Class<?>>> args) {
        List<Object> inputArgValues = Collections.emptyList();
        if (args != null && !args.isEmpty()) {
            inputArgValues = args.stream()
                    .map(Pair::getLeft)
                    .collect(Collectors.toList());
        }
        return inputArgValues;
    }
}

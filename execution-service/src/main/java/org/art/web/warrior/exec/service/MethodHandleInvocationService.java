package org.art.web.warrior.exec.service;

import org.apache.commons.lang3.tuple.Pair;
import org.art.web.warrior.commons.driver.exception.ClientCodeExecutionException;
import org.art.web.warrior.exec.domain.api.MethodDescriptor;
import org.art.web.warrior.exec.exception.MethodInvocationException;
import org.art.web.warrior.exec.service.api.MethodInvocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Method invocation service implementation.
 * Provides java method invocation based on {@link MethodDescriptor},
 * which encapsulates all necessary data.
 * Current implementation is based on Java Method Handle API.
 */
@Service
public class MethodHandleInvocationService implements MethodInvocationService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandleInvocationService.class);

    private static final MethodHandles.Lookup PUBLIC_LOOKUP = MethodHandles.publicLookup();

    @Override
    public Object invokeMethod(MethodDescriptor descriptor) throws MethodInvocationException {
        Objects.requireNonNull(descriptor, "Method descriptor should not be null!");
        Object instance = descriptor.getTargetInstance();
        String methodName = descriptor.getMethodName();
        if (instance == null || methodName == null) {
            throw new MethodInvocationException("Method target instance and method name should not be null!", descriptor);
        }
        Class<?> clazz = instance.getClass();
        List<Pair<Object, Class<?>>> args = descriptor.getArgs();
        Class<?> retType = descriptor.getReturnType();
        if (retType == null) {
            retType = void.class;
        }
        LOG.debug("Method invocation. Class: {}, method: {}, ret type: {}, args: {}", clazz.getName(), methodName, retType, args);
        List<Class<?>> inTypes = retrieveInputTypes(args);
        List<Object> inArgValues = retrieveInputArgValues(args);
        try {
            MethodType methodType = MethodType.methodType(retType, inTypes);
            MethodHandle methodHan = PUBLIC_LOOKUP.findVirtual(clazz, methodName, methodType);
            MethodHandle methodHandle = methodHan.bindTo(instance);
            return methodHandle.invokeWithArguments(inArgValues);
        } catch (ClientCodeExecutionException e) {
            throw e;
        } catch (Throwable t) {
            LOG.warn("Internal invocation error. MethodInvocationException is thrown!");
            throw new MethodInvocationException("Internal invocation error!", descriptor, t);
        }
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

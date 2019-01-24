package org.art.web.compiler.service.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.art.web.compiler.model.MethodDescriptor;
import org.art.web.compiler.service.MethodInvoker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodHandleInvoker implements MethodInvoker {

    @Override
    public Object invokeVirtual(MethodDescriptor descriptor) throws Throwable {
        Objects.requireNonNull(descriptor, "Method descriptor should not be null!");
        MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        List<Class<?>> inputTypes = descriptor.getArgs()
                .stream()
                .map(Pair::getRight)
                .collect(Collectors.toList());
        List<Object> inputArgValues = descriptor.getArgs()
                .stream()
                .map(Pair::getLeft)
                .collect(Collectors.toList());
        MethodType methodType = MethodType.methodType(descriptor.getReturnType(), inputTypes);
        Object targetInstance = descriptor.getTargetInstance();
        String methodName = descriptor.getMethodName();
        MethodHandle methoddHan = lookup.findVirtual(targetInstance.getClass(), methodName, methodType);
        MethodHandle methodHandle = methoddHan.bindTo(targetInstance);
        return methodHandle.invokeWithArguments(inputArgValues);
    }
}

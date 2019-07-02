package org.art.web.warrior.compiler.service;

import lombok.extern.slf4j.Slf4j;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.StandardJavaFileManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple implementation of a wrapper for {@link StandardJavaFileManager}.
 * Stores a Java file object as an output for compiled source code.
 */
@Slf4j
public final class CustomClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

    private final Map<String, OutputJavaClassFileObject> classFiles;

    public CustomClassFileManager(StandardJavaFileManager fileManager) {
        super(fileManager);
        this.classFiles = new HashMap<>();
    }

    @Override
    public OutputJavaClassFileObject getJavaFileForOutput(Location location,
                                                          String className,
                                                          OutputJavaClassFileObject.Kind kind,
                                                          FileObject sibling) {
        OutputJavaClassFileObject classFileObject = new OutputJavaClassFileObject(className, kind);
        log.debug("Java class file object was instantiated: class name - {}", className);
        classFiles.put(className, classFileObject);
        return classFileObject;
    }

    public Map<String, OutputJavaClassFileObject> getClassFiles() {
        return Collections.unmodifiableMap(this.classFiles);
    }
}

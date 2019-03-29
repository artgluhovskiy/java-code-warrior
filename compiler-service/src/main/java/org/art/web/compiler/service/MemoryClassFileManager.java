package org.art.web.compiler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public final class MemoryClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

    private static final Logger LOG = LoggerFactory.getLogger(MemoryClassFileManager.class);

    private final Map<String, MemoryJavaFileObject> classFiles;

    public MemoryClassFileManager(StandardJavaFileManager fileManager) {
        super(fileManager);
        classFiles = new HashMap<>();
    }

    @Override
    public MemoryJavaFileObject getJavaFileForOutput(Location location,
                                                     String className,
                                                     MemoryJavaFileObject.Kind kind,
                                                     FileObject sibling) {
        MemoryJavaFileObject classFileObject = new MemoryJavaFileObject(className, kind);
        LOG.debug("Java class file object was instantiated: class name - {}", className);
        classFiles.put(className, classFileObject);
        return classFileObject;
    }

    public Map<String, MemoryJavaFileObject> getClassFiles() {
        return Collections.unmodifiableMap(this.classFiles);
    }
}

package org.art.web.warrior.compiler.service;

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
public final class CustomClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

    private final Map<String, CustomJavaClassFileObject> classFiles;

    public CustomClassFileManager(StandardJavaFileManager fileManager) {
        super(fileManager);
        this.classFiles = new HashMap<>();
    }

    @Override
    public CustomJavaClassFileObject getJavaFileForOutput(Location location,
                                                          String className,
                                                          CustomJavaClassFileObject.Kind kind,
                                                          FileObject sibling) {
        CustomJavaClassFileObject classFileObject = new CustomJavaClassFileObject(className, kind);
        classFiles.put(className, classFileObject);
        return classFileObject;
    }

    public Map<String, CustomJavaClassFileObject> getClassFiles() {
        return Collections.unmodifiableMap(this.classFiles);
    }
}

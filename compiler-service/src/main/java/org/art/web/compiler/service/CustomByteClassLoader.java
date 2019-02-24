package org.art.web.compiler.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Simple class loader implementation.
 * Provides loading classes from compiled byte file objects.
 */
public class CustomByteClassLoader extends ClassLoader {

    private static final Logger LOG = LogManager.getLogger(CustomByteClassLoader.class);

    private final Map<String, byte[]> classFiles;

    public CustomByteClassLoader(Map<String, byte[]> classFiles) {
        this.classFiles = classFiles;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        LOG.debug("Finding class with name: {}", className);
        if (classFiles.containsKey(className)) {
            byte[] classFile = classFiles.get(className);
            Class<?> clazz = defineClass(className, classFile, 0, classFile.length);
            classFiles.remove(className);
            return clazz;
        }
        LOG.debug("Class with the name {} wasn't found in the class files map. " +
                  "Delegating invocation to the super class.", className);
        return super.findClass(className);
    }

    public byte[] getClassBinData(String className) {
        LOG.debug("Getting class binary data for class with name: {}", className);
        byte[] classBinData = new byte[0];
        if (classFiles.containsKey(className)) {
            classBinData = classFiles.get(className);
        }
        return classBinData;
    }
}

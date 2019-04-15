package org.art.web.warrior.common.classloading;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple class loader implementation.
 * Provides loading classes from compiled byte file objects.
 */
public class CustomByteClassLoader extends ClassLoader {

    private static final Logger LOG = LoggerFactory.getLogger(CustomByteClassLoader.class);

    private final Map<String, byte[]> classData = new HashMap<>();

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        LOG.debug("Finding class with name: {}", className);
        if (classData.containsKey(className)) {
            byte[] classFile = classData.get(className);
            Class<?> clazz = defineClass(className, classFile, 0, classFile.length);
            classData.remove(className);
            return clazz;
        }
        LOG.info("Class with the name {} wasn't found in the class files map. Returning null.", className);
        return null;
    }

    public void addClassData(String className, byte[] compiledClass) {
        if (StringUtils.isNotBlank(className) && ArrayUtils.isNotEmpty(compiledClass)) {
            classData.put(className, compiledClass);
        }
    }
}

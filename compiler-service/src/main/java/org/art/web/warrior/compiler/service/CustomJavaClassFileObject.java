package org.art.web.warrior.compiler.service;

import javax.tools.JavaFileManager;
import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

import static org.art.web.warrior.commons.CommonConstants.DOT_CH;
import static org.art.web.warrior.commons.CommonConstants.SLASH_CH;
import static org.art.web.warrior.compiler.ServiceCommonConstants.JAVA_STRING_SCHEME;

/**
 * Provides a simple implementation of a java class file.
 * Is used as a dependency class file object for a {@link JavaFileManager}.
 */
public final class CustomJavaClassFileObject extends SimpleJavaFileObject {

    private final String name;

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    public CustomJavaClassFileObject(String name, Kind kind) {
        super(URI.create(JAVA_STRING_SCHEME + name.replace(DOT_CH, SLASH_CH) + kind.extension), kind);
        this.name = name;
    }

    public byte[] getBytes() {
        return out.toByteArray();
    }

    @Override
    public OutputStream openOutputStream() {
        return out;
    }

    @Override
    public String getName() {
        return name;
    }
}

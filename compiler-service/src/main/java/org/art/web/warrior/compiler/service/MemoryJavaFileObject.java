package org.art.web.warrior.compiler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

import static org.art.web.warrior.compiler.service.ServiceCommonConstants.DOT_CH;
import static org.art.web.warrior.compiler.service.ServiceCommonConstants.SLASH_CH;

/**
 * Provides a simple implementation of a Java file object based on byte stream.
 * Is used as an input/output file object for File Manager in Java Compiler API.
 */
public final class MemoryJavaFileObject extends SimpleJavaFileObject {

    private static final Logger LOG = LoggerFactory.getLogger(MemoryJavaFileObject.class);

    private static final String JAVA_STRING_SCHEME = "string:///";

    private final String name;

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    public MemoryJavaFileObject(String name, Kind kind) {
        super(URI.create(JAVA_STRING_SCHEME + name.replace(DOT_CH, SLASH_CH) + kind.extension), kind);
        this.name = name;
        LOG.debug("Creating java file object for '{}' with kind '{}'", name, kind);
    }

    public byte[] getBytes() {
        return out.toByteArray();
    }

    @Override
    public OutputStream openOutputStream() {
        return this.out;
    }

    @Override
    public String getName() {
        return name;
    }
}
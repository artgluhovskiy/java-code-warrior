package org.art.web.warrior.compiler.service;

import lombok.extern.slf4j.Slf4j;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

import static org.art.web.warrior.commons.CommonConstants.DOT_CH;
import static org.art.web.warrior.commons.CommonConstants.SLASH_CH;

/**
 * Provides a simple implementation of a Java file object based on byte stream.
 * Is used as an input/output file object for File Manager in Java Compiler API.
 */
@Slf4j
public final class OutputJavaClassFileObject extends SimpleJavaFileObject {

    private static final String JAVA_STRING_SCHEME = "string:///";

    private final String name;

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    public OutputJavaClassFileObject(String name, Kind kind) {
        super(URI.create(JAVA_STRING_SCHEME + name.replace(DOT_CH, SLASH_CH) + kind.extension), kind);
        this.name = name;
        log.debug("Creating output java class file object for '{}' with kind '{}'", name, kind);
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

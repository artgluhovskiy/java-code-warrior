package org.art.web.warrior.compiler.service;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

import static org.art.web.warrior.compiler.service.ServiceCommonConstants.DOT_CH;
import static org.art.web.warrior.compiler.service.ServiceCommonConstants.SLASH_CH;

/**
 * Provides a simple implementation of a Java source file based on character sequence.
 * Is used as a compilation unit for Java Compiler API.
 */
public final class CharSequenceJavaFileObject extends SimpleJavaFileObject {

    private static final String JAVA_STRING_SCHEME = "string:///";

    private final CharSequence srcCode;

    public CharSequenceJavaFileObject(String srcClassName, CharSequence srcCode) {
        super(URI.create(JAVA_STRING_SCHEME + srcClassName.replace(DOT_CH, SLASH_CH) + Kind.SOURCE.extension), Kind.SOURCE);
        this.srcCode = srcCode;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return this.srcCode;
    }
}

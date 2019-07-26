package org.art.web.warrior.compiler.service;

import javax.tools.JavaFileManager;
import javax.tools.SimpleJavaFileObject;
import java.net.URI;

import static org.art.web.warrior.commons.CommonConstants.DOT_CH;
import static org.art.web.warrior.commons.CommonConstants.SLASH_CH;
import static org.art.web.warrior.compiler.ServiceCommonConstants.JAVA_STRING_SCHEME;

/**
 * Provides a simple implementation of a java source file based on character sequence.
 * Is used as a compilation unit for a {@link JavaFileManager}.
 */
public final class CustomJavaSourceFileObject extends SimpleJavaFileObject {

    private final CharSequence srcCode;

    public CustomJavaSourceFileObject(String srcClassName, CharSequence srcCode) {
        super(URI.create(JAVA_STRING_SCHEME + srcClassName.replace(DOT_CH, SLASH_CH) + Kind.SOURCE.extension), Kind.SOURCE);
        this.srcCode = srcCode;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return this.srcCode;
    }
}

package org.art.web.warrior.compiler.service;

import org.springframework.http.MediaType;

/**
 * Common service constants.
 */
public class ServiceCommonConstants {
    
    private ServiceCommonConstants() {
    }

    public static final char DOT_CH = '.';
    public static final char COMMA_CH = ',';
    public static final char SLASH_CH = '/';
    public static final char NEW_LINE_CH = '\n';
    public static final char QUOTE_CH = '"';

    public static final String COMPILER_SERVICE_OK_MESSAGE = "Compiler Service: OK!";
    public static final String KRYO_CONTENT_TYPE = "application/x-kryo";

    public static final MediaType KRYO_MEDIA_TYPE = new MediaType("application", "x-kryo");
}

package org.art.web.warrior.compiler;

import org.springframework.http.MediaType;

import java.util.regex.Pattern;

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

    public static final String COMPILER_SERVICE_OK_MESSAGE = "Compiler Service: OK!";
    public static final String KRYO_CONTENT_TYPE = "application/x-kryo";

    public static final MediaType KRYO_MEDIA_TYPE = new MediaType("application", "x-kryo");

    public static final Pattern CLASS_NAME_REG_EXP = Pattern.compile("(?<=class[ \\n])(\\w+)");

    //Exception messages
    public static final String COMP_UNITS_ARG_SHOULD_NOT_BE_NULL_MESSAGE = "Compilation units argument should not be null!";
    public static final String UNEXPECTED_INTERNAL_ERROR_MESSAGE = "Unexpected error occurred while units compilation!";
    public static final String NOT_VALID_COMPILATION_UNITS_MESSAGE = "Failed to compile units. Compilation units are not valid!";
    public static final String REQUEST_DATA_CANNOT_BE_PROCESSED_MESSAGE = "Client request data cannot be processed!";
}

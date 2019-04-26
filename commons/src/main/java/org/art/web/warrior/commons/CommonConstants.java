package org.art.web.warrior.commons;

import java.util.regex.Pattern;

public class CommonConstants {

    private CommonConstants() {}

    public static final char DOT_CH = '.';
    public static final char COMMA_CH = ',';
    public static final char SLASH_CH = '/';
    public static final char NEW_LINE_CH = '\n';
    public static final char QUOTE_CH = '"';

    public static final String SPRING_ACTIVE_PROFILE_ENV_PROP_NAME = "spring.profiles.active";

    public static final String ACTIVE_PROFILE_CONTAINER = "container";

    public static final String LOCALHOST = "localhost";

    public static final Pattern CLASS_NAME_REG_EXP = Pattern.compile("(?<=class[ \\n])(\\w+)");

    public static final String KRYO_CONTENT_TYPE = "application/x-kryo";
}
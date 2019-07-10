package org.art.web.warrior.commons;

import java.util.regex.Pattern;

public class CommonConstants {

    private CommonConstants() {}

    public static final char DOT_CH = '.';
    public static final char COMMA_CH = ',';
    public static final char SLASH_CH = '/';
    public static final char NEW_LINE_CH = '\n';
    public static final char QUOTE_CH = '"';
    public static final char COLON_CH = ':';
    public static final char SPACE_CH = ' ';

    public static final String NEW_LINE = "\n";
    public static final String SPACE = " ";

    public static final String SPRING_ACTIVE_PROFILE_ENV_PROP_NAME = "spring.profiles.active";
    public static final String SPRING_LOCAL_PORT_PROP_NAME = "local.server.port";
    public static final String SPRING_INFO_APP_PROP_NAME = "info.app";
    public static final String SPRING_APPLICATION_NAME_PROP_NAME = "spring.application.name";

    // Spring Profiles
    public static final String PROFILE_CONTAINER = "container";
    public static final String PROFILE_RETROFIT = "retrofit";
    public static final String PROFILE_SINGLE = "single";

    public static final String LOCALHOST = "localhost";

    public static final Pattern CLASS_NAME_REG_EXP = Pattern.compile("(?<=class[ \\n])(\\w+)");

    public static final String KRYO_CONTENT_TYPE = "application/x-kryo";

    public static final String SOLUTION_CLASS_NAME = "Solution";
    public static final String RUNNER_CLASS_NAME = "Runner";

    public static final String SOLUTION_SETTER_METHOD_NAME = "setSolution";
    public static final String RUNNER_RUN_METHOD_NAME = "run";

    public static final String EMAIL_REGEXP = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$";

    //Kafka
    public static final String TASK_SERVICE_TOPIC_NAME = "taskService1";

    public static final String TASK_SERVICE_CONSUMER_GROUP = "taskServiceConsumers";
}

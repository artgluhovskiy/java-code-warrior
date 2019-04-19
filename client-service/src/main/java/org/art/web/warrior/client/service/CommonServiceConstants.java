package org.art.web.warrior.client.service;

import java.text.MessageFormat;

public class CommonServiceConstants {

    private CommonServiceConstants() {}

    public static final String COMPILER_SERVICE_HOST_ENV_PROP_NAME = "COMPILER_SERVICE_HOST";
    public static final String COMPILER_SERVICE_PORT_ENV_PROP_NAME = "COMPILER_SERVICE_PORT";

    public static final String INVOCATION_SERVICE_HOST_ENV_PROP_NAME = "INVOCATION_SERVICE_HOST";
    public static final String INVOCATION_SERVICE_PORT_ENV_PROP_NAME = "INVOCATION_SERVICE_PORT";

    public static final String SPRING_ACTIVE_PROFILE_ENV_PROP_NAME = "spring.profiles.active";

    public static final String ACTIVE_PROFILE_CONTAINER = "container";

    public static final String LOCALHOST = "localhost";

    public static final String COMP_SERVICE_PORT_NO_PROFILE = "8080";

    public static final MessageFormat COMPILATION_SERVICE_ENDPOINT_FORMAT = new MessageFormat("http://{0}:{1}/compiler/compile/src/entity");
    public static final MessageFormat INVOCATION_SERVICE_ENDPOINT_FORMAT = new MessageFormat("http://{0}:{1}/executor/execute");
}

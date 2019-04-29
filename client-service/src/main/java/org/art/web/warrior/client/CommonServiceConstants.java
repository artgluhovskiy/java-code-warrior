package org.art.web.warrior.client;

import java.text.MessageFormat;

public class CommonServiceConstants {

    private CommonServiceConstants() {
    }

    public static final String COMPILER_SERVICE_HOST_ENV_PROP_NAME = "COMPILER_SERVICE_HOST";
    public static final String COMPILER_SERVICE_PORT_ENV_PROP_NAME = "COMPILER_SERVICE_PORT";

    public static final String EXECUTION_SERVICE_HOST_ENV_PROP_NAME = "EXECUTION_SERVICE_HOST";
    public static final String EXECUTION_SERVICE_PORT_ENV_PROP_NAME = "EXECUTION_SERVICE_PORT";

    public static final String TASK_SERVICE_HOST_ENV_PROP_NAME = "TASK_SERVICE_HOST";
    public static final String TASK_SERVICE_PORT_ENV_PROP_NAME = "TASK_SERVICE_PORT";

    public static final String COMP_SERVICE_PORT_NO_PROFILE = "8080";
    public static final String EXEC_SERVICE_PORT_NO_PROFILE = "8081";
    public static final String TASK_SERVICE_PORT_NO_PROFILE = "8083";

    public static final MessageFormat COMPILATION_SERVICE_ENDPOINT_FORMAT = new MessageFormat("http://{0}:{1}/compiler/compile/src/entity");
    public static final MessageFormat EXECUTION_SERVICE_ENDPOINT_FORMAT = new MessageFormat("http://{0}:{1}/executor/execute");
    public static final MessageFormat TASK_SERVICE_ENDPOINT_FORMAT = new MessageFormat("http://{0}:{1}/tasks");

    public static final String UNPROCESSABLE_CLIENT_REQUEST_MESSAGE = "Client request cannot be processed. Client source data is invalid!";
    public static final String INTERNAL_SERVICE_ERROR_MESSAGE = "Internal service error occurred! Compilation service responded with empty body.";
    public static final String COMPILATION_ERROR_MESSAGE = "Compilation errors occurred while compiling client source code!";
    public static final String COMPILATION_OK_MESSAGE = "Client source code was successfully compiled!";
    public static final String TASK_PUBLISHING_OK_MESSAGE = "New coding task was successfully compiled and published!";
}

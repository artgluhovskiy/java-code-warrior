package org.art.web.warrior.client;

import java.text.MessageFormat;

public class CommonServiceConstants {

    private CommonServiceConstants() {
    }

    //View names
    public static final String HOME_VIEW_NAME = "home";
    public static final String LOGIN_VIEW_NAME = "login/login";
    public static final String REGISTRATION_VIEW_NAME = "registration/registration";
    public static final String TASKS_VIEW_NAME = "tasks/tasks";
    public static final String ADMIN_VIEW_NAME = "admin/admin";
    public static final String LAYOUT_VIEW_NAME = "layout/layout";

    public static final String FRAGMENT = "fragment";
    public static final String LOGIN_FRAGMENT = "login";
    public static final String REGISTRATION_FRAGMENT = "registration";
    public static final String SUBMISSION_FRAGMENT = "submission";

    public static final String USER_ATTR_NAME = "user";

    //Request URL Parts
    public static final String USER = "user";
    public static final String ADMIN = "admin";
    public static final String LOGIN = "login";
    public static final String SUBMIT = "submit";
    public static final String REGISTRATION = "registration";

    public static final String COMPILER_SERVICE_HOST_ENV_PROP_NAME = "COMPILER_SERVICE_HOST";
    public static final String COMPILER_SERVICE_PORT_ENV_PROP_NAME = "COMPILER_SERVICE_PORT";

    public static final String EXECUTION_SERVICE_HOST_ENV_PROP_NAME = "EXECUTION_SERVICE_HOST";
    public static final String EXECUTION_SERVICE_PORT_ENV_PROP_NAME = "EXECUTION_SERVICE_PORT";

    public static final String TASK_SERVICE_HOST_ENV_PROP_NAME = "TASK_SERVICE_HOST";
    public static final String TASK_SERVICE_PORT_ENV_PROP_NAME = "TASK_SERVICE_PORT";

    public static final String COMP_SERVICE_PORT_NO_PROFILE = "8080";
    public static final String EXEC_SERVICE_PORT_NO_PROFILE = "8081";
    public static final String TASK_SERVICE_PORT_NO_PROFILE = "8083";

    public static final MessageFormat COMPILATION_SERVICE_ENDPOINT_FORMAT = new MessageFormat("http://{0}:{1}/compiler/compile");
    public static final MessageFormat EXECUTION_SERVICE_ENDPOINT_FORMAT = new MessageFormat("http://{0}:{1}/executor/execute");
    public static final MessageFormat TASK_SERVICE_ENDPOINT_FORMAT = new MessageFormat("http://{0}:{1}/tasking/task");

    public static final String INTERNAL_SERVICE_ERROR_MESSAGE = "Internal service error occurred! Compilation service responded with empty body.";
    public static final String COMPILATION_ERROR_MESSAGE = "Compilation errors occurred while compiling client source code!";
    public static final String COMPILATION_OK_MESSAGE = "Client source code was successfully compiled!";
    public static final String TASK_PUBLISHING_OK_MESSAGE = "New coding task was successfully compiled and published!";
    public static final String TASK_EXECUTION_ERROR_MESSAGE = "Some problems occurred while task execution.";
    public static final String TASK_NOT_FOUND_ERROR_MESSAGE = "Coding task with such name wasn't found.";
}

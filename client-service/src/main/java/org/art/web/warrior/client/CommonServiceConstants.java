package org.art.web.warrior.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.MessageFormat;

public class CommonServiceConstants {

    private CommonServiceConstants() {
    }

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    //View names
    public static final String ADMIN_VIEW_NAME = "admin/admin";
    public static final String LAYOUT_VIEW_NAME = "layout/layout";

    public static final String VIEW_FRAGMENT = "fragment";
    public static final String LOGIN_FRAGMENT = "login";
    public static final String REGISTRATION_FRAGMENT = "registration";
    public static final String SUBMISSION_FRAGMENT = "submission";
    public static final String TASKS_FRAGMENT = "tasks";
    public static final String TASK_FRAGMENT = "task";

    public static final String USER_ATTR_NAME = "user";
    public static final String USER_TASK_LIST_ATTR_NAME = "userTaskList";
    public static final String TASK_ATTR_NAME = "task";
    public static final String API_ERROR_ATTR_NAME = "apiError";

    //Request URL Mapping
    public static final String TASKS = "tasks";
    public static final String REDIRECT = "redirect:";

    public static final String COMPILER_SERVICE_HOST_ENV_PROP_NAME = "COMPILER_SERVICE_HOST";
    public static final String COMPILER_SERVICE_PORT_ENV_PROP_NAME = "COMPILER_SERVICE_PORT";

    public static final String EXECUTION_SERVICE_HOST_ENV_PROP_NAME = "EXECUTION_SERVICE_HOST";
    public static final String EXECUTION_SERVICE_PORT_ENV_PROP_NAME = "EXECUTION_SERVICE_PORT";

    public static final String TASK_SERVICE_HOST_ENV_PROP_NAME = "TASK_SERVICE_HOST";
    public static final String TASK_SERVICE_PORT_ENV_PROP_NAME = "TASK_SERVICE_PORT";

    public static final String USER_SERVICE_HOST_ENV_PROP_NAME = "USER_SERVICE_HOST";
    public static final String USER_SERVICE_PORT_ENV_PROP_NAME = "USER_SERVICE_PORT";

    public static final String USER_SERVICE_PORT_NO_PROFILE = "8081";
    public static final String COMP_SERVICE_PORT_NO_PROFILE = "8082";
    public static final String EXEC_SERVICE_PORT_NO_PROFILE = "8083";
    public static final String TASK_SERVICE_PORT_NO_PROFILE = "8084";

    public static final MessageFormat USER_SERVICE_ENDPOINT_FORMAT = new MessageFormat("http://{0}/users");
    public static final MessageFormat TASK_SERVICE_ENDPOINT_FORMAT = new MessageFormat("http://{0}:{1}/tasks");
    public static final MessageFormat COMPILATION_SERVICE_ENDPOINT_FORMAT = new MessageFormat("http://{0}:{1}/compiler/compile");
    public static final MessageFormat EXECUTION_SERVICE_ENDPOINT_FORMAT = new MessageFormat("http://{0}:{1}/executor/execute");

    public static final String COMPILATION_ERROR_MESSAGE = "Compilation errors occurred while compiling client source code!";

    public static final String TASK_PUBLICATION_OK_MESSAGE = "Coding task was successfully published!";
    public static final String TASK_UPDATE_OK_MESSAGE = "Coding task was successfully updated!";
    public static final String TASK_DELETE_OK_MESSAGE = "Coding task was successfully deleted!";

    public static final String EXTERNAL_SERVICE_ERROR_MESSAGE = "Some errors occurred while external service invocation!";

    //Eureka Discovery
    public static final String EXECUTION_SERVICE_NAME = "executor-service";
    public static final String COMPILER_SERVICE_NAME = "compiler-service";
    public static final String TASK_SERVICE_NAME = "task-service";
    public static final String USER_SERVICE_NAME = "user-service";
}

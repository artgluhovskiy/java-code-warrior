package org.art.web.warrior.users;

import java.text.MessageFormat;

public class CommonServiceConstants {

    private CommonServiceConstants() {
    }

    //Roles
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    public static final String INTERNAL_SERVICE_ERROR_MESSAGE = "Internal service error occurred! Service responded with an empty body.";
    public static final String UNEXPECTED_SERVICE_ERROR_MESSAGE = "Unexpected service error occurred! Service responded with unknown status code.";

    public static final String EMAIL_REGEXP = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$";
}

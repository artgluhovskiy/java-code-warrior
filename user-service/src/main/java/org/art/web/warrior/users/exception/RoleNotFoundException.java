package org.art.web.warrior.users.exception;

import org.apache.commons.lang3.StringUtils;

public class RoleNotFoundException extends RuntimeException {

    private String roleName;

    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleNotFoundException(String message, String roleName) {
        super(message);
        this.roleName = roleName;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (StringUtils.isNotBlank(roleName)) {
            message += " Role name: " + roleName;
        }
        return message;
    }
}

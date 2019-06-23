package org.art.web.warrior.users.exception;

import org.apache.commons.lang3.StringUtils;

public class UserNotFoundException extends RuntimeException {

    private String email;

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(String message, String email) {
        super(message);
        this.email = email;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (StringUtils.isNotBlank(email)) {
            message += " User email: " + email;
        }
        return message;
    }
}

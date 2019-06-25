package org.art.web.warrior.users.exception;

public class EmailExistsException extends RuntimeException {

    private String email;

    public EmailExistsException(String message, String email) {
        super(message);
        this.email = email;
    }

    @Override
    public String getMessage() {
        return getMessage() + " Email: " + email;
    }
}

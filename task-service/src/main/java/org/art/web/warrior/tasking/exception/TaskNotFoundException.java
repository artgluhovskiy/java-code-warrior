package org.art.web.warrior.tasking.exception;

public class TaskNotFoundException extends RuntimeException {

    private final String nameId;

    public TaskNotFoundException(String message, String nameId) {
        super(message);
        this.nameId = nameId;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " Task name ID: " + nameId;
    }
}
